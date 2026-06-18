package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class TenorUnit {

    public static final String DAY   = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR  = "YEAR";

    private static final Set<String> ALL = Set.of(DAY, MONTH, YEAR);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private TenorUnit() {}
}
