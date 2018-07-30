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
    public static final String IS_ADMIN = "isAdmin";
    public static final String SOCIETY_SERVICE_UID = "societyServiceUid";
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

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static final int END_SERVICE_REQUEST_CODE = 2;
    public static final int SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE = 3;

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 120;

    /* ------------------------------------------------------------- *
     * Firebase Keys
     * ------------------------------------------------------------- */

    private static final String FIREBASE_CHILD_ADMIN = "admin";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICE_TYPE = "societyServiceType";
    private static final String FIREBASE_CHILD_SOCIETYSERVICENOTIFICATIONS = "societyServiceNotifications";
    public static final String FIREBASE_CHILD_AVAILABLE = "available";
    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_TOKEN_ID = "tokenId";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICES = "societyServices";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_NOTIFICATIONS = "notifications";
    public static final String FIREBASE_CHILD_TAKEN_BY = "takenBy";

    /* ------------------------------------------------------------- *
     * Firebase Values
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ACCEPTED = "Accepted";
    public static final String FIREBASE_CHILD_REJECTED = "Rejected";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference SOCIETY_SERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_SERVICES);
    public static final DatabaseReference SOCIETY_SERVICES_ADMIN_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ADMIN);
    public static final DatabaseReference ALL_SOCIETY_SERVICES_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SOCIETY_SERVICE_TYPE_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_SOCIETY_SERVICE_TYPE);
    public static final DatabaseReference ALL_SOCIETYSERVICENOTIFICATION_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATIONS).child(FIREBASE_CHILD_ALL);

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
}
