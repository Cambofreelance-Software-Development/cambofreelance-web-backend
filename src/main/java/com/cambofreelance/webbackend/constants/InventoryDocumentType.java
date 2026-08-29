package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class InventoryDocumentType {

    public static final String NATIONAL_ID = "NATIONAL_ID";
    public static final String FAMILY_BOOK = "FAMILY_BOOK";
    public static final String SALARY_CERTIFICATE = "SALARY_CERTIFICATE";
    public static final String BANK_STATEMENT = "BANK_STATEMENT";
    public static final String REGISTRATION = "REGISTRATION";
    public static final String INVOICE = "INVOICE";
    public static final String CONTRACT = "CONTRACT";
    public static final String WARRANTY = "WARRANTY";
    public static final String SPECIFICATION = "SPECIFICATION";
    public static final String OTHER = "OTHER";

    private static final Set<String> ALL =
        Set.of(NATIONAL_ID, FAMILY_BOOK, SALARY_CERTIFICATE, BANK_STATEMENT,
               REGISTRATION, INVOICE, CONTRACT, WARRANTY, SPECIFICATION, OTHER);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private InventoryDocumentType() {}
}
