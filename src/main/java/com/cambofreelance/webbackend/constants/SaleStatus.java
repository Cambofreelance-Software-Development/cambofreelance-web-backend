package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class SaleStatus {

    public static final String DRAFT = "DRAFT";
    public static final String RESERVED = "RESERVED";
    public static final String LOAN_PENDING = "LOAN_PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";
    public static final String COMPLETED = "COMPLETED";

    private static final Set<String> ALL =
        Set.of(DRAFT, RESERVED, LOAN_PENDING, CONFIRMED, DELIVERED, CANCELLED, COMPLETED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private SaleStatus() {}
}
