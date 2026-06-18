package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class LoanPurpose {

    public static final String BUSINESS = "BUSINESS";
    public static final String FARMING  = "FARMING";
    public static final String PERSONAL = "PERSONAL";

    private static final Set<String> ALL = Set.of(BUSINESS, FARMING, PERSONAL);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private LoanPurpose() {}
}
