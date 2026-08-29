package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class ProductType {

    public static final String STOCK = "STOCK";
    public static final String VEHICLE = "VEHICLE";
    public static final String PART = "PART";
    public static final String ELECTRONICS = "ELECTRONICS";
    public static final String SERVICE = "SERVICE";

    private static final Set<String> ALL =
        Set.of(STOCK, VEHICLE, PART, ELECTRONICS, SERVICE);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private ProductType() {}
}
