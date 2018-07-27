package com.kirtanlabs.nammaapartmentssocietyservices.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.Future;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.History;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.Serving;
import com.kirtanlabs.nammaapartmentssocietyservices.login.SignIn;

public class NammaApartmentsPlumberServices extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private LinearLayout layoutBaseActivity;
    private TabLayout tabLayout;
    private TextView textActivityTitle;
    private ImageView imageMenu;

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

        /*Getting Id's for all the views*/
        ViewPager mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        textActivityTitle = findViewById(R.id.textActivityTitle);
        Switch switchAvailability = findViewById(R.id.switchAvailability);
        layoutBaseActivity = findViewById(R.id.layoutBaseActivity);
        imageMenu = findViewById(R.id.imageMenu);

        /*Setting font for all the views*/
        textActivityTitle.setTypeface(Constants.setLatoRegularFont(this));

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        switchAvailability.setVisibility(View.VISIBLE);
        imageMenu.setVisibility(View.VISIBLE);

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
        imageMenu.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding onClick, OnCheckedChanged & onMenuItemClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(NammaApartmentsPlumberServices.this, imageMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(NammaApartmentsPlumberServices.this, SignIn.class));
            finish();
            return true;
        });
        popupMenu.show();
    }

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
