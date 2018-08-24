package com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.ApprovedUsersFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.RemovedUsersFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.UnapprovedUsersFragment;

public class ManageUsers extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_manage_users;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.manage_users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager manageUsersViewPager = findViewById(R.id.containerManageUsers);
        TabLayout tabsManageUsers = findViewById(R.id.tabsManageUsers);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        manageUsersViewPager.setAdapter(mSectionsPagerAdapter);

        manageUsersViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabsManageUsers));
        tabsManageUsers.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(manageUsersViewPager));
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
                    return new ApprovedUsersFragment();
                case 1:
                    return new UnapprovedUsersFragment();
                case 2:
                    return new RemovedUsersFragment();
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