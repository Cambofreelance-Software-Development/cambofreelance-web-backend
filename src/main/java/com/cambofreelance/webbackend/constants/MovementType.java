package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class MovementType {

    public static final String OPENING = "OPENING";
    public static final String PURCHASE = "PURCHASE";
    public static final String SALE = "SALE";
    public static final String RETURN_IN = "RETURN_IN";
    public static final String RETURN_OUT = "RETURN_OUT";
    public static final String TRANSFER_IN = "TRANSFER_IN";
    public static final String TRANSFER_OUT = "TRANSFER_OUT";
    public static final String ADJUSTMENT_IN = "ADJUSTMENT_IN";
    public static final String ADJUSTMENT_OUT = "ADJUSTMENT_OUT";
    public static final String DAMAGE = "DAMAGE";

    private static final Set<String> ALL =
        Set.of(OPENING, PURCHASE, SALE, RETURN_IN, RETURN_OUT,
               TRANSFER_IN, TRANSFER_OUT, ADJUSTMENT_IN, ADJUSTMENT_OUT, DAMAGE);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private MovementType() {}
}
