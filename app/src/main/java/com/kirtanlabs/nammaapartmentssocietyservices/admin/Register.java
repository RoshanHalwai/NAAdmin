package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.endservice.OTP;


import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE;

public class Register extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*We want to display menu icon in Title bar, so that user can perform various actions from list*/
        showMenuIcon();

        /*Getting Id's for all the views*/
        TextView textSocietyServiceType = findViewById(R.id.textSocietyServiceType);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        Spinner spinnerSocietyServiceType = findViewById(R.id.spinnerSocietyServiceType);
        EditText editFullName = findViewById(R.id.editFullName);
        EditText editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        /*Setting font for all the views*/
        textSocietyServiceType.setTypeface(Constants.setLatoBoldFont(this));
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textFullName.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editFullName.setTypeface(Constants.setLatoRegularFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonRegister.setTypeface(Constants.setLatoLightFont(this));

        ServiceTypeAdapter serviceTypeAdapter = new ServiceTypeAdapter(Register.this, getResources().getStringArray(R.array.service_list));

        /*Setting adapter to Spinner view*/
        spinnerSocietyServiceType.setAdapter(serviceTypeAdapter);

        /*Setting onClickListener for view*/
        buttonRegister.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListeners Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Register.this, OTP.class);
        intent.putExtra(Constants.SCREEN_TITLE, R.string.register);
        startActivityForResult(intent, SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: We have to store Society Service Details in firebase.
    }
}
