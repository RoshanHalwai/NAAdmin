package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.activities;

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
import com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.adapter.HelpDeskAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.pojo.HelpDeskPojo;

import java.util.ArrayList;
import java.util.List;

public class HelpDeskActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<HelpDeskPojo> helpDeskList;
    private HelpDeskAdapter helpDeskAdapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_helpdesk;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.help_desk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id for all the views*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Initialising Array List*/
        helpDeskList = new ArrayList<>();

        /*Initializing the adapter*/
        helpDeskAdapter = new HelpDeskAdapter(helpDeskList, this);
        /* Setting the GridView Adapter*/
        recyclerView.setAdapter(helpDeskAdapter);

        /*Retrieving Help Desk data raised by the user's*/
        retrieveHelpDeskData();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked while retrieving all the support issues raised by user's of the Society
     */
    private void retrieveHelpDeskData() {
        /*Getting 'Support' reference to get the unique UID generated after an issue has been raised*/
        DatabaseReference helpDeskReference = Constants.SUPPORT_REFERENCE;
        helpDeskReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.helpdesk_unavailable);
                } else {
                    hideProgressIndicator();
                    /*Iterating through all issue's raised by the user*/
                    for (DataSnapshot helpDeskDataSnapshot : dataSnapshot.getChildren()) {
                        String helpDeskUID = helpDeskDataSnapshot.getKey();
                        DatabaseReference helpDeskUIDReference = helpDeskReference.child(helpDeskUID);
                        helpDeskUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HelpDeskPojo helpDeskPojo = dataSnapshot.getValue(HelpDeskPojo.class);
                                helpDeskList.add(index++, helpDeskPojo);
                                helpDeskAdapter.notifyDataSetChanged();
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
