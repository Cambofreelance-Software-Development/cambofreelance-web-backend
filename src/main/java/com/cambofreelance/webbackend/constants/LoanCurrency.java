package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class LoanCurrency {

    public static final String USD = "USD";
    public static final String KHR = "KHR";

    private static final Set<String> ALL = Set.of(USD, KHR);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private LoanCurrency() {}
}
