package com.example.commericalcommon.utils;

public class Constant {
    public static final String SUCCESS_CODE = "00";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "12";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/register",
            "/auth/login",
            "/public/**",
            "/v3/api-docs/**",
            "/configuration/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/api-docs/**",
            "/posts/by-conditions",
            "/internal/**"
    };

    public static class DefaultRole {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String ARTIST = "ARTIST";
    }

    public static class PrefixNo {
        public static final String USER_NO = "USR";
        public static final String PRODUCT_NO = "PRD";
        public static final String ORDER_NO = "ORD";
        public static final String ATTACHMENT_NO = "ATT";
    }

    public static class Status {
        public static final String ACTIVE = "A";
        public static final String INACTIVE = "I";
        public static final String DELETED = "D";
    }

    public static class Language {
        public static final String VIETNAMESE = "vi";
        public static final String ENGLISH = "en";
    }

    public static class Hashtag {
        public static class ObjectType {
            public static final String POST = "POST";
            public static final String PRODUCT = "PRODUCT";
        }
    }

    public static class Attachment {
        public static class ObjectType {
            public static final String POST = "POST";
            public static final String PRODUCT = "PRODUCT";
        }
        public static final String ALGORITHM_HASH = "SHA-1";
    }


}
