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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.Future;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.History;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.Serving;

public class NammaApartmentsPlumberServices extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

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

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        switchAvailability.setVisibility(View.VISIBLE);

        /*When user logs In its default status will be Available */
        switchAvailability.setChecked(true);
        layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
        tabLayout.setBackgroundResource(R.color.nmGreen);

        /*Storing society service token_id in firebase so that user can send notification*/
        String tokenId = FirebaseInstanceId.getInstance().getToken();
        String societyServiceUid = getIntent().getStringExtra(Constants.SOCIETY_SERVICE_UID);
        String societyServiceMobileNumber = getIntent().getStringExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER);

        /*Getting the reference till societyService UID*/
        DatabaseReference societyServiceUIDReference = Constants.SOCIETY_SERVICE_TYPE_REFERENCE.child(societyServiceUid);
        societyServiceUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot societyServiceTypeSnapshot : dataSnapshot.getChildren()) {
                    /*Getting the societyServiceType*/
                    String societyServiceType = societyServiceTypeSnapshot.getKey();

                    /*Getting the token Id reference*/
                    DatabaseReference tokenIdReference = Constants.SOCIETY_SERVICES_REFERENCE
                            .child(societyServiceType)
                            .child(Constants.FIREBASE_CHILD_PRIVATE);

                    /*Setting the token Id in data->societyServiceUID*/
                    tokenIdReference.child(Constants.FIREBASE_CHILD_DATA).child(societyServiceUid).child(Constants.FIREBASE_CHILD_TOKEN_ID).setValue(tokenId);

                    /*Mapping Society Service mobile number with token Id in 'available'*/
                    tokenIdReference.child(Constants.FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber).setValue(tokenId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Setting event for views*/
        switchAvailability.setOnCheckedChangeListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnCheckedChanged Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //TODO: To update the status of society service in firebase also
        if (isChecked) {
            textActivityTitle.setText(R.string.available);
            layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
            tabLayout.setBackgroundResource(R.color.nmGreen);
        } else {
            textActivityTitle.setText(R.string.unavailable);
            layoutBaseActivity.setBackgroundResource(R.color.nmRed);
            tabLayout.setBackgroundResource(R.color.nmRed);
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
                    return new Serving();
                case 1:
                    return new Future();
                case 2:
                    return new History();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
