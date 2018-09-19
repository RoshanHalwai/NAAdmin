package com.kirtanlabs.nammaapartmentssocietyservices;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Constants {

    /* ------------------------------------------------------------- *
     * Environment
     * ------------------------------------------------------------- */

    static final String BETA_ENV = "beta_env";
    static final String DEV_ENV = "dev_env";

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String MESSAGE = "message";
    public static final String SCREEN_TITLE = "screenTitle";
    public static final String END_OTP = "endOTP";
    public static final String SOCIETY_SERVICE_MOBILE_NUMBER = "societyServiceMobileNumber";
    public static final String NOTIFICATION_UID = "notificationUID";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String SOCIETY_SERVICE_TYPE = "societyServiceType";
    public static final String NOTIFICATION_ID = "notificationID";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int EDIT_TEXT_EMPTY_LENGTH = 0;
    static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final String COUNTRY_CODE_IN = "+91";
    public static final int DEFAULT_MANAGE_USERS_TAB_POSITION = 1;
    public static final String MORNING = "8AM - 12PM";
    public static final String NOON = "12PM - 4PM";
    public static final String EVENING = "4PM - 8PM";
    public static final String NIGHT = "8PM - 12PM";

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static final int END_SERVICE_REQUEST_CODE = 2;
    public static final int SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int NEW_USER_OR_NEW_EVENT_REQUEST_CODE = 5;
    static final int SEND_SMS_PERMISSION_REQUEST_CODE = 6;
    static final int ENABLE_LOCATION_PERMISSION_CODE = 7;

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 120;

    /* ------------------------------------------------------------- *
     * Notification
     * ------------------------------------------------------------- */

    public static final String NOTIFICATION_EXPAND_MSG = "Slide down on note to respond";
    public static final String NOTIFICATION_EXPAND_TITLE = "Namma Apartments";

    /* ------------------------------------------------------------- *
     * Firebase Keys
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ADMIN = "admin";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICE_TYPE = "societyServiceType";
    private static final String FIREBASE_CHILD_SOCIETYSERVICENOTIFICATIONS = "societyServiceNotifications";
    private static final String FIREBASE_CHILD_SUPPORT = "support";
    public static final String FIREBASE_CHILD_AVAILABLE = "available";
    public static final String FIREBASE_CHILD_UNAVAILABLE = "unavailable";
    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_TOKEN_ID = "tokenId";
    public static final String FIREBASE_CHILD_SOCIETY_SERVICES = "societyServices";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    public static final String FIREBASE_CHILD_PERSONAL_DETAILS = "personalDetails";
    public static final String FIREBASE_CHILD_PHONE_NUMBER = "phoneNumber";
    public static final String FIREBASE_CHILD_GARBAGE_COLLECTION = "garbageCollection";
    public static final String FIREBASE_CHILD_NOTIFICATIONS = "notifications";
    public static final String FIREBASE_CHILD_TAKEN_BY = "takenBy";
    private static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_SERVING = "serving";
    public static final String FIREBASE_CHILD_FUTURE = "future";
    public static final String FIREBASE_CHILD_HISTORY = "history";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_EVENT_MANAGEMENT = "eventManagement";
    public static final String FIREBASE_CHILD_PLUMBER = "plumber";
    public static final String FIREBASE_CHILD_CARPENTER = "carpenter";
    public static final String FIREBASE_CHILD_ELECTRICIAN = "electrician";
    public static final String FIREBASE_CHILD_GUARDS = "guards";
    public static final String FIREBASE_CHILD_PRIVILEGES = "privileges";
    public static final String FIREBASE_CHILD_VERIFIED = "verified";
    public static final String FIREBASE_CHILD_NOTICE_BOARD = "noticeBoard";
    public static final int FIREBASE_CHILD_VERIFIED_PENDING = 0;
    public static final int FIREBASE_CHILD_VERIFIED_APPROVED = 1;
    public static final int FIREBASE_CHILD_VERIFIED_DECLINED = 2;
    private static final String FIREBASE_CHILD_USER_DATA = "userData";
    public static final String FIREBASE_CHILD_OTHER_DETAILS = "otherDetails";
    public static final String FIREBASE_CHILD_TIMESTAMP = "timestamp";
    public static final String FIREBASE_CHILD_RATING = "rating";
    public static final String FIREBASE_CHILD_EMERGENCY = "emergency";
    static final String FIREBASE_CHILD_LATITUDE = "latitude";
    static final String FIREBASE_CHILD_LONGITUDE = "longitude";
    public static final String FIREBASE_CHILD_MAINTENANCE_COST = "maintenanceCost";
    public static final String FIREBASE_CHILD_FULLNAME = "fullName";

    /* ------------------------------------------------------------- *
     * Firebase Values
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ACCEPTED = "Accepted";
    public static final String FIREBASE_CHILD_REJECTED = "Rejected";
    public static final String FIREBASE_CHILD_COMPLETED = "Completed";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseApp FIREBASE_APP = FirebaseApp.getInstance(DEV_ENV);
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(FIREBASE_APP);
    public static final FirebaseStorage FIREBASE_STORAGE = FirebaseStorage.getInstance(FIREBASE_APP);
    public static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance(FIREBASE_APP);
    public static final DatabaseReference SOCIETY_SERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_SERVICES);
    private static final DatabaseReference USERS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    private static final DatabaseReference SOCIETY_SERVICE_NOTIFICATION_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATIONS);
    public static final DatabaseReference SOCIETY_SERVICES_ADMIN_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ADMIN);
    public static final DatabaseReference ALL_SOCIETY_SERVICES_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SOCIETY_SERVICE_TYPE_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_SOCIETY_SERVICE_TYPE);
    public static final DatabaseReference ALL_SOCIETYSERVICENOTIFICATION_REFERENCE = SOCIETY_SERVICE_NOTIFICATION_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USERS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USERS_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference NOTICE_BOARD_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_NOTICE_BOARD);
    public static final DatabaseReference EVENT_MANAGEMENT_NOTIFICATION_REFERENCE = SOCIETY_SERVICE_NOTIFICATION_REFERENCE.child(FIREBASE_CHILD_EVENT_MANAGEMENT);
    public static final DatabaseReference EVENT_MANAGEMENT_TIME_SLOT_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_EVENT_MANAGEMENT);
    public static final DatabaseReference PRIVATE_USER_DATA_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USER_DATA).child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference GUARD_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_GUARDS);
    public static final DatabaseReference ALL_GUARD_REFERENCE = GUARD_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_GUARD_REFERENCE = GUARD_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference GUARDS_DATA_REFERENCE = PRIVATE_GUARD_REFERENCE.child(FIREBASE_CHILD_DATA);
    public static final DatabaseReference SUPPORT_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SUPPORT);

    /* ------------------------------------------------------------- *
     * Remote Message Keys And Values
     * ------------------------------------------------------------- */

    public static final String REMOTE_NOTIFICATION_UID = "notificationUID";
    public static final String REMOTE_MOBILE_NUMBER = "mobileNumber";
    public static final String REMOTE_SOCIETY_SERVICE_TYPE = "societyServiceType";
    public static final String REMOTE_USER_ACCOUNT_NOTIFICATION = "userAccountNotification";
    public static final String REMOTE_CANCELLED_SERVICE_REQUEST = "cancelledServiceRequest";
    public static final String REMOTE_USER_DONATE_FOOD_NOTIFICATION = "userDonateFoodNotification";

    /* ------------------------------------------------------------- *
     * Receiver Action Keys
     * ------------------------------------------------------------- */

    public static final String ACCEPT_BUTTON_CLICKED = "accept_button_clicked";
    public static final String REJECT_BUTTON_CLICKED = "reject_button_clicked";

    /* ------------------------------------------------------------- *
     * Shared Preference Keys
     * ------------------------------------------------------------- */

    public static final String NAMMA_APARTMENTS_SOCIETY_SERVICES_PREFERENCE = "nammaApartmentsSocietyServicesPreference";
    public static final String LOGGED_IN = "loggedIn";
    public static final String SOCIETY_SERVICE_UID = "societyServiceUid";
    public static final String LOGIN_TYPE = "loginType";
    public static final String FIRST_TIME = "firstTime";

    /* ------------------------------------------------------------- *
     * Society Service Type
     * ------------------------------------------------------------- */

    public static final String PLUMBER = "Plumber";
    public static final String CARPENTER = "Carpenter";
    public static final String ELECTRICIAN = "Electrician";
    public static final String GARBAGE_COLLECTOR = "Garbage Collector";
    public static final String GUARD = "Guard";

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoLightFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

    static Typeface setLatoItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Italic.ttf");
    }

    public static Typeface setLatoBoldItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BoldItalic.ttf");
    }

}
