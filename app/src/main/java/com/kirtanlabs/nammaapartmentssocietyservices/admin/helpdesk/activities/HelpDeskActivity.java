package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
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

        /*Initialising Array List*/
        helpDeskList = new ArrayList<>();

        /*Initializing the adapter*/
        helpDeskAdapter = new HelpDeskAdapter(helpDeskList, this);
        /* Setting the GridView Adapter*/
        recyclerView.setAdapter(helpDeskAdapter);
    }
}
