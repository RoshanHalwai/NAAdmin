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

public class StaffActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private StaffAdapter staffAdapter;
    private List<SocietyServiceData> staffDataList;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_staff;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.staff;
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
        staffAdapter = new StaffAdapter(this, staffDataList);

        /*Attaching adapter to the recyclerView */
        recyclerView.setAdapter(staffAdapter);

        showProgressIndicator();

        /*To Retrieve Staff Detail from firebase*/
        retrieveStaffDataFromFirebase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to get Details of Admin's Staff from (societyServices->serviceType->SocietyServiceUID) in firebase.
     */
    private void retrieveStaffDataFromFirebase() {
        DatabaseReference staffUIDsReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE;
        /*Getting Staff UID here from (societyServices->All->UID) in firebase*/
        staffUIDsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot staffUIDSnapshot : dataSnapshot.getChildren()) {
                        String staffUID = staffUIDSnapshot.getValue(String.class);
                        new RetrievingNotificationData(StaffActivity.this, staffUID)
                                .getSocietyServiceData(societyServiceData -> {
                                    hideProgressIndicator();
                                    staffDataList.add(societyServiceData);
                                    staffAdapter.notifyDataSetChanged();
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