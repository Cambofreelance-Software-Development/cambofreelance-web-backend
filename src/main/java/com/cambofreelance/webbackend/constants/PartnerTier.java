package com.cambofreelance.webbackend.constants;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Partner commission tier — derived live from the number of distinct referred users who
 * currently hold an ACTIVE subscription. Not stored on the application row.
 *
 *   BRONZE  0–4 active   → 10%
 *   SILVER  5–14 active  → 18%
 *   GOLD    15+ active   → 25%
 */
public final class PartnerTier {

    public static final String BRONZE = "BRONZE";
    public static final String SILVER = "SILVER";
    public static final String GOLD   = "GOLD";

    public static final int SILVER_MIN = 5;
    public static final int GOLD_MIN   = 15;

    private static final Map<String, BigDecimal> RATES = Map.of(
        BRONZE, new BigDecimal("0.10"),
        SILVER, new BigDecimal("0.18"),
        GOLD,   new BigDecimal("0.25")
    );

    public static String tierFor(long activeReferredCount) {
        if (activeReferredCount >= GOLD_MIN)   return GOLD;
        if (activeReferredCount >= SILVER_MIN) return SILVER;
        return BRONZE;
    }

    public static BigDecimal rateOf(String tier) {
        return RATES.getOrDefault(tier, RATES.get(BRONZE));
    }

    private PartnerTier() {}
}
