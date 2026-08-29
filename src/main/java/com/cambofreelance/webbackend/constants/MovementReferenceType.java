package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class MovementReferenceType {

    public static final String PO = "PO";
    public static final String SALE = "SALE";
    public static final String TRANSFER = "TRANSFER";
    public static final String ADJUSTMENT = "ADJUSTMENT";
    public static final String INITIAL = "INITIAL";

    private static final Set<String> ALL =
        Set.of(PO, SALE, TRANSFER, ADJUSTMENT, INITIAL);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private MovementReferenceType() {}
}
