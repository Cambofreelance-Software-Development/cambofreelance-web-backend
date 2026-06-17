package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class LoanApplicationStatus {

    public static final String DRAFT            = "DRAFT";
    public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
    public static final String APPROVED         = "APPROVED";
    public static final String REJECTED         = "REJECTED";

    private static final Set<String> ALL = Set.of(DRAFT, PENDING_APPROVAL, APPROVED, REJECTED);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private LoanApplicationStatus() {}
}
