package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class InventoryItemStatus {

    public static final String AVAILABLE = "AVAILABLE";
    public static final String RESERVED = "RESERVED";
    public static final String LOAN_PENDING = "LOAN_PENDING";
    public static final String SOLD = "SOLD";
    public static final String DELIVERED = "DELIVERED";
    public static final String RETURNED = "RETURNED";
    public static final String DAMAGED = "DAMAGED";

    private static final Set<String> ALL =
        Set.of(AVAILABLE, RESERVED, LOAN_PENDING, SOLD, DELIVERED, RETURNED, DAMAGED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private InventoryItemStatus() {}
}
