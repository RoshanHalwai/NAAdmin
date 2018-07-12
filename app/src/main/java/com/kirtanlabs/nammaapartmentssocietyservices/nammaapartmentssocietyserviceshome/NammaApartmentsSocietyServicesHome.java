package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class NammaApartmentsSocietyServicesHome extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_namma_apartments_society_services_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.society_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*Since this is Namma Apartments Society Services Home Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textAwaitingResponse = findViewById(R.id.textAwaitingResponse);
        TextView textName = findViewById(R.id.textName);
        TextView textSociety = findViewById(R.id.textSociety);
        TextView textApartment = findViewById(R.id.textApartment);
        TextView textFlatNumber = findViewById(R.id.textFlatNumber);
        TextView textProblemDescription = findViewById(R.id.textProblemDescription);
        TextView textNameValue = findViewById(R.id.textNameValue);
        TextView textSocietyValue = findViewById(R.id.textSocietyValue);
        TextView textApartmentValue = findViewById(R.id.textApartmentValue);
        TextView textFlatNumberValue = findViewById(R.id.textFlatNumberValue);
        TextView textProblemDescriptionValue = findViewById(R.id.textProblemDescriptionValue);
        Button buttonMakeCall = findViewById(R.id.buttonMakeCall);
        Button buttonEndService = findViewById(R.id.buttonEndService);

        /*Setting font for all the views*/
        textAwaitingResponse.setTypeface(Constants.setLatoRegularFont(this));
        textName.setTypeface(Constants.setLatoRegularFont(this));
        textSociety.setTypeface(Constants.setLatoRegularFont(this));
        textApartment.setTypeface(Constants.setLatoRegularFont(this));
        textFlatNumber.setTypeface(Constants.setLatoRegularFont(this));
        textProblemDescription.setTypeface(Constants.setLatoRegularFont(this));
        textNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyValue.setTypeface(Constants.setLatoBoldFont(this));
        textApartmentValue.setTypeface(Constants.setLatoBoldFont(this));
        textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(this));
        textProblemDescriptionValue.setTypeface(Constants.setLatoBoldFont(this));
        buttonMakeCall.setTypeface(Constants.setLatoLightFont(this));
        buttonEndService.setTypeface(Constants.setLatoLightFont(this));

        // TODO: To uncomment this later, this layout will be visible in starting when particular society service has not accepted any request from user.
        /* layoutAwaitingResponse.setVisibility(View.VISIBLE);
         *//*We need Progress Indicator in this screen*//*
        showProgressIndicator();*/

        /*Setting onClickListener for view*/
        buttonEndService.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        startActivity(new Intent(NammaApartmentsSocietyServicesHome.this, EndService.class));
    }
}
