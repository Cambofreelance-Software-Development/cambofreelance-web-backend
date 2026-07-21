package com.cambofreelance.webbackend.constants;

public final class ArticleType {

    public static final String NEWS          = "NEWS";
    public static final String PROMOTIONS    = "PROMOTIONS";
    public static final String BLOGS         = "BLOGS";
    public static final String ANNOUNCEMENTS = "ANNOUNCEMENTS";
    public static final String SERVICE       = "SERVICE";
    public static final String TEAM          = "TEAM";
    public static final String COURSE        = "COURSE";
    public static final String PRODUCTS      = "PRODUCTS";
    public static final String TUTORIAL      = "TUTORIAL";
    public static final String STATS         = "STATS";
    public static final String REVIEW        = "REVIEW";
    public static final String FEATURE       = "FEATURE";
    public static final String BUSINESS_TYPES = "BUSINESS-TYPES";
    public static final String PARTNER       = "PARTNER";
    public static final String CONTACT       = "CONTACT";
    public static final String ABOUT_US      = "ABOUT-US";
    public static final String ARTICLE_HERO_SECTION = "ARTICLE-HERO-SECTION";

    private static final String[] ALL = {
        NEWS, PROMOTIONS, BLOGS, ANNOUNCEMENTS, SERVICE, TEAM, COURSE, PRODUCTS, TUTORIAL, STATS, REVIEW, FEATURE, BUSINESS_TYPES, PARTNER, CONTACT, ABOUT_US, ARTICLE_HERO_SECTION
    };

    public static boolean isValid(String value) {
        if (value == null) return false;
        String upper = value.toUpperCase();
        for (String type : ALL) {
            if (type.equals(upper)) return true;
        }
        return false;
    }

    private ArticleType() {}
}
