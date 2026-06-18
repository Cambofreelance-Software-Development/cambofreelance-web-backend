package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.response.CurrencyDataPoint;
import com.cambofreelance.webbackend.dto.response.DashboardResponse;
import com.cambofreelance.webbackend.dto.response.DashboardSummaryResponse;
import com.cambofreelance.webbackend.dto.response.DpdBucketPoint;
import com.cambofreelance.webbackend.dto.response.MonthlyDataPoint;
import com.cambofreelance.webbackend.services.DashboardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DashboardResponse getDashboard() {
        return DashboardResponse.builder()
            .summary(buildSummary())
            .monthlyDisbursement(monthlyDisbursement())
            .collectionTrend(collectionTrend())
            .portfolioByCurrency(portfolioByCurrency())
            .overdueAnalysis(overdueAnalysis())
            .build();
    }

    private DashboardSummaryResponse buildSummary() {
        long totalCustomers = num(em.createNativeQuery(
            "SELECT COUNT(*) FROM customers WHERE status = 'ACT'").getSingleResult());

        long totalActiveLoans = num(em.createNativeQuery(
            "SELECT COUNT(*) FROM loan_applications WHERE application_status = 'APPROVED' AND status = 'ACT'").getSingleResult());

        BigDecimal totalLoanPortfolio = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(loan_amount),0) FROM loan_applications WHERE application_status = 'APPROVED' AND status = 'ACT'").getSingleResult());

        BigDecimal todayCollections = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(total_paid),0) FROM payments WHERE payment_date = CURRENT_DATE AND payment_status = 'ACTIVE' AND status = 'ACT'").getSingleResult());

        long overdueLoans = num(em.createNativeQuery(
            "SELECT COUNT(DISTINCT loan_application_id) FROM repayment_installments WHERE installment_status = 'OVERDUE'").getSingleResult());

        BigDecimal outstandingBalance = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(remaining_balance),0) FROM repayment_installments WHERE installment_status IN ('PENDING','PARTIAL_PAID','OVERDUE')").getSingleResult());

        return DashboardSummaryResponse.builder()
            .totalCustomers(totalCustomers)
            .totalActiveLoans(totalActiveLoans)
            .totalLoanPortfolio(totalLoanPortfolio)
            .todayCollections(todayCollections)
            .overdueLoans(overdueLoans)
            .outstandingBalance(outstandingBalance)
            .build();
    }

    @SuppressWarnings("unchecked")
    private List<MonthlyDataPoint> monthlyDisbursement() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', d.disbursement_date), 'YYYY-MM') as month," +
            "       COALESCE(SUM(d.disbursement_amount), 0) as total " +
            "FROM loan_disbursements d " +
            "WHERE d.disbursement_date >= CURRENT_DATE - INTERVAL '12 months' " +
            "  AND d.disbursement_status = 'COMPLETED' AND d.status = 'ACT' " +
            "GROUP BY DATE_TRUNC('month', d.disbursement_date) " +
            "ORDER BY DATE_TRUNC('month', d.disbursement_date)").getResultList();
        return rows.stream().map(r -> new MonthlyDataPoint((String) r[0], dec(r[1]))).toList();
    }

    @SuppressWarnings("unchecked")
    private List<MonthlyDataPoint> collectionTrend() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', p.payment_date), 'YYYY-MM') as month," +
            "       COALESCE(SUM(p.total_paid), 0) as total " +
            "FROM payments p " +
            "WHERE p.payment_date >= CURRENT_DATE - INTERVAL '12 months' " +
            "  AND p.payment_status = 'ACTIVE' AND p.status = 'ACT' " +
            "GROUP BY DATE_TRUNC('month', p.payment_date) " +
            "ORDER BY DATE_TRUNC('month', p.payment_date)").getResultList();
        return rows.stream().map(r -> new MonthlyDataPoint((String) r[0], dec(r[1]))).toList();
    }

    @SuppressWarnings("unchecked")
    private List<CurrencyDataPoint> portfolioByCurrency() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT la.currency, COUNT(*) as loan_count, COALESCE(SUM(la.loan_amount), 0) as total_amount " +
            "FROM loan_applications la " +
            "WHERE la.application_status = 'APPROVED' AND la.status = 'ACT' " +
            "GROUP BY la.currency ORDER BY la.currency").getResultList();
        return rows.stream().map(r -> new CurrencyDataPoint((String) r[0], num(r[1]), dec(r[2]))).toList();
    }

    @SuppressWarnings("unchecked")
    private List<DpdBucketPoint> overdueAnalysis() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT " +
            "  CASE WHEN dpd BETWEEN 1 AND 30 THEN '1-30 days' " +
            "       WHEN dpd BETWEEN 31 AND 60 THEN '31-60 days' " +
            "       WHEN dpd BETWEEN 61 AND 90 THEN '61-90 days' " +
            "       ELSE '90+ days' END as bucket, " +
            "  COUNT(*) as loan_count, " +
            "  COALESCE(SUM(total_overdue_amount), 0) as total_overdue " +
            "FROM collection_cases " +
            "WHERE dpd > 0 AND status = 'ACT' " +
            "GROUP BY bucket ORDER BY MIN(dpd)").getResultList();
        return rows.stream().map(r -> new DpdBucketPoint((String) r[0], num(r[1]), dec(r[2]))).toList();
    }

    private static long num(Object v) {
        return v == null ? 0L : ((Number) v).longValue();
    }

    private static BigDecimal dec(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal bd) return bd;
        return new BigDecimal(v.toString());
    }
}
