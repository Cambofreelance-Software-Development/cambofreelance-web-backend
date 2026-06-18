package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class CustomerDocumentType {

    public static final String NATIONAL_ID_FRONT = "NATIONAL_ID_FRONT";
    public static final String NATIONAL_ID_BACK = "NATIONAL_ID_BACK";
    public static final String CUSTOMER_PHOTO = "CUSTOMER_PHOTO";
    public static final String ADDRESS_VERIFICATION = "ADDRESS_VERIFICATION";
    public static final String INCOME_VERIFICATION = "INCOME_VERIFICATION";
    public static final String GUARANTOR_ID = "GUARANTOR_ID";

    private static final Set<String> ALL = Set.of(
        NATIONAL_ID_FRONT, NATIONAL_ID_BACK, CUSTOMER_PHOTO,
        ADDRESS_VERIFICATION, INCOME_VERIFICATION, GUARANTOR_ID
    );

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private CustomerDocumentType() {}
}
