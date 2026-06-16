package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class BillingCycle {

    public static final String MONTHLY = "MONTHLY";
    public static final String QUARTERLY = "QUARTERLY";
    public static final String YEARLY = "YEARLY";

    private static final Set<String> ALL = Set.of(MONTHLY, QUARTERLY, YEARLY);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    /** Number of calendar months covered by one billing cycle. */
    public static int months(String value) {
        return switch (value) {
            case QUARTERLY -> 3;
            case YEARLY -> 12;
            default -> 1;
        };
    }

    private BillingCycle() {}
}
