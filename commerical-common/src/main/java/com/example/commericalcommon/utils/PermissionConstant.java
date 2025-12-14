package com.example.commericalcommon.utils;

public class PermissionConstant {
    
    // AUTH
    public static class Auth {
        public static final String DELETE_ACCOUNT_INIT = "AUTH_DELETE_ACCOUNT_INIT";
        public static final String DELETE_ACCOUNT_CONFIRM = "AUTH_DELETE_ACCOUNT_CONFIRM";
        public static final String REGISTER_ARTIST = "AUTH_REGISTER_ARTIST";
        public static final String REGISTER_DEVICE = "AUTH_REGISTER_DEVICE";
        public static final String REGISTER_BIOMETRIC = "AUTH_REGISTER_BIOMETRIC";
    }

    
    // USER PROFILE
    public static class UserProfile {
        public static final String VIEW = "USER_PROFILE_VIEW";
        public static final String UPDATE = "USER_PROFILE_UPDATE";
    }

    
    // POST
    public static class Post {
        public static final String LIST_VIEW = "POST_LIST_VIEW";
        public static final String DETAIL_VIEW = "POST_DETAIL_VIEW";
        public static final String RATE_VIEW = "POST_RATE_VIEW";
        public static final String RATE_CREATE = "POST_RATE_CREATE";
        public static final String FAVORITE_TOGGLE = "POST_FAVORITE_TOGGLE";
        public static final String BLOCK = "POST_BLOCK";
        public static final String REPORT = "POST_REPORT";
        public static final String SHARE = "POST_SHARE";
        public static final String FAVORITE_LIST_VIEW = "POST_FAVORITE_LIST_VIEW";
    }

    
    // MESSAGE
    public static class Message {
        public static final String HISTORY_VIEW = "MESSAGE_HISTORY_VIEW";
        public static final String MARK_ALL_READ = "MESSAGE_MARK_ALL_READ";
        public static final String SEND = "MESSAGE_SEND";
    }

    
    // ARTIST
    public static class Artist {
        public static final String LIST_VIEW = "ARTIST_LIST_VIEW";
        public static final String PROFILE_VIEW = "ARTIST_PROFILE_VIEW";
        public static final String BLOCK = "ARTIST_BLOCK";
        public static final String REPORT = "ARTIST_REPORT";
        public static final String SHARE = "ARTIST_SHARE";
        public static final String POST_LIST_VIEW = "ARTIST_POST_LIST_VIEW";
        public static final String SERVICE_LIST_VIEW = "ARTIST_SERVICE_LIST_VIEW";
        public static final String SERVICE_DETAIL_VIEW = "ARTIST_SERVICE_DETAIL_VIEW";
        public static final String REVIEW_LIST_VIEW = "ARTIST_REVIEW_LIST_VIEW";
        public static final String REVIEW_CREATE = "ARTIST_REVIEW_CREATE";

        public static final String POST_CREATE = "ARTIST_POST_CREATE";
        public static final String POST_UPDATE = "ARTIST_POST_UPDATE";
        public static final String POST_DELETE = "ARTIST_POST_DELETE";

        public static final String SERVICE_CREATE = "ARTIST_SERVICE_CREATE";
        public static final String SERVICE_UPDATE = "ARTIST_SERVICE_UPDATE";
        public static final String SERVICE_DELETE = "ARTIST_SERVICE_DELETE";

        public static final String PROMOTION_CREATE = "ARTIST_PROMOTION_CREATE";
        public static final String PROMOTION_UPDATE = "ARTIST_PROMOTION_UPDATE";
        public static final String PROMOTION_DELETE = "ARTIST_PROMOTION_DELETE";

        public static final String PROFILE_UPDATE = "ARTIST_PROFILE_UPDATE";
    }

    
    // BOOKING
    public static class Booking {
        public static final String USER_HISTORY_VIEW = "BOOKING_USER_HISTORY_VIEW";
        public static final String USER_CREATE = "BOOKING_USER_CREATE";
        public static final String ARTIST_LIST_VIEW = "BOOKING_ARTIST_LIST_VIEW";
    }

    
    // NOTIFICATION
    public static class Notification {
        public static final String USER_HISTORY_VIEW = "NOTIFICATION_USER_HISTORY_VIEW";
        public static final String BOOKING_ARTIST_VIEW = "NOTIFICATION_BOOKING_ARTIST_VIEW";
    }

    
    // ADMIN
    public static class Admin {
        public static final String REPORTED_POST_LIST_VIEW = "ADMIN_REPORTED_POST_LIST_VIEW";
        public static final String REPORTED_ARTIST_LIST_VIEW = "ADMIN_REPORTED_ARTIST_LIST_VIEW";
        public static final String REPORTED_POST_DELETE = "ADMIN_REPORTED_POST_DELETE";
        public static final String REPORTED_ARTIST_LOCK = "ADMIN_REPORTED_ARTIST_LOCK";
        public static final String ARTIST_REGISTRATION_LIST_VIEW = "ADMIN_ARTIST_REGISTRATION_LIST_VIEW";
        public static final String ARTIST_REGISTRATION_APPROVE = "ADMIN_ARTIST_REGISTRATION_APPROVE";
        public static final String ARTIST_REGISTRATION_REJECT = "ADMIN_ARTIST_REGISTRATION_REJECT";
    }
}
