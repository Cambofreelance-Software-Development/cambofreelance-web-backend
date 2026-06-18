package com.cambofreelance.webbackend.constants;

public final class PaymentMethod {
    public static final String CASH     = "CASH";
    public static final String TRANSFER = "TRANSFER";

    private PaymentMethod() {}

    public static boolean isValid(String method) {
        return CASH.equals(method) || TRANSFER.equals(method);
    }
}
