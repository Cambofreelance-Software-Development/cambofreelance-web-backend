package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class FinancingStatus {

    public static final String DRAFT = "DRAFT";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String UNDER_REVIEW = "UNDER_REVIEW";
    public static final String ADDITIONAL_DOCUMENT_REQUIRED = "ADDITIONAL_DOCUMENT_REQUIRED";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
    public static final String CANCELLED = "CANCELLED";
    public static final String EXPIRED = "EXPIRED";

    private static final Set<String> ALL =
        Set.of(DRAFT, SUBMITTED, UNDER_REVIEW, ADDITIONAL_DOCUMENT_REQUIRED,
               APPROVED, REJECTED, CANCELLED, EXPIRED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private FinancingStatus() {}
}
