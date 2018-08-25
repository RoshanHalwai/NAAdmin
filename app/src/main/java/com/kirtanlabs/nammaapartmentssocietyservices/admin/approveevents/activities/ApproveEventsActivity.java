package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters.ApproveEventAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApproveEventsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<SocietyServiceNotification> eventsDataList;
    private ApproveEventAdapter approveEventAdapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_approve_events;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.approve_events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listOfEvents);

        /*Setting attributes for the recycler view*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventsDataList = new ArrayList<>();

        showProgressIndicator();

        /*Creating the Adapter*/
        approveEventAdapter = new ApproveEventAdapter(this, eventsDataList);

        /*Attaching adapter to the recyclerView */
        recyclerView.setAdapter(approveEventAdapter);

        /*Retrieving All Events Request Details from firebase*/
        retrieveApproveEventsData();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to Retrieve All Events Requests made by the User from Firebase
     */
    private void retrieveApproveEventsData() {
        DatabaseReference notificationUIDReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
        /*Retrieving All Society Service Notification UID from (societyServiceNotifications->all->notificationUID) in firebase*/
        notificationUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot notificationUIdDataSnapshot : dataSnapshot.getChildren()) {
                        String notificationUID = notificationUIdDataSnapshot.getKey();

                        DatabaseReference eventDetailsReference = notificationUIDReference
                                .child(notificationUID);
                        /*Getting All Event Management Notification Data and Putting Then in a list.*/
                        eventDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String serviceType = dataSnapshot.child(Constants.SOCIETY_SERVICE_TYPE).getValue(String.class);
                                String status = dataSnapshot.child(Constants.FIREBASE_CHILD_STATUS).getValue(String.class);
                                if (Objects.requireNonNull(serviceType).equals(Constants.FIREBASE_CHILD_EVENT_MANAGEMENT)
                                        && Objects.requireNonNull(status).equals(Constants.IN_PROGRESS)) {
                                    hideProgressIndicator();
                                    String eventDate = dataSnapshot.child(Constants.FIREBASE_CHILD_EVENT_DATE).getValue(String.class);
                                    String eventTitle = dataSnapshot.child(Constants.FIREBASE_CHILD_EVENT_TITLE).getValue(String.class);

                                    SocietyServiceNotification societyServiceNotification = dataSnapshot.getValue(SocietyServiceNotification.class);
                                    Objects.requireNonNull(societyServiceNotification).setEventDate(eventDate);
                                    societyServiceNotification.setEventTitle(eventTitle);

                                    eventsDataList.add(index++, societyServiceNotification);
                                    approveEventAdapter.notifyDataSetChanged();
                                }
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
    }
}
