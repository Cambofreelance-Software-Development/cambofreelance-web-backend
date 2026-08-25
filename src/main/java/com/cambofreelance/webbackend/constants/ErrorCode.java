package com.cambofreelance.webbackend.constants;

public final class ErrorCode {
    public final static String SUCCESS = "SUC-00001";
    public final static String GENERAL_ERROR = "ERR-00002";
    public final static String BAD_REQUEST = "ERR-00001";
    public final static String INTERNAL_SERVER_ERROR = "ERR-500";
    public final static String NAME_ALREADY_USE = "ERR-003";
    public final static String USER_NOT_PERMISSION = "ERR-004";
    public final static String USER_UNAUTHORIZED = "ERR-401";
    public final static String INVALID_REQ_ERROR = "ERR-001";
    public final static String ALREADY_EXIST_LOGIN_ID = "ERR-0001";
    public final static String ACCOUNT_NOT_FOUND = "ERR-0002";
    public final static String CONFIRM_PASSWORD_NOT_MATCH = "ERR-0003";
    public final static String CONFIRM_CURRENT_PASSWORD_NOT_MATCH = "ERR-0004";
    public final static String ACCOUNT_NOT_ENOUGH_BALANCE = "ERR-0005";
    public final static String CREATE_ACCOUNT_SUCCESS = "SUC-001";
    public final static String UPDATE_ACCOUNT_SUCCESS = "SUC-002";
    public final static String CHANGE_PASSWORD_ACCOUNT_SUCCESS = "SUC-003";
    public final static String CREATE_DATA_SUCCESS = "SUC-003";
    public final static String UPDATE_DATA_SUCCESS = "SUC-004";
    public final static String DELETE_DATA_SUCCESS = "SUC-005";
    public final static String UPDATE_PASSWORD_SUCCESS = "SUC-006";

    public final static String USERNAME_ALREADY_EXIST = "ERR-0006";
    public final static String EMAIL_ALREADY_EXIST = "ERR-0007";
    public final static String PHONE_ALREADY_EXIST = "ERR-0008";
  public static final String UNAUTHORIZED = "ERR-00003";
    public static final String LOGIN_SUCCESS = "ERR-00007";
    public static final String INVALID_OTP   = "ERR-0011";
    public static final String OTP_EXPIRED   = "ERR-0012";

    public static final String ACCOUNT_NOT_APPROVED         = "ERR-0013";
    public static final String SUBSCRIPTION_ALREADY_ACTIVE  = "ERR-0014";
    public static final String PAYMENT_NOT_FOUND            = "ERR-0015";
    public static final String PAYMENT_NOT_COMPLETED        = "ERR-0016";
    public static final String PLAN_NOT_AVAILABLE           = "ERR-0017";
    public static final String PLAN_CHANGE_NOT_UPGRADE      = "ERR-0018";
    public static final String AUTO_RENEW_NOT_AVAILABLE     = "ERR-0019";
    public static final String AUTO_RENEW_TOKEN_MISSING     = "ERR-0020";
    public static final String ACTIVE_SUBSCRIPTION_NOT_FOUND = "ERR-0021";
    public static final String GOOGLE_AUTH_FAILED = "ERR-0022";
    public static final String ACCOUNT_NOT_VERIFIED = "ERR-0023";
    public static final String OTP_SEND_LIMIT_EXCEEDED = "ERR-0024";
}
