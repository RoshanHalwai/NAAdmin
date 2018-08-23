package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import static com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications.MyFirebaseInstanceIdService.getRefreshedToken;

public class SocietyAdminHome extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_admin_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.society_admin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Check user is already loggedIn or not*/
        checkSharedPreference();

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*We want to display menu icon in Title bar, so that user can perform various actions from list*/
        showMenuIcon();

        /*Getting Id's for all the views*/
        GridView gridSocietyAdminHome = findViewById(R.id.gridSocietyAdminHome);

        /*Setting the adapter to grid view*/
        gridSocietyAdminHome.setAdapter(getAdapter());

        /*Storing admin token_id in firebase so that user can send notification for event management*/
        storeTokenID();
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to check whether admin is already LoggedIn or not.
     * If not then create Shared preference and store admin's credentials in shared preference.
     */
    private void checkSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_SOCIETY_SERVICES_PREFERENCE, MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences.getBoolean(Constants.LOGGED_IN, false);

        /*If not LoggedIn than store Admin's Credentials in Shared Preference */
        if (!isLoggedIn) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.LOGGED_IN, true);
            editor.putString(Constants.LOGIN_TYPE, Constants.FIREBASE_CHILD_ADMIN);
            editor.apply();
        }
    }

    /**
     * This method is invoked to get Adapter to Grid View
     *
     * @return adapter of grid view
     */
    private SocietyAdminHomeAdapter getAdapter() {

        String[] stringSocietyAdminServices = getResources().getStringArray(R.array.society_admin_services);

        int[] icons = {R.drawable.manage_user,
                R.drawable.register_society_service,
                R.drawable.staff,
                R.drawable.event,
                R.drawable.notice_board,
                R.drawable.help_desk
        };

        return new SocietyAdminHomeAdapter(this, stringSocietyAdminServices, icons);
    }

    /**
     * Stores Admin token ID to server so that User can request for event management in form of Notifications
     */
    private void storeTokenID() {
        /*Getting the token Id reference of Admin*/
        DatabaseReference adminTokenIdReference = Constants.SOCIETY_SERVICES_ADMIN_REFERENCE
                .child(Constants.FIREBASE_CHILD_TOKEN_ID);

        /*Setting the token Id in admin->tokenId*/
        adminTokenIdReference.setValue(getRefreshedToken());
    }

}
