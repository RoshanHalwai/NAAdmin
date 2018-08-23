package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities.AddNoticeActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.registersocietyservices.activities.RegistrationCategories;

import static com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications.MyFirebaseInstanceIdService.getRefreshedToken;

public class SocietyAdminHome extends BaseActivity implements AdapterView.OnItemClickListener {

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

        /*Setting onItemClickListener for view*/
        gridSocietyAdminHome.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding On Item Click Method
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                startActivity(new Intent(SocietyAdminHome.this, RegistrationCategories.class));
                break;
            case 4:
                startActivity(new Intent(SocietyAdminHome.this, AddNoticeActivity.class));
                break;
            default:
                Toast.makeText(this, "Yet to Implement", Toast.LENGTH_SHORT).show();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to set Adapter to Grid View
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
