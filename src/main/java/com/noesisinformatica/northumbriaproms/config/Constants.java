package com.noesisinformatica.northumbriaproms.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String BOOKINGS_QUEUE = "promsapp_bookings_queue";
    public static final String PLANS_QUEUE = "promsapp_plans_queue";
    public static final String ACTIONS_QUEUE = "promsapp_actions_queue";

    private Constants() {
    }
}
