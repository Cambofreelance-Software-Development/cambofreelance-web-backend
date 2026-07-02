package com.cambofreelance.webbackend.constants;

import java.math.BigDecimal;
import java.util.Set;

public final class LoanCurrency {

    public static final String USD = "USD";
    public static final String KHR = "KHR";

    public static final BigDecimal MIN_LOAN_AMOUNT_USD = BigDecimal.valueOf(10);
    public static final BigDecimal MIN_LOAN_AMOUNT_KHR = BigDecimal.valueOf(40000);

    private static final Set<String> ALL = Set.of(USD, KHR);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    /** Minimum loan amount for the given currency. Assumes {@code value} has already been validated. */
    public static BigDecimal minLoanAmount(String value) {
        return KHR.equals(value.toUpperCase()) ? MIN_LOAN_AMOUNT_KHR : MIN_LOAN_AMOUNT_USD;
    }

    private LoanCurrency() {}
}
