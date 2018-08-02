package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FUTURE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;

public class FutureFragment extends Fragment {

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    private SocietyServiceNotification societyServiceNotification;
    private List<SocietyServiceNotification> futureRequestDetailsList;
    private HomeAdapter adapter;

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_future, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*//*
        RecyclerView recyclerViewFuture = view.findViewById(R.id.recyclerViewFuture);
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getActivity()));

        futureRequestDetailsList = new ArrayList<>();

        adapter = new HomeAdapter(getActivity(), futureRequestDetailsList, R.string.future);

        //Setting adapter to recycler view
        recyclerViewFuture.setAdapter(adapter);

        *//*Retrieving all the request details to which society service is going to serve in future*//*
        getFutureRequestDetails();*/
    }

    /**
     * This method is invoked to retrieve all Future request from firebase that society service is going to serve after current request.
     */
    private void getFutureRequestDetails() {
        new RetrievingNotificationData().getServiceType(serviceType -> {

            /*Retrieving future notification uid from (Society Services->serviceType->Private->data->Society service Uid->notifications->future) in firebase.*/
            DatabaseReference notificationsReference = SOCIETY_SERVICES_REFERENCE.child(serviceType)
                    .child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child(FIREBASE_CHILD_NOTIFICATIONS)
                    .child(FIREBASE_CHILD_FUTURE);
            notificationsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot futureUidDataSnapshot : dataSnapshot.getChildren()) {
                            String notificationUid = futureUidDataSnapshot.getKey();

                            /*Getting notification details and adding the in a list.*/
                            DatabaseReference notificationDataReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                                    .child(notificationUid);
                            notificationDataReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    societyServiceNotification = dataSnapshot.getValue(SocietyServiceNotification.class);
                                    futureRequestDetailsList.add(societyServiceNotification);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }
}
