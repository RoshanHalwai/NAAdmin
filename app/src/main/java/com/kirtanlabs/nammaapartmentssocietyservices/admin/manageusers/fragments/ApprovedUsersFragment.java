package com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.ManageUsers;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.adapter.ManageUsersAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApprovedUsersFragment extends Fragment {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private BaseActivity baseActivity;
    private List<NAUser> approvedUsersList;
    private ManageUsersAdapter approvedUsersAdapter;

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_approved_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewApprovedUsers = view.findViewById(R.id.recyclerViewApprovedUsers);
        recyclerViewApprovedUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Setting Tag for Approved Users Fragment*/
        String approvedUsersFragmentTag = getTag();
        ((ManageUsers) Objects.requireNonNull(getActivity())).setApprovedUsersFragmentTag(approvedUsersFragmentTag);

        baseActivity = (BaseActivity) getActivity();
        Objects.requireNonNull(baseActivity).showProgressIndicator();
        approvedUsersList = new ArrayList<>();

        /*Setting Adapter to Recycler view*/
        approvedUsersAdapter = new ManageUsersAdapter(getActivity(), R.string.approved_users, approvedUsersList);
        recyclerViewApprovedUsers.setAdapter(approvedUsersAdapter);

        /*Retrieving a List of Approved Users*/
        retrieveApprovedUserDetails();
    }

    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve the list of Approved Users from Firebase
     */
    public void retrieveApprovedUserDetails() {
        new RetrievingNotificationData(getActivity(), "")
                .getApprovedUserDataList(approvedUsersList -> {
                    if (!approvedUsersList.isEmpty()) {
                        this.approvedUsersList.clear();
                        this.approvedUsersList.addAll(approvedUsersList);
                        approvedUsersAdapter.notifyDataSetChanged();
                        baseActivity.hideProgressIndicator();
                    }
                });
    }
}
