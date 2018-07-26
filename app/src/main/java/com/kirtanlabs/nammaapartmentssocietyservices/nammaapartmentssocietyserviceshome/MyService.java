package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class MyService extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private LinearLayout layoutBaseActivity;
    private TabLayout tabLayout;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_service;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Namma Apartments Society Services Home Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        Switch switchAvailability = findViewById(R.id.switchAvailability);
        layoutBaseActivity = findViewById(R.id.layoutBaseActivity);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        switchAvailability.setVisibility(View.VISIBLE);

        /*When user logs In its default status will be Available */
        switchAvailability.setChecked(true);
        layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
        tabLayout.setBackgroundResource(R.color.nmGreen);

        /*Storing society service token_id in firebase so that user can send notification*/
        String token_id = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", token_id);
        DatabaseReference securityGuardReference = Constants.SOCIETY_SERVICE_TOKEN_REFERENCE;
        securityGuardReference.setValue(token_id);

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
            layoutBaseActivity.setBackgroundResource(R.color.nmGreen);
            tabLayout.setBackgroundResource(R.color.nmGreen);
        } else {
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
