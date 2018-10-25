package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.adapters.FoodCollectionsAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;

public class FoodCollectionsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_food_donations;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.food_collections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id for all the views*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showProgressIndicator();

        /*Retrieving list of all food collection data*/
        new RetrievingNotificationData(this, "").getFoodCollectionDataList(foodCollectionDataList -> {
            hideProgressIndicator();
            if (!foodCollectionDataList.isEmpty()) {
                /*Setting Adapter to the recycler view*/
                recyclerView.setAdapter(new FoodCollectionsAdapter(FoodCollectionsActivity.this, foodCollectionDataList));
            } else {
                showFeatureUnavailableLayout(R.string.food_collection_unavailable);
            }
        });
    }
}
