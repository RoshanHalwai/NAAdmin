package com.kirtanlabs.nammaapartmentssocietyservices.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.NammaApartmentsPlumberServices;

public class SignIn extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*Since this is Login Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonLogin.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonLogin.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        String mobileNumber = editMobileNumber.getText().toString().trim();
        //TODO: To remove this mobile number from here and check whether entered is mobile number present in firebase or not.
        if (isValidPhone(mobileNumber) && mobileNumber.equals("7895185103")) {
            startActivity(new Intent(SignIn.this, NammaApartmentsPlumberServices.class));
            finish();
        } else {
            editMobileNumber.setError(getString(R.string.mobile_number_validation));
        }
    }
}
