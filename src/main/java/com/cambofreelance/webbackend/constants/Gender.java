package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class Gender {

    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";
    public static final String OTHER = "OTHER";

    private static final Set<String> ALL = Set.of(MALE, FEMALE, OTHER);

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value.toUpperCase());
    }

    private Gender() {}
}
