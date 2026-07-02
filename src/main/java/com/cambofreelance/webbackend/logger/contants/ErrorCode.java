package com.cambofreelance.webbackend.logger.contants;

public final class ErrorCode {
    public static final String SUCCESS = "SUC-00001";
    public static final String USER_CREATE_SUC = "SUC-00002";
    public static final String USER_UPDATE_SUC = "SUC-00003";
    public static final String USER_DELETE_SUC = "SUC-00004";
    public static final String GENERAL_ERROR = "ERR-00002";
    public static final String INVALID_REQ_ERROR = "ERR-00001";
    public static final String UNAUTHORIZED = "ERR-00003";
    public static final String ACCOUNT_NOT_REQUIRE_CHANGE_PASSWORD = "ERR-00004";
    public static final String USER_ALREADY_EXISTS = "ERR-00005";
    public static final String USER_NOT_FOUND = "ERR-00006";
    public static final String LOGIN_SUCCESS = "ERR-00007";
    public static final String USER_IS_NOT_ACTIVE = "ERR-00008";
    public static final String ACCESS_DENIED = "ERR-00009";
    public static final String USERNAME_ALREADY_EXIST = "ERR-00010";
    public static final String EMAIL_ALREADY_EXIST = "ERR-00011";
    public static final String PHONE_ALREADY_EXIST = "ERR-00012";
    public static final String INCORRECT_USERNAME_PASSWORD = "ERR-00013";

    public static final String CHANGE_PASSWORD_SUCCESS = "SUC-00005";
    public static final String UPDATE_PROFILE_SUCCESS = "SUC-00006";

    public static final String ARTICLE_NOT_FOUND          = "ERR-00014";
    public static final String INVALID_ARTICLE_TYPE        = "ERR-00015";
    public static final String PHONE_ALREADY_EXISTS        = "ERR-00016";
    public static final String IDENTITY_CARD_ALREADY_EXISTS = "ERR-00017";

    private ErrorCode() {
    }
}