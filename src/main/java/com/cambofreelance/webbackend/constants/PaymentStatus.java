package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class PaymentStatus {

    public static final String PENDING = "PENDING";
    public static final String PAID = "PAID";
    public static final String OVERDUE = "OVERDUE";

    private static final Set<String> ALL = Set.of(PENDING, PAID, OVERDUE);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private PaymentStatus() {}
}
