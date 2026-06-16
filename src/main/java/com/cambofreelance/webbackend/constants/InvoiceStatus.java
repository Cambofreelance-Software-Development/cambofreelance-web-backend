package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class InvoiceStatus {

    public static final String DRAFT = "DRAFT";
    public static final String UNPAID = "UNPAID";
    public static final String PAID = "PAID";

    private static final Set<String> ALL = Set.of(DRAFT, UNPAID, PAID);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private InvoiceStatus() {}
}
