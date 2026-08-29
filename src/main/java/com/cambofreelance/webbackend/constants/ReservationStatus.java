package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class ReservationStatus {

    public static final String ACTIVE = "ACTIVE";
    public static final String RELEASED = "RELEASED";
    public static final String EXPIRED = "EXPIRED";
    public static final String CONVERTED = "CONVERTED";

    private static final Set<String> ALL =
        Set.of(ACTIVE, RELEASED, EXPIRED, CONVERTED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private ReservationStatus() {}
}
