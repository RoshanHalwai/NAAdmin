package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.adapters.FoodCollectionsAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;

public class FoodCollectionsActivity extends BaseActivity {

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
        return screenTitle = R.string.food_collections;
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

        /*Since we have History button here, we would want admin to navigate to history and take a look at their
         * History of that food donations*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(v -> startActivity(new Intent(FoodCollectionsActivity.this, FoodCollectionHistory.class)));

        /*Retrieving list of all food collection data*/
        new RetrievingNotificationData(this, "").getFoodCollectionDataList(foodCollectionDataList -> {
            hideProgressIndicator();
            if (!foodCollectionDataList.isEmpty()) {
                /*Setting Adapter to the recycler view*/
                recyclerView.setAdapter(new FoodCollectionsAdapter(FoodCollectionsActivity.this, foodCollectionDataList, screenTitle));
            } else {
                showFeatureUnavailableLayout(R.string.food_collection_unavailable);
            }
        });
    }
}
