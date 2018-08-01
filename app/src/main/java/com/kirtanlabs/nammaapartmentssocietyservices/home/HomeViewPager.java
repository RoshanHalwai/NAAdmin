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
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.FutureFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.HistoryFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.ServingFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SERVICE_COUNT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications.MyFirebaseInstanceIdService.getRefreshedToken;
import static com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications.MyFirebaseInstanceIdService.isTokenRefreshed;

public class HomeViewPager extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private LinearLayout layoutBaseActivity;
    private TabLayout tabLayout;
    private TextView textActivityTitle;
    private String societyServiceUid;
    private String societyServiceMobileNumber;

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

        if (isTokenRefreshed) {
            /*Storing society service token_id in firebase so that user can send notification*/
            storeTokenID();
        }

        /*Setting event for views*/
        switchAvailability.setOnCheckedChangeListener(this);
    }

    /**
     * Returns the Society Service Type
     *
     * @param serviceTypeCallback callback to return Society Service Type
     */
    public void getServiceType(ServingFragment.ServiceTypeCallback serviceTypeCallback) {
        DatabaseReference societyServiceTypeReference = SOCIETY_SERVICE_TYPE_REFERENCE.
                child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        societyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceTypeCallback.onCallBack(dataSnapshot.getChildren().iterator().next().getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storeTokenID() {
        getServiceType(serviceType -> {
            DatabaseReference societyServiceDataRef = SOCIETY_SERVICES_REFERENCE
                    .child(serviceType)
                    .child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            societyServiceDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SocietyServiceData societyServiceData = dataSnapshot.getValue(SocietyServiceData.class);
                    societyServiceMobileNumber = Objects.requireNonNull(societyServiceData).getMobileNumber();
                    societyServiceUid = societyServiceData.getUid();

                    /*Getting the token Id reference*/
                    DatabaseReference tokenIdReference = Constants.SOCIETY_SERVICES_REFERENCE
                            .child(serviceType)
                            .child(Constants.FIREBASE_CHILD_PRIVATE);

                    /*Setting the token Id in data->societyServiceUID*/
                    tokenIdReference.child(FIREBASE_CHILD_DATA).child(societyServiceUid).child(Constants.FIREBASE_CHILD_TOKEN_ID).setValue(getRefreshedToken());

                    /*Adding Token Id and Service Count under Available Society Service Mobile Number*/
                    tokenIdReference.child(Constants.FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber).child(Constants.FIREBASE_CHILD_TOKEN_ID).setValue(getRefreshedToken());
                    tokenIdReference.child(Constants.FIREBASE_CHILD_AVAILABLE).child(societyServiceMobileNumber).child(FIREBASE_CHILD_SERVICE_COUNT).setValue(0);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
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
                    return new ServingFragment();
                case 1:
                    return new FutureFragment();
                case 2:
                    return new HistoryFragment();
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
