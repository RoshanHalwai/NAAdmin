package com.kirtanlabs.nammaapartmentssocietyservices.admin.registersocietyservices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.registersocietyservices.adapter.RegistrationCategoriesAdapter;

public class RegistrationCategories extends BaseActivity implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_registration_categories;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listViewRegisterType = findViewById(R.id.listViewRegisterType);

        String[] stringRegisterCategories = {getString(R.string.plumber),
                getString(R.string.carpenter),
                getString(R.string.electrician),
                getString(R.string.garbage_management),
                getString(R.string.guard)
        };

        int[] icons = {R.drawable.plumber, R.drawable.carpenter, R.drawable.electrician, R.drawable.garbage_bag, R.drawable.security_guard};

        /*Setting the Adapter to list view*/
        RegistrationCategoriesAdapter adapter = new RegistrationCategoriesAdapter(RegistrationCategories.this, stringRegisterCategories, icons);
        listViewRegisterType.setAdapter(adapter);

        /*Setting onClickListener for view*/
        listViewRegisterType.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding On Item Click Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RegistrationCategories.this, Register.class);
        switch (position) {
            case 0:
                intent.putExtra(Constants.REGISTRATION_OF, getString(R.string.plumber));
                break;
            case 1:
                intent.putExtra(Constants.REGISTRATION_OF, getString(R.string.carpenter));
                break;
            case 2:
                intent.putExtra(Constants.REGISTRATION_OF, getString(R.string.electrician));
                break;
            case 3:
                intent.putExtra(Constants.REGISTRATION_OF, getString(R.string.garbage_management));
                break;
            case 4:
                intent.putExtra(Constants.REGISTRATION_OF, getString(R.string.guard));
                break;
        }
        startActivity(intent);
    }

}
