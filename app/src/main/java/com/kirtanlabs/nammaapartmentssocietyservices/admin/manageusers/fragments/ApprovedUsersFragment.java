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

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.adapter.ManageUsersAdapter;

public class ApprovedUsersFragment extends Fragment {

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

        /*Setting Adapter to Recycler view*/
        //TODO: To UnComment this when we retrieve approved user details.
        /*ManageUsersAdapter approvedUsersAdapter = new ManageUsersAdapter(getActivity(),R.string.approved_users);
        recyclerViewApprovedUsers.setAdapter(approvedUsersAdapter);*/
    }
}
