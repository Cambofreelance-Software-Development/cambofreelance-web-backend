package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.services.ReportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> customerList() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT c.customer_code, c.first_name, c.last_name, c.gender, c.phone_number," +
            "       c.onboarding_status, c.created_at," +
            "       COUNT(la.id) AS loan_count " +
            "FROM customers c " +
            "LEFT JOIN loan_applications la ON la.customer_id = c.id AND la.status = 'ACT' " +
            "WHERE c.status = 'ACT' " +
            "GROUP BY c.id, c.customer_code, c.first_name, c.last_name, c.gender, " +
            "         c.phone_number, c.onboarding_status, c.created_at " +
            "ORDER BY c.created_at DESC").getResultList();

        return rows.stream().map(r -> row(
            "customerCode", r[0],
            "firstName",    r[1],
            "lastName",     r[2],
            "gender",       r[3],
            "phoneNumber",  r[4],
            "status",       r[5],
            "registeredAt", r[6],
            "loanCount",    num(r[7])
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> activeLoans() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT la.loan_number, la.currency, la.loan_amount, la.interest_rate," +
            "       la.tenor_value, la.tenor_unit," +
            "       la.customer_first_name, la.customer_last_name, la.customer_code," +
            "       la.created_at," +
            "       COALESCE(SUM(CASE WHEN ri.installment_status NOT IN ('PAID','WAIVED') THEN ri.remaining_balance ELSE 0 END),0) AS outstanding " +
            "FROM loan_applications la " +
            "LEFT JOIN repayment_installments ri ON ri.loan_application_id = la.id " +
            "WHERE la.application_status = 'APPROVED' AND la.status = 'ACT' " +
            "GROUP BY la.id, la.loan_number, la.currency, la.loan_amount, la.interest_rate, " +
            "         la.tenor_value, la.tenor_unit, la.customer_first_name, la.customer_last_name, " +
            "         la.customer_code, la.created_at " +
            "ORDER BY la.created_at DESC").getResultList();

        return rows.stream().map(r -> row(
            "loanNumber",       r[0],
            "currency",         r[1],
            "loanAmount",       r[2],
            "interestRate",     r[3],
            "tenorValue",       r[4],
            "tenorUnit",        r[5],
            "customerFirstName",r[6],
            "customerLastName", r[7],
            "customerCode",     r[8],
            "createdAt",        r[9],
            "outstandingBalance", r[10]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> closedLoans() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT la.loan_number, la.currency, la.loan_amount," +
            "       la.customer_first_name, la.customer_last_name, la.customer_code, la.created_at," +
            "       COALESCE(SUM(p.total_paid), 0) AS total_paid_amount " +
            "FROM loan_applications la " +
            "LEFT JOIN payments p ON p.loan_application_id = la.id AND p.payment_status = 'ACTIVE' AND p.status = 'ACT' " +
            "WHERE la.application_status = 'APPROVED' AND la.status = 'ACT' " +
            "  AND NOT EXISTS (" +
            "    SELECT 1 FROM repayment_installments ri " +
            "    WHERE ri.loan_application_id = la.id " +
            "      AND ri.installment_status NOT IN ('PAID','WAIVED')" +
            "  ) " +
            "GROUP BY la.id, la.loan_number, la.currency, la.loan_amount, " +
            "         la.customer_first_name, la.customer_last_name, la.customer_code, la.created_at " +
            "ORDER BY la.created_at DESC").getResultList();

        return rows.stream().map(r -> row(
            "loanNumber",       r[0],
            "currency",         r[1],
            "loanAmount",       r[2],
            "customerFirstName",r[3],
            "customerLastName", r[4],
            "customerCode",     r[5],
            "createdAt",        r[6],
            "totalPaid",        r[7]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> loanPortfolio() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT la.currency, COUNT(*) AS loan_count," +
            "       COALESCE(SUM(la.loan_amount), 0) AS total_loan_amount," +
            "       COALESCE(SUM(sub.outstanding), 0) AS total_outstanding " +
            "FROM loan_applications la " +
            "LEFT JOIN (" +
            "  SELECT loan_application_id, SUM(remaining_balance) AS outstanding " +
            "  FROM repayment_installments " +
            "  WHERE installment_status NOT IN ('PAID','WAIVED') " +
            "  GROUP BY loan_application_id" +
            ") sub ON sub.loan_application_id = la.id " +
            "WHERE la.application_status = 'APPROVED' AND la.status = 'ACT' " +
            "GROUP BY la.currency ORDER BY la.currency").getResultList();

        return rows.stream().map(r -> row(
            "currency",        r[0],
            "loanCount",       num(r[1]),
            "totalLoanAmount", r[2],
            "totalOutstanding",r[3]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> dailyCollection(String dateFrom, String dateTo) {
        String sql =
            "SELECT p.receipt_number, p.loan_number, p.customer_first_name, p.customer_last_name," +
            "       p.payment_date, p.principal_paid, p.interest_paid, p.penalty_fee," +
            "       p.total_paid, p.payment_method, p.payment_type, p.currency " +
            "FROM payments p " +
            "WHERE p.payment_status = 'ACTIVE' AND p.status = 'ACT' ";
        if (dateFrom != null && !dateFrom.isBlank())
            sql += " AND p.payment_date >= CAST('" + dateFrom + "' AS DATE)";
        if (dateTo != null && !dateTo.isBlank())
            sql += " AND p.payment_date <= CAST('" + dateTo + "' AS DATE)";
        sql += " ORDER BY p.payment_date DESC, p.created_at DESC";

        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
        return rows.stream().map(r -> row(
            "receiptNumber",     r[0],
            "loanNumber",        r[1],
            "customerFirstName", r[2],
            "customerLastName",  r[3],
            "paymentDate",       r[4],
            "principalPaid",     r[5],
            "interestPaid",      r[6],
            "penaltyFee",        r[7],
            "totalPaid",         r[8],
            "paymentMethod",     r[9],
            "paymentType",       r[10],
            "currency",          r[11]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> monthlyCollection(Integer year) {
        String yearFilter = year != null
            ? " AND EXTRACT(YEAR FROM p.payment_date) = " + year
            : " AND p.payment_date >= CURRENT_DATE - INTERVAL '24 months'";
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', p.payment_date), 'YYYY-MM') AS month," +
            "       COUNT(*) AS receipt_count," +
            "       COALESCE(SUM(p.principal_paid), 0) AS total_principal," +
            "       COALESCE(SUM(p.interest_paid), 0) AS total_interest," +
            "       COALESCE(SUM(p.penalty_fee), 0) AS total_penalty," +
            "       COALESCE(SUM(p.total_paid), 0) AS total_collected " +
            "FROM payments p " +
            "WHERE p.payment_status = 'ACTIVE' AND p.status = 'ACT'" + yearFilter +
            " GROUP BY DATE_TRUNC('month', p.payment_date) " +
            "ORDER BY DATE_TRUNC('month', p.payment_date) DESC").getResultList();

        return rows.stream().map(r -> row(
            "month",          r[0],
            "receiptCount",   num(r[1]),
            "totalPrincipal", r[2],
            "totalInterest",  r[3],
            "totalPenalty",   r[4],
            "totalCollected", r[5]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> overdueReport() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT cc.loan_number, cc.customer_first_name, cc.customer_last_name, cc.currency," +
            "       cc.dpd, cc.total_overdue_amount, cc.penalty_amount, cc.collection_status " +
            "FROM collection_cases cc " +
            "WHERE cc.dpd > 0 AND cc.status = 'ACT' " +
            "ORDER BY cc.dpd DESC").getResultList();

        return rows.stream().map(r -> row(
            "loanNumber",        r[0],
            "customerFirstName", r[1],
            "customerLastName",  r[2],
            "currency",          r[3],
            "dpd",               num(r[4]),
            "totalOverdueAmount",r[5],
            "penaltyAmount",     r[6],
            "collectionStatus",  r[7]
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> interestIncome(Integer year) {
        String yearFilter = year != null
            ? " AND EXTRACT(YEAR FROM p.payment_date) = " + year
            : " AND p.payment_date >= CURRENT_DATE - INTERVAL '24 months'";
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', p.payment_date), 'YYYY-MM') AS month," +
            "       COALESCE(SUM(p.interest_paid), 0) AS interest_income," +
            "       COUNT(*) AS transaction_count " +
            "FROM payments p " +
            "WHERE p.payment_status = 'ACTIVE' AND p.status = 'ACT'" + yearFilter +
            " GROUP BY DATE_TRUNC('month', p.payment_date) " +
            "ORDER BY DATE_TRUNC('month', p.payment_date)").getResultList();

        return rows.stream().map(r -> row(
            "month",            r[0],
            "interestIncome",   r[1],
            "transactionCount", num(r[2])
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> serviceFeeIncome(Integer year) {
        String yearFilter = year != null
            ? " AND EXTRACT(YEAR FROM d.disbursement_date) = " + year
            : " AND d.disbursement_date >= CURRENT_DATE - INTERVAL '24 months'";
        List<Object[]> rows = em.createNativeQuery(
            "SELECT TO_CHAR(DATE_TRUNC('month', d.disbursement_date), 'YYYY-MM') AS month," +
            "       COALESCE(SUM(la.service_fee_amount), 0) AS service_fee_income," +
            "       COUNT(*) AS loan_count " +
            "FROM loan_disbursements d " +
            "JOIN loan_applications la ON la.id = d.loan_application_id " +
            "WHERE d.disbursement_status = 'COMPLETED' AND d.status = 'ACT'" + yearFilter +
            " GROUP BY DATE_TRUNC('month', d.disbursement_date) " +
            "ORDER BY DATE_TRUNC('month', d.disbursement_date)").getResultList();

        return rows.stream().map(r -> row(
            "month",           r[0],
            "serviceFeeIncome",r[1],
            "loanCount",       num(r[2])
        )).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> outstandingBalance() {
        List<Object[]> rows = em.createNativeQuery(
            "SELECT la.loan_number, la.customer_first_name, la.customer_last_name, la.currency," +
            "       la.loan_amount," +
            "       COALESCE(SUM(CASE WHEN ri.installment_status NOT IN ('PAID','WAIVED') THEN ri.remaining_balance ELSE 0 END),0) AS outstanding_balance," +
            "       COALESCE(SUM(CASE WHEN ri.installment_status NOT IN ('PAID','WAIVED') THEN ri.interest_amount ELSE 0 END),0) AS accrued_interest " +
            "FROM loan_applications la " +
            "LEFT JOIN repayment_installments ri ON ri.loan_application_id = la.id " +
            "WHERE la.application_status = 'APPROVED' AND la.status = 'ACT' " +
            "GROUP BY la.id, la.loan_number, la.customer_first_name, la.customer_last_name, la.currency, la.loan_amount " +
            "HAVING COALESCE(SUM(CASE WHEN ri.installment_status NOT IN ('PAID','WAIVED') THEN ri.remaining_balance ELSE 0 END),0) > 0 " +
            "ORDER BY outstanding_balance DESC").getResultList();

        return rows.stream().map(r -> row(
            "loanNumber",        r[0],
            "customerFirstName", r[1],
            "customerLastName",  r[2],
            "currency",          r[3],
            "loanAmount",        r[4],
            "outstandingBalance",r[5],
            "accruedInterest",   r[6]
        )).toList();
    }

    private static long num(Object v) {
        return v == null ? 0L : ((Number) v).longValue();
    }

    private static Map<String, Object> row(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }
}
