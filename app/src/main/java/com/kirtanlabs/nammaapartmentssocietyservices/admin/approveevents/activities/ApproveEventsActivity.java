package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters.ApproveEventAdapter;

public class ApproveEventsActivity extends BaseActivity {

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

        /*Creating the Adapter*/
        ApproveEventAdapter approveEventAdapter = new ApproveEventAdapter(this);

        /*Attaching adapter to the recyclerView */
        recyclerView.setAdapter(approveEventAdapter);
    }
}
