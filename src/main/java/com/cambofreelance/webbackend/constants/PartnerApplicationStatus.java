package com.cambofreelance.webbackend.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Lifecycle of a {@code partner_applications} row — mirrors the 4-step onboarding timeline
 * shown to the applicant (see {@code PartnerApplicationResponse#buildTimeline}):
 *   1. Application Submitted            → SUBMITTED
 *   2. Document & Merchant Verification → UNDER_REVIEW
 *   3. Strategy & Demo Call             → DEMO_SCHEDULED
 *   4. Portal Activation & Payout Setup → APPROVED
 * REJECTED / WITHDRAWN are terminal from any non-terminal stage. A REJECTED/WITHDRAWN row
 * may be re-submitted in place.
 */
public final class PartnerApplicationStatus {

    public static final String SUBMITTED      = "SUBMITTED";
    public static final String UNDER_REVIEW   = "UNDER_REVIEW";
    public static final String DEMO_SCHEDULED = "DEMO_SCHEDULED";
    public static final String APPROVED       = "APPROVED";
    public static final String REJECTED       = "REJECTED";
    public static final String WITHDRAWN      = "WITHDRAWN";

    private static final Map<String, Set<String>> TRANSITIONS = new HashMap<>();

    static {
        // Every non-terminal stage may also jump straight to APPROVED/REJECTED — admins are
        // not forced through every stage, just given the option to record each one.
        TRANSITIONS.put(SUBMITTED,      Set.of(UNDER_REVIEW, DEMO_SCHEDULED, APPROVED, REJECTED, WITHDRAWN));
        TRANSITIONS.put(UNDER_REVIEW,   Set.of(DEMO_SCHEDULED, APPROVED, REJECTED, WITHDRAWN));
        TRANSITIONS.put(DEMO_SCHEDULED, Set.of(APPROVED, REJECTED, WITHDRAWN));
        TRANSITIONS.put(APPROVED,       Set.of());
        TRANSITIONS.put(REJECTED,       Set.of());
        TRANSITIONS.put(WITHDRAWN,      Set.of());
    }

    public static boolean isValid(String value) {
        return value != null && TRANSITIONS.containsKey(value.toUpperCase());
    }

    public static boolean canTransition(String from, String to) {
        if (from == null || to == null) return false;
        Set<String> allowed = TRANSITIONS.get(from.toUpperCase());
        return allowed != null && allowed.contains(to.toUpperCase());
    }

    /** Only a SUBMITTED application is still editable by the applicant. */
    public static boolean isEditable(String status) {
        return SUBMITTED.equals(status);
    }

    /** A rejected or withdrawn application can be filled in and submitted again. */
    public static boolean isReapplyable(String status) {
        return REJECTED.equals(status) || WITHDRAWN.equals(status);
    }

    private PartnerApplicationStatus() {}
}
