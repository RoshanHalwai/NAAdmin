package com.kirtanlabs.nammaapartmentssocietyservices.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.SocietyAdminHome;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;

import java.util.Objects;

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

        /*Here we check If User has Logged In or Not*/
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_SOCIETY_SERVICES_PREFERENCE, MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences.getBoolean(Constants.LOGGED_IN, false);
        String loginType = sharedPreferences.getString(Constants.LOGIN_TYPE, null);
        if (isLoggedIn) {
            switch (Objects.requireNonNull(loginType)) {
                case Constants.FIREBASE_CHILD_SOCIETY_SERVICES:
                    startActivity(new Intent(SignIn.this, HomeViewPager.class));
                    break;
                case Constants.FIREBASE_CHILD_ADMIN:
                    startActivity(new Intent(SignIn.this, SocietyAdminHome.class));
                    break;
            }
            finish();
        }

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
        if (isValidPhone(mobileNumber)) {
            //We send mobile number to OTP class for Validation
            Intent intent = new Intent(SignIn.this, OTP.class);
            intent.putExtra(Constants.SCREEN_TITLE, R.string.login);
            intent.putExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER, mobileNumber);
            startActivity(intent);
            finish();
        } else {
            editMobileNumber.setError(getString(R.string.mobile_number_validation));
        }
    }

}
