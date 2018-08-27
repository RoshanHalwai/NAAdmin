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
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.UnapprovedUsersFragment;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.DEFAULT_MANAGE_USERS_TAB_POSITION;

public class ManageUsers extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String approvedUsersFragmentTag;

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

        /*Setting Unapproved Users Tab as default Tab*/
        manageUsersViewPager.setCurrentItem(DEFAULT_MANAGE_USERS_TAB_POSITION);
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to give the tag of Approved Users Fragment
     *
     * @return - Approved Users fragment tag
     */
    public String getApprovedUsersFragmentTag() {
        return approvedUsersFragmentTag;
    }

    /**
     * This method is used to set Tag for Approved Users Fragment
     *
     * @param approvedUsersFragmentTag - tag of Approved Users fragment
     */
    public void setApprovedUsersFragmentTag(String approvedUsersFragmentTag) {
        this.approvedUsersFragmentTag = approvedUsersFragmentTag;
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