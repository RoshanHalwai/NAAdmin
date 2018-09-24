package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters.ApproveEventAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.EVENT_MANAGEMENT_NOTIFICATION_REFERENCE;

public class ApproveEventsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<SocietyServiceNotification> eventsDataList;
    private ApproveEventAdapter approveEventAdapter;
    private int index = 0;
    private int childrenCount = 0, notificationResponsePendingCount = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_approve_events;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.booked_events;
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
     * This method is used to Retrieve All Events Requests made by the User from Firebase.
     */
    private void retrieveApproveEventsData() {
        /*Retrieving All Event Management Notification UID from (societyServiceNotifications->eventManagement->notificationUID) in firebase*/
        EVENT_MANAGEMENT_NOTIFICATION_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot notificationUIDs) {
                if (notificationUIDs.exists()) {
                    hideProgressIndicator();
                    for (DataSnapshot notificationUIdDataSnapshot : notificationUIDs.getChildren()) {
                        childrenCount++;
                        String notificationUID = notificationUIdDataSnapshot.getKey();
                        /*Checking if Admin has Responded to that Event Request or Not*/
                        if (Objects.requireNonNull(notificationUIdDataSnapshot.getValue(Boolean.class))) {
                            notificationResponsePendingCount++;
                            DatabaseReference eventDetailsReference = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                                    .child(notificationUID);
                            /*Getting Event Management Notification Data and Putting Them in a list.*/
                            eventDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    SocietyServiceNotification societyServiceNotification = dataSnapshot.getValue(SocietyServiceNotification.class);
                                    eventsDataList.add(index++, societyServiceNotification);

                                    if (childrenCount == notificationUIDs.getChildrenCount()) {
                                        approveEventAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    if (notificationResponsePendingCount == 0) {
                        showFeatureUnavailableLayout(R.string.unapproved_events_unavailable);
                    }
                } else {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.unapproved_events_unavailable);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
