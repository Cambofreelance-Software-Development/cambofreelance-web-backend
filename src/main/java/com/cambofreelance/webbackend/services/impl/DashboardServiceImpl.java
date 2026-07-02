package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.response.CurrencyDataPoint;
import com.cambofreelance.webbackend.dto.response.DashboardResponse;
import com.cambofreelance.webbackend.dto.response.DashboardSummaryResponse;
import com.cambofreelance.webbackend.dto.response.DpdBucketPoint;
import com.cambofreelance.webbackend.dto.response.LoanCurrencySummary;
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

    private static final List<String> CURRENCIES = List.of("USD", "KHR");

    @Override
    public DashboardResponse getDashboard() {
        return DashboardResponse.builder()
            .summary(buildSummary())
            .loansByCurrency(loansByCurrency())
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

        // These three are USD-only: this app never sums USD and KHR together (no FX conversion exists
        // anywhere in the codebase), so a currency-blind SUM would mix raw amounts from two different
        // currencies (KHR figures run ~4000x USD for the same real value) into one meaningless number.
        // Per-currency figures are available separately via loansByCurrency()/portfolioByCurrency().
        BigDecimal totalLoanPortfolio = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(loan_amount),0) FROM loan_applications " +
            "WHERE currency = 'USD' AND application_status = 'APPROVED' AND status = 'ACT'").getSingleResult());

        BigDecimal todayCollectionsUsd = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(p.total_paid),0) " +
            "FROM payments p " +
            "JOIN loan_applications la ON la.id = p.loan_application_id " +
            "WHERE la.currency = 'USD' AND p.payment_date = CURRENT_DATE " +
            "  AND p.payment_status = 'ACTIVE' AND p.status = 'ACT'").getSingleResult());

        BigDecimal todayCollectionsKhr = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(p.total_paid),0) " +
            "FROM payments p " +
            "JOIN loan_applications la ON la.id = p.loan_application_id " +
            "WHERE la.currency = 'KHR' AND p.payment_date = CURRENT_DATE " +
            "  AND p.payment_status = 'ACTIVE' AND p.status = 'ACT'").getSingleResult());

        long overdueLoans = num(em.createNativeQuery(
            "SELECT COUNT(DISTINCT loan_application_id) FROM repayment_installments WHERE installment_status = 'OVERDUE'").getSingleResult());

        BigDecimal outstandingBalance = dec(em.createNativeQuery(
            "SELECT COALESCE(SUM(ri.remaining_balance),0) " +
            "FROM repayment_installments ri " +
            "JOIN loan_applications la ON la.id = ri.loan_application_id " +
            "WHERE la.currency = 'USD' AND ri.installment_status IN ('PENDING','PARTIAL_PAID','OVERDUE')").getSingleResult());

        return DashboardSummaryResponse.builder()
            .totalCustomers(totalCustomers)
            .totalActiveLoans(totalActiveLoans)
            .totalLoanPortfolio(totalLoanPortfolio)
            .todayCollectionsUsd(todayCollectionsUsd)
            .todayCollectionsKhr(todayCollectionsKhr)
            .overdueLoans(overdueLoans)
            .outstandingBalance(outstandingBalance)
            .build();
    }

    @SuppressWarnings("unchecked")
    private List<MonthlyDataPoint> monthlyDisbursement() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', d.disbursement_date), 'YYYY-MM') as month," +
            "       COALESCE(SUM(CASE WHEN d.currency = 'USD' THEN d.disbursement_amount ELSE 0 END), 0) as total_usd," +
            "       COALESCE(SUM(CASE WHEN d.currency = 'KHR' THEN d.disbursement_amount ELSE 0 END), 0) as total_khr " +
            "FROM loan_disbursements d " +
            "WHERE d.disbursement_date >= CURRENT_DATE - INTERVAL '12 months' " +
            "  AND d.disbursement_status = 'COMPLETED' AND d.status = 'ACT' " +
            "GROUP BY DATE_TRUNC('month', d.disbursement_date) " +
            "ORDER BY DATE_TRUNC('month', d.disbursement_date)").getResultList();
        return rows.stream().map(r -> new MonthlyDataPoint((String) r[0], dec(r[1]), dec(r[2]))).toList();
    }

    @SuppressWarnings("unchecked")
    private List<MonthlyDataPoint> collectionTrend() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', p.payment_date), 'YYYY-MM') as month," +
            "       COALESCE(SUM(CASE WHEN la.currency = 'USD' THEN p.total_paid ELSE 0 END), 0) as total_usd," +
            "       COALESCE(SUM(CASE WHEN la.currency = 'KHR' THEN p.total_paid ELSE 0 END), 0) as total_khr " +
            "FROM payments p " +
            "JOIN loan_applications la ON la.id = p.loan_application_id " +
            "WHERE p.payment_date >= CURRENT_DATE - INTERVAL '12 months' " +
            "  AND p.payment_status = 'ACTIVE' AND p.status = 'ACT' " +
            "GROUP BY DATE_TRUNC('month', p.payment_date) " +
            "ORDER BY DATE_TRUNC('month', p.payment_date)").getResultList();
        return rows.stream().map(r -> new MonthlyDataPoint((String) r[0], dec(r[1]), dec(r[2]))).toList();
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

    private List<LoanCurrencySummary> loansByCurrency() {
        return CURRENCIES.stream().map(currency -> {
            long activeLoans = num(em.createNativeQuery(
                "SELECT COUNT(*) FROM loan_applications " +
                "WHERE currency = ?1 AND application_status = 'APPROVED' AND status = 'ACT'")
                .setParameter(1, currency).getSingleResult());

            long totalApplications = num(em.createNativeQuery(
                "SELECT COUNT(*) FROM loan_applications WHERE currency = ?1 AND status = 'ACT'")
                .setParameter(1, currency).getSingleResult());

            BigDecimal portfolioAmount = dec(em.createNativeQuery(
                "SELECT COALESCE(SUM(loan_amount),0) FROM loan_applications " +
                "WHERE currency = ?1 AND application_status = 'APPROVED' AND status = 'ACT'")
                .setParameter(1, currency).getSingleResult());

            BigDecimal outstandingBalance = dec(em.createNativeQuery(
                "SELECT COALESCE(SUM(ri.remaining_balance),0) " +
                "FROM repayment_installments ri " +
                "JOIN loan_applications la ON la.id = ri.loan_application_id " +
                "WHERE la.currency = ?1 AND ri.installment_status IN ('PENDING','PARTIAL_PAID','OVERDUE')")
                .setParameter(1, currency).getSingleResult());

            BigDecimal todayCollections = dec(em.createNativeQuery(
                "SELECT COALESCE(SUM(p.total_paid),0) " +
                "FROM payments p " +
                "JOIN loan_applications la ON la.id = p.loan_application_id " +
                "WHERE la.currency = ?1 AND p.payment_date = CURRENT_DATE " +
                "  AND p.payment_status = 'ACTIVE' AND p.status = 'ACT'")
                .setParameter(1, currency).getSingleResult());

            return new LoanCurrencySummary(currency, activeLoans, totalApplications,
                portfolioAmount, outstandingBalance, todayCollections);
        }).toList();
    }

    @SuppressWarnings("unchecked")
    private List<DpdBucketPoint> overdueAnalysis() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT " +
            "  CASE WHEN cc.dpd BETWEEN 1 AND 30 THEN '1-30 days' " +
            "       WHEN cc.dpd BETWEEN 31 AND 60 THEN '31-60 days' " +
            "       WHEN cc.dpd BETWEEN 61 AND 90 THEN '61-90 days' " +
            "       ELSE '90+ days' END as bucket, " +
            "  COUNT(*) as loan_count, " +
            "  COALESCE(SUM(CASE WHEN la.currency = 'USD' THEN cc.total_overdue_amount ELSE 0 END), 0) as total_overdue_usd, " +
            "  COALESCE(SUM(CASE WHEN la.currency = 'KHR' THEN cc.total_overdue_amount ELSE 0 END), 0) as total_overdue_khr " +
            "FROM collection_cases cc " +
            "JOIN loan_applications la ON la.id = cc.loan_application_id " +
            "WHERE cc.dpd > 0 AND cc.status = 'ACT' " +
            "GROUP BY bucket ORDER BY MIN(cc.dpd)").getResultList();
        return rows.stream().map(r -> new DpdBucketPoint((String) r[0], num(r[1]), dec(r[2]), dec(r[3]))).toList();
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
