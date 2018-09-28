package com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.activities;

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
import com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.adapters.StaffAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private StaffAdapter staffAdapter;
    private List<SocietyServiceData> staffDataList;
    private String staffType;
    private int screenTitle;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_staff;
    }

    @Override
    protected int getActivityTitle() {
        staffType = getIntent().getStringExtra(Constants.SOCIETY_SERVICE_TYPE);
        switch (staffType) {
            case Constants.PLUMBER:
                screenTitle = R.string.plumbers;
                break;
            case Constants.CARPENTER:
                screenTitle = R.string.carpenters;
                break;
            case Constants.ELECTRICIAN:
                screenTitle = R.string.electricians;
                break;
            case Constants.GARBAGE_COLLECTOR:
                screenTitle = R.string.garbageCollectors;
                break;
            case Constants.GUARD:
                screenTitle = R.string.guards;
                break;
        }
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listSocietyStaff);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        staffDataList = new ArrayList<>();

        /*Creating the Adapter*/
        staffAdapter = new StaffAdapter(this, staffDataList, screenTitle);

        /*Attaching adapter to the recyclerView */
        recyclerView.setAdapter(staffAdapter);

        showProgressIndicator();

        if (screenTitle == R.string.guards) {
            /*To Retrieve Security Guards Details from firebase*/
            retrieveSecurityGuardsDataFromFirebase();
        } else {
            /*To Retrieve Staff Details from firebase*/
            retrieveStaffDataFromFirebase();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to get Details of Admin's Staff from (societyServices->serviceType->SocietyServiceUID) in firebase.
     */
    private void retrieveStaffDataFromFirebase() {
        String societyServiceType = staffType.toLowerCase();
        if (screenTitle == R.string.garbageCollectors) {
            societyServiceType = Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
        }

        DatabaseReference staffUIDsReference = Constants.SOCIETY_SERVICES_REFERENCE
                .child(societyServiceType)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_DATA);
        /*Getting Staff UID here from (societyServices->Society Service Type->Private->Data->UID) in firebase*/
        staffUIDsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot staffUIDSnapshot : dataSnapshot.getChildren()) {
                        count++;
                        String staffUID = staffUIDSnapshot.getKey();
                        new RetrievingNotificationData(StaffActivity.this, staffUID)
                                .getSocietyServiceData(societyServiceData -> {
                                    hideProgressIndicator();
                                    staffDataList.add(societyServiceData);
                                    if (count == dataSnapshot.getChildrenCount()) {
                                        staffAdapter.notifyDataSetChanged();
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

    /**
     * This method is invoked to get Security Guard Details from (guards->private->data->guardUID) in firebase.
     */
    private void retrieveSecurityGuardsDataFromFirebase() {
        DatabaseReference guardsDetailsReference = Constants.GUARDS_DATA_REFERENCE;
        /*Retrieving Guards UID from (guards->private->data)*/
        guardsDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot guardsUIDSnapshot) {
                if (guardsUIDSnapshot.exists()) {
                    hideProgressIndicator();
                    for (DataSnapshot dataSnapshot : guardsUIDSnapshot.getChildren()) {
                        String guardUID = dataSnapshot.getKey();
                        /*Getting Security Guard Data and Put then in a List*/
                        guardsDetailsReference.child(guardUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                count++;
                                SocietyServiceData societyServiceData = dataSnapshot.getValue(SocietyServiceData.class);
                                Objects.requireNonNull(societyServiceData).setSocietyServiceType(Constants.FIREBASE_CHILD_GUARDS);
                                staffDataList.add(societyServiceData);
                                if (count == guardsUIDSnapshot.getChildrenCount()) {
                                    staffAdapter.notifyDataSetChanged();
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
