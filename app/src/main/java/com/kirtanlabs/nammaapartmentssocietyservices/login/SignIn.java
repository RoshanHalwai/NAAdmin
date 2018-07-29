package com.kirtanlabs.nammaapartmentssocietyservices.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.endservice.OTP;

import java.util.Objects;

public class SignIn extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
    private String societyServiceUid;
    private String mobileNumber;

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
        mobileNumber = editMobileNumber.getText().toString().trim();
        if (isValidPhone(mobileNumber)) {
            checkMobileNumberInFirebase();
        } else {
            editMobileNumber.setError(getString(R.string.mobile_number_validation));
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Checking if user's mobile number exists in Firebase or not
     */
    private void checkMobileNumberInFirebase() {
        DatabaseReference societyServiceAdminReference = Constants.SOCIETY_SERVICES_ADMIN_REFERENCE;

        /* Here we are checking if mobile number entered of Admin or Society Service */
        societyServiceAdminReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String adminMobileNumber = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                if (mobileNumber.equals(adminMobileNumber)) {
                    openOtpScreen(true);
                } else {
                    DatabaseReference societyServicesReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child(mobileNumber);
                    societyServicesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                societyServiceUid = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                openOtpScreen(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Navigating to OTP class
     *
     * @param isAdmin Verifies if the mobile number entered by Admin exists or not in Database
     */
    private void openOtpScreen(boolean isAdmin) {
        Intent intent = new Intent(SignIn.this, OTP.class);
        intent.putExtra(Constants.SCREEN_TITLE, R.string.login);
        intent.putExtra(Constants.IS_ADMIN, isAdmin);
        if (!isAdmin) {
            intent.putExtra(Constants.SOCIETY_SERVICE_UID, societyServiceUid);
            intent.putExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER, mobileNumber);
        }
        startActivity(intent);
        finish();
    }
}
