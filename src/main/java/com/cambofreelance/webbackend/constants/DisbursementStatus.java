package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class DisbursementStatus {

    public static final String PENDING   = "PENDING";
    public static final String COMPLETED = "COMPLETED";

    private static final Set<String> ALL = Set.of(PENDING, COMPLETED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private DisbursementStatus() {}
}
