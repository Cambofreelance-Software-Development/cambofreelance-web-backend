package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class LoanDocumentType {

    public static final String APPLICATION_FORM    = "APPLICATION_FORM";
    public static final String ID_FRONT            = "ID_FRONT";
    public static final String ID_BACK             = "ID_BACK";
    public static final String INCOME_PROOF        = "INCOME_PROOF";
    public static final String COLLATERAL_PHOTO    = "COLLATERAL_PHOTO";
    public static final String BUSINESS_LICENSE    = "BUSINESS_LICENSE";
    public static final String LAND_TITLE          = "LAND_TITLE";
    public static final String OTHER               = "OTHER";

    private static final Set<String> ALL = Set.of(
        APPLICATION_FORM, ID_FRONT, ID_BACK, INCOME_PROOF,
        COLLATERAL_PHOTO, BUSINESS_LICENSE, LAND_TITLE, OTHER
    );

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private LoanDocumentType() {}
}
