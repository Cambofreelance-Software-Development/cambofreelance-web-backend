package com.cambofreelance.webbackend.constants;

public final class CollectionStatus {
    public static final String CURRENT     = "CURRENT";
    public static final String DELINQUENT  = "DELINQUENT";
    public static final String OVERDUE     = "OVERDUE";
    public static final String WRITTEN_OFF = "WRITTEN_OFF";

    public static String fromDpd(int dpd) {
        if (dpd <= 0)  return CURRENT;
        if (dpd <= 30) return DELINQUENT;
        if (dpd <= 90) return OVERDUE;
        return WRITTEN_OFF;
    }

    private CollectionStatus() {}
}
