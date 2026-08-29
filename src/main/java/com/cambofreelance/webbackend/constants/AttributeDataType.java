package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class AttributeDataType {

    public static final String TEXT = "TEXT";
    public static final String NUMBER = "NUMBER";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String DATE = "DATE";
    public static final String SELECT = "SELECT";

    private static final Set<String> ALL =
        Set.of(TEXT, NUMBER, BOOLEAN, DATE, SELECT);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private AttributeDataType() {}
}
