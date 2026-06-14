package com.cambofreelance.authenticationservice.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ArticleWorkflowStatus {

    public static final String DRAFT     = "DRAFT";
    public static final String REVIEW    = "REVIEW";
    public static final String APPROVAL  = "APPROVAL";
    public static final String PUBLISHED = "PUBLISHED";
    public static final String ARCHIVED  = "ARCHIVED";

    /** Allowed forward and backward transitions per state. */
    private static final Map<String, Set<String>> TRANSITIONS = new HashMap<>();

    static {
        TRANSITIONS.put(DRAFT,     Set.of(REVIEW));
        TRANSITIONS.put(REVIEW,    Set.of(APPROVAL, DRAFT));
        TRANSITIONS.put(APPROVAL,  Set.of(PUBLISHED, REVIEW));
        TRANSITIONS.put(PUBLISHED, Set.of(ARCHIVED));
        TRANSITIONS.put(ARCHIVED,  Set.of(DRAFT));
    }

    public static boolean isValid(String value) {
        if (value == null) return false;
        return TRANSITIONS.containsKey(value.toUpperCase());
    }

    public static boolean canTransition(String from, String to) {
        if (from == null || to == null) return false;
        Set<String> allowed = TRANSITIONS.get(from.toUpperCase());
        return allowed != null && allowed.contains(to.toUpperCase());
    }

    private ArticleWorkflowStatus() {}
}
