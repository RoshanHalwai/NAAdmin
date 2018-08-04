package com.kirtanlabs.nammaapartmentssocietyservices.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.FutureFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.ServingFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_AVAILABLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SERVICE_COUNT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_TOKEN_ID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_UNAVAILABLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;
import static com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications.MyFirebaseInstanceIdService.getRefreshedToken;

public class HomeViewPager extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private LinearLayout layoutBaseActivity;
    private TabLayout tabLayout;
    private TextView textActivityTitle;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_namma_apartments_plumber_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.available;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Namma Apartments Society Services Home Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*We want to display menu icon in Title bar, so that user can perform various actions from list*/
        showMenuIcon();

        /*Getting Id's for all the views*/
        ViewPager mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        textActivityTitle = findViewById(R.id.textActivityTitle);
        Switch switchAvailability = findViewById(R.id.switchAvailability);
        layoutBaseActivity = findViewById(R.id.layoutBaseActivity);

        /*Setting font for all the views*/
        textActivityTitle.setTypeface(Constants.setLatoRegularFont(this));

        /*Store Society Service data to SocietyServiceGlobal class to access through out the application*/
        String societyServiceUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        new RetrievingNotificationData(this, societyServiceUID)
                .getSocietyServiceData(societyServiceData -> {
                    ((SocietyServiceGlobal) getApplicationContext()).setSocietyServiceData(societyServiceData);

                    /*Storing society service token_id in firebase so that user can send notification*/
                    storeTokenID();

                    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

                });

        switchAvailability.setVisibility(View.VISIBLE);

        /*When user logs In its default status will be Available */
        switchAvailability.setChecked(true);
        layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
        tabLayout.setBackgroundResource(R.color.nmGreen);

        /*Setting event for views*/
        switchAvailability.setOnCheckedChangeListener(this);
    }

    /**
     * Stores token ID to server so User can request for services in form of Notifications
     */
    private void storeTokenID() {
        SocietyServiceData societyServiceData = ((SocietyServiceGlobal) getApplicationContext()).getSocietyServiceData();
        String serviceType = societyServiceData.getSocietyServiceType();
        String societyServiceMobileNumber = societyServiceData.getMobileNumber();

        /*Getting the token Id reference*/
        DatabaseReference tokenIdReference = Constants.SOCIETY_SERVICES_REFERENCE
                .child(serviceType)
                .child(Constants.FIREBASE_CHILD_PRIVATE);

        /*Setting the token Id in data->societyServiceUID*/
        tokenIdReference.child(FIREBASE_CHILD_DATA).child(societyServiceUID).child(FIREBASE_CHILD_TOKEN_ID).setValue(getRefreshedToken());

        /*Adding Token Id and Service Count under Available Society Service Mobile Number*/
        tokenIdReference.child(Constants.FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber).child(FIREBASE_CHILD_TOKEN_ID).setValue(getRefreshedToken());
        tokenIdReference.child(Constants.FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber).child(FIREBASE_CHILD_SERVICE_COUNT).setValue(0);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnCheckedChanged Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SocietyServiceData societyServiceData = ((SocietyServiceGlobal) getApplicationContext()).getSocietyServiceData();
        String serviceType = societyServiceData.getSocietyServiceType();
        String societyServiceMobileNumber = societyServiceData.getMobileNumber();
        if (isChecked) {
            textActivityTitle.setText(R.string.available);
            layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
            tabLayout.setBackgroundResource(R.color.nmGreen);

            /*Getting the reference of Society Service Mobile Number and placing the data back in 'available' in Firebase,
             * once the Society Service is available. At the same time, the corresponding mobile number and its data is removed from 'unavailable'*/
            DatabaseReference insertInAvailableReference = SOCIETY_SERVICES_REFERENCE.child(serviceType).child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_UNAVAILABLE).child(societyServiceMobileNumber);
            insertInAvailableReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SOCIETY_SERVICES_REFERENCE.child(serviceType)
                            .child(FIREBASE_CHILD_PRIVATE)
                            .child(FIREBASE_CHILD_AVAILABLE)
                            .child(societyServiceMobileNumber)
                            .setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> insertInAvailableReference.removeValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            textActivityTitle.setText(R.string.unavailable);
            layoutBaseActivity.setBackgroundResource(R.color.nmRed);
            tabLayout.setBackgroundResource(R.color.nmRed);

            /*Getting the reference of Society Service Mobile Number and placing the data in 'unavailable' in Firebase,
             * once the Society Service is unavailable. At the same time, the corresponding mobile number and its data is removed from 'available'*/
            DatabaseReference removeFromAvailableReference = SOCIETY_SERVICES_REFERENCE.child(serviceType).child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber);
            removeFromAvailableReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SOCIETY_SERVICES_REFERENCE.child(serviceType)
                            .child(FIREBASE_CHILD_PRIVATE)
                            .child(FIREBASE_CHILD_UNAVAILABLE)
                            .child(societyServiceMobileNumber)
                            .setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> removeFromAvailableReference.removeValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /* ------------------------------------------------------------- *
     * SectionPagerAdapter Class
     * ------------------------------------------------------------- */

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /* ------------------------------------------------------------- *
         * Overriding FragmentPagerAdapter Objects
         * ------------------------------------------------------------- */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ServingFragment();
                case 1:
                    return new FutureFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
