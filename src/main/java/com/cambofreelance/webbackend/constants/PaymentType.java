package com.cambofreelance.webbackend.constants;

public final class PaymentType {
    public static final String PARTIAL         = "PARTIAL";
    public static final String FULL_SETTLEMENT = "FULL_SETTLEMENT";

    private PaymentType() {}

    public static boolean isValid(String type) {
        return PARTIAL.equals(type) || FULL_SETTLEMENT.equals(type);
    }
}
