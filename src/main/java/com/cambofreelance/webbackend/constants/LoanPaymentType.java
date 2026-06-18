package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class LoanPaymentType {

    public static final String DAILY   = "DAILY";
    public static final String WEEKLY  = "WEEKLY";
    public static final String MONTHLY = "MONTHLY";

    private static final Set<String> ALL = Set.of(DAILY, WEEKLY, MONTHLY);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private LoanPaymentType() {}
}
