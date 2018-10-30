package com.kirtanlabs.nammaapartmentssocietyservices.admin.scrapcollections.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.scrapcollections.adapters.ScrapCollectionsAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;

public class ScrapCollectionsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     *Private Members
     * ------------------------------------------------------------- */

    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_scrap_collections;
    }

    @Override
    protected int getActivityTitle() {
        return screenTitle = R.string.scrap_collections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id for the recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        /*Setting Attributes for the Recycler View */
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*We need progress indicator in this screen*/
        showProgressIndicator();

        /*Since we have History button here, we would want admin to navigate to history and take a look at their
         * History of that food donations*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(v -> startActivity(new Intent(ScrapCollectionsActivity.this, ScrapCollectionHistory.class)));

        /*Retrieving list of all scrap collection data raised by user*/
        new RetrievingNotificationData(this, "").getScrapCollectionDataList(scrapCollectionDataList -> {
            hideProgressIndicator();
            if (!scrapCollectionDataList.isEmpty()) {
                /*Setting Adapter to the recycler view*/
                recyclerView.setAdapter(new ScrapCollectionsAdapter(ScrapCollectionsActivity.this, scrapCollectionDataList, screenTitle));
            } else {
                showFeatureUnavailableLayout(R.string.scrap_collection_unavailable);
            }
        });
    }
}
