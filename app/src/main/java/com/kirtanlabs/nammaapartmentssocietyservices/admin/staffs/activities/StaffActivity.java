package com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.adapters.StaffAdapter;

public class StaffActivity extends BaseActivity {

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

        /*Creating the Adapter*/
        StaffAdapter staffAdapter = new StaffAdapter(this);

        /*Attaching adapter to the recyclerView */
        recyclerView.setAdapter(staffAdapter);
    }
}
