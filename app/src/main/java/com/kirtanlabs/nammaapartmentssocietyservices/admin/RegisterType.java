package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class RegisterType extends BaseActivity implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register_type;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        ListView listViewRegisterType = findViewById(R.id.listViewRegisterType);

        String[] stringRegisterType = {getString(R.string.plumber),
                getString(R.string.carpenter),
                getString(R.string.electrician),
                getString(R.string.guard),
                getString(R.string.user)
        };

        int[] icons = {R.drawable.plumber, R.drawable.carpenter, R.drawable.electrician, R.drawable.security_guard, R.drawable.user};

        /*Setting the Adapter to list view*/
        RegisterTypeAdapter adapter = new RegisterTypeAdapter(RegisterType.this, stringRegisterType, icons);
        listViewRegisterType.setAdapter(adapter);

        /*Setting onClickListener for view*/
        listViewRegisterType.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding On Item Click Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intentPlumber = new Intent(RegisterType.this, Register.class);
                intentPlumber.putExtra(Constants.SCREEN_TITLE, getString(R.string.plumber));
                startActivity(intentPlumber);
                break;
            case 1:
                Intent intentCarpenter = new Intent(RegisterType.this, Register.class);
                intentCarpenter.putExtra(Constants.SCREEN_TITLE, getString(R.string.carpenter));
                startActivity(intentCarpenter);
                break;
            case 2:
                Intent intentElectrician = new Intent(RegisterType.this, Register.class);
                intentElectrician.putExtra(Constants.SCREEN_TITLE, getString(R.string.electrician));
                startActivity(intentElectrician);
                break;
            case 3:
                Intent intentSecurity = new Intent(RegisterType.this, Register.class);
                intentSecurity.putExtra(Constants.SCREEN_TITLE, getString(R.string.guard));
                startActivity(intentSecurity);
                break;
            case 4:
                Toast.makeText(this, "Yet to implement", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
