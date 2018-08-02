package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;

public class History extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieving all the request details which society service has received till now*/
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData(this, societyServiceUID);
        retrievingNotificationData.getHistoryNotificationDataList(historyNotificationDataList -> {
            if (historyNotificationDataList == null) {
                showFeatureUnavailableLayout(R.string.history_detail_message);
            } else {
                TimeLineAdapter adapter = new TimeLineAdapter(this, historyNotificationDataList);
                recyclerViewHistory.setAdapter(adapter);
            }
        });
    }
}
