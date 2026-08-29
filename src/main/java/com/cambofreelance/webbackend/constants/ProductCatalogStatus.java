package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class ProductCatalogStatus {

    public static final String DRAFT = "DRAFT";
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";

    private static final Set<String> ALL =
        Set.of(DRAFT, ACTIVE, INACTIVE);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private ProductCatalogStatus() {}
}
