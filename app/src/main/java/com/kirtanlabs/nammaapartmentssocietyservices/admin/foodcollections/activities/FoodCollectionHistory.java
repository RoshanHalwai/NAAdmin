package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.adapters.FoodCollectionsAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;

import java.util.Collections;

public class FoodCollectionHistory extends BaseActivity {

    /* ------------------------------------------------------------- *
     *Private Members
     * ------------------------------------------------------------- */

    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_food_donations;
    }

    @Override
    protected int getActivityTitle() {
        return screenTitle = R.string.history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id for the recyclerView*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        /*Setting attributes for the recycler view*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Retrieving list of all food collection data whose status is "Completed"*/
        new RetrievingNotificationData(this, "").getFoodDonationHistoryList(foodCollectionDataList -> {
            hideProgressIndicator();
            if (!foodCollectionDataList.isEmpty()) {
                Collections.reverse(foodCollectionDataList);
                /*Setting Adapter to the recycler view*/
                recyclerView.setAdapter(new FoodCollectionsAdapter(FoodCollectionHistory.this, foodCollectionDataList, screenTitle));
            } else {
                showFeatureUnavailableLayout(R.string.food_collection_unavailable);
            }

        });

    }
}
