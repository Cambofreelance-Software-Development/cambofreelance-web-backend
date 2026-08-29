package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class InventoryDocumentOwnerType {

    public static final String CUSTOMER = "CUSTOMER";
    public static final String SALE = "SALE";
    public static final String FINANCING_APPLICATION = "FINANCING_APPLICATION";
    public static final String INVENTORY_ITEM = "INVENTORY_ITEM";
    public static final String PRODUCT = "PRODUCT";
    public static final String VARIANT = "VARIANT";

    private static final Set<String> ALL =
        Set.of(CUSTOMER, SALE, FINANCING_APPLICATION, INVENTORY_ITEM, PRODUCT, VARIANT);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private InventoryDocumentOwnerType() {}
}
