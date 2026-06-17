package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class CustomerStatus {

    public static final String DRAFT = "DRAFT";
    public static final String PENDING_VERIFICATION = "PENDING_VERIFICATION";
    public static final String VERIFIED = "VERIFIED";
    public static final String REJECTED = "REJECTED";
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";

    private static final Set<String> ALL =
        Set.of(DRAFT, PENDING_VERIFICATION, VERIFIED, REJECTED, ACTIVE, INACTIVE);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private CustomerStatus() {}
}
