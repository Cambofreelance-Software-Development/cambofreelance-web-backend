package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class InstallmentStatus {

    public static final String PENDING      = "PENDING";
    public static final String PAID         = "PAID";
    public static final String PARTIAL_PAID = "PARTIAL_PAID";
    public static final String OVERDUE      = "OVERDUE";
    public static final String WAIVED       = "WAIVED";

    private static final Set<String> ALL = Set.of(PENDING, PAID, PARTIAL_PAID, OVERDUE, WAIVED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private InstallmentStatus() {}
}
