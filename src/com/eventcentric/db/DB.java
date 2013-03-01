package com.eventcentric.db;

public interface DB {
    public static final String DATABASE = "shizzlr.db";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS ";
    public static final String ALTER_TABLE_QUERY = "ALTER TABLE ";
    public static final String ADD_COLUMN = " ADD COLUMN ";
    public static final String START_TABLE = " (", SEPARATE_COLUMN = ", ", END_TABLE = ");";
    public static final String TYPE_TEXT = " TEXT";
    public static final String TYPE_ID = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_INT = " INTEGER";
    public static final String TYPE_BLOB = " BLOB";
    public static final String COLUMN_ID = "_id";

    public static final String TABLE_SETTINGS = "settings";
    public static final String COLUMN_SETTINGS_KEY = "key";
    public static final String COLUMN_SETTINGS_VALUE = "value";


    public static final String FACEBOOK_IS_SINGLE_SIGNON = "FacebookIsIngleSignOn";
    public static final String SHIZLLER_USER_ID = "ShizzlerUserId";
    public static final String SHIZLLER_USERNAME = "ShizzlerUsername";
    public static final String SHIZLLER_PASSWORD = "ShizzlerPassword";
    public static final String FACEBOOK_USER_ID = "FacebookUserId";
    public static final String FACEBOOK_USER_ID_SignIN = "FacebookUserIdSignIn";
    public static final String FACEBOOK_TOKEN = "FacebookToken";
    public static final String FACEBOOK_EXPIRE_DATE = "FacebookExpireDate";
    public static final String FACEBOOK_USERNAME = "FacebookUserName";
    public static final String LAST_TOWN_VIEWED = "LastTownViewed";
    public static final String HOMETOWN = "HomeTown";
    public static final String SCHOOL = "School";
    public static final String PHONE_NUMBER = "PhoneNumber";
    public static final String ENABLE_PUSH_NOTIFICATIONS = "PushNotifications";
    public static final String MY_FULL_NAME = "FullName";
    public static final String Phone_Activated = "PhoneActivated";
    public static final String Pass_Code = "PassCode";

    public static final String FoursquareToken = "FoursquareToken";
    public static final String TwitterToken_token = "TwitterToken_token";
    public static final String TwitterToken_secret = "TwitterToken_secret";
    public static final String TwitterToken_userID = "TwitterToken_userID";
    public static final String TwitterToken_username = "TwitterToken_username";


    public static final String TABLE_FRIENDS = "friends";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PROFILE_PIC = "profile_pic";
    public static final String COLUMN_FACEBOOK_ID = "facebook_id";
    public static final String COLUMN_SHIZZLR_ID = "shizzlr_id";

    public static final String TABLE_FB_FRIENDS = "fb_friends";
    public static final String COLUMN_FULLNAME_FB = "username";
    
    public static final String TABLE_TOWNS = "towns";
    public static final String COLUMN_TOWNNAME = "townname";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
}
