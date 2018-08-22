package com.kirtanlabs.nammaapartmentssocietyservices;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

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
    public static final String REGISTRATION_OF = "registrationOf";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int EDIT_TEXT_EMPTY_LENGTH = 0;
    static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final String COUNTRY_CODE_IN = "+91";

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static final int END_SERVICE_REQUEST_CODE = 2;
    public static final int SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;

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
    public static final String FIREBASE_CHILD_AVAILABLE = "available";
    public static final String FIREBASE_CHILD_UNAVAILABLE = "unavailable";
    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_TOKEN_ID = "tokenId";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICES = "societyServices";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    public static final String FIREBASE_CHILD_GARBAGE_MANAGEMENT = "garbageManagement";
    public static final String FIREBASE_CHILD_NOTIFICATIONS = "notifications";
    public static final String FIREBASE_CHILD_TAKEN_BY = "takenBy";
    private static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_SERVICE_COUNT = "serviceCount";
    public static final String FIREBASE_CHILD_SERVING = "serving";
    public static final String FIREBASE_CHILD_FUTURE = "future";
    public static final String FIREBASE_CHILD_HISTORY = "history";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_EVENT_MANAGEMENT = "eventManagement";

    /* ------------------------------------------------------------- *
     * Firebase Values
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ACCEPTED = "Accepted";
    public static final String FIREBASE_CHILD_REJECTED = "Rejected";
    public static final String FIREBASE_CHILD_COMPLETED = "Completed";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference SOCIETY_SERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_SERVICES);
    private static final DatabaseReference USERS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    public static final DatabaseReference SOCIETY_SERVICES_ADMIN_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ADMIN);
    public static final DatabaseReference ALL_SOCIETY_SERVICES_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SOCIETY_SERVICE_TYPE_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_SOCIETY_SERVICE_TYPE);
    public static final DatabaseReference ALL_SOCIETYSERVICENOTIFICATION_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATIONS).child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USERS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);

    /* ------------------------------------------------------------- *
     * Remote Message Keys
     * ------------------------------------------------------------- */

    public static final String REMOTE_NOTIFICATION_UID = "notificationUID";
    public static final String REMOTE_MOBILE_NUMBER = "mobileNumber";
    public static final String REMOTE_SOCIETY_SERVICE_TYPE = "societyServiceType";

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

}
