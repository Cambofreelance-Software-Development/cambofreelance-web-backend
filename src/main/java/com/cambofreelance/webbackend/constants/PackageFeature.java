package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class PackageFeature {

    public static final String CUSTOMER_MANAGEMENT = "CUSTOMER_MANAGEMENT";
    public static final String LOAN_MANAGEMENT = "LOAN_MANAGEMENT";
    public static final String PAYMENT_COLLECTION = "PAYMENT_COLLECTION";
    public static final String DASHBOARD_REPORTS = "DASHBOARD_REPORTS";
    public static final String DOCUMENT_MANAGEMENT = "DOCUMENT_MANAGEMENT";
    public static final String MOBILE_APP_ACCESS = "MOBILE_APP_ACCESS";
    public static final String API_ACCESS = "API_ACCESS";
    public static final String CUSTOM_BRANDING = "CUSTOM_BRANDING";
    public static final String MULTI_BRANCH_SUPPORT = "MULTI_BRANCH_SUPPORT";
    public static final String WHITE_LABEL = "WHITE_LABEL";

    public static final Set<String> ALL = Set.of(
        CUSTOMER_MANAGEMENT, LOAN_MANAGEMENT, PAYMENT_COLLECTION, DASHBOARD_REPORTS,
        DOCUMENT_MANAGEMENT, MOBILE_APP_ACCESS, API_ACCESS, CUSTOM_BRANDING,
        MULTI_BRANCH_SUPPORT, WHITE_LABEL
    );

    public static boolean isValid(String code) {
        return code != null && ALL.contains(code.toUpperCase());
    }

    private PackageFeature() {}
}
