package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class TrackingType {

    public static final String QUANTITY = "QUANTITY";
    public static final String BATCH = "BATCH";
    public static final String SERIALIZED = "SERIALIZED";

    private static final Set<String> ALL =
        Set.of(QUANTITY, BATCH, SERIALIZED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private TrackingType() {}
}
