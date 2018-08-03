package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.login.OTP;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE;

public class Register extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editFullName, editMobileNumber;
    private Spinner spinnerSocietyServiceType;
    private String serviceType;

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
        spinnerSocietyServiceType = findViewById(R.id.spinnerSocietyServiceType);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        /*Setting font for all the views*/
        textSocietyServiceType.setTypeface(Constants.setLatoBoldFont(this));
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textFullName.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editFullName.setTypeface(Constants.setLatoRegularFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonRegister.setTypeface(Constants.setLatoLightFont(this));

        /*Setting font for all the items in the list*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.service_list)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textServiceType = view.findViewById(android.R.id.text1);
                textServiceType.setTypeface(Constants.setLatoRegularFont(Register.this));
                return view;
            }
        };

        /*Setting adapter to Spinner view*/
        spinnerSocietyServiceType.setAdapter(adapter);

        /*Setting onClickListener for view*/
        buttonRegister.setOnClickListener(this);
        spinnerSocietyServiceType.setOnItemSelectedListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnItemSelected Listeners Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        String mobileNumber = editMobileNumber.getText().toString().trim();
        Intent intent = new Intent(Register.this, OTP.class);
        intent.putExtra(Constants.SCREEN_TITLE, R.string.register);
        intent.putExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER, mobileNumber);
        startActivityForResult(intent, SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        serviceType = spinnerSocietyServiceType.getItemAtPosition(position).toString().toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE) {
            storeSocietyServiceData();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when the 'Admin' registers a Society Service
     */
    private void storeSocietyServiceData() {
        //displaying progress dialog while image is uploading
        showProgressDialog(this,
                getString(R.string.adding_society_service_data),
                getString(R.string.please_wait_a_moment));

        /*Getting the reference of 'Data' child under 'societyServices'*/
        DatabaseReference societyServicesReference = SOCIETY_SERVICES_REFERENCE.child(serviceType).child(FIREBASE_CHILD_PRIVATE)
                .child(FIREBASE_CHILD_DATA);

        /*Generating the societyServiceUID and creating a reference for it*/
        String societyServiceUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference societyServiceDetailsReference = societyServicesReference.child(societyServiceUID);

        /*Getting the data of the Society Service entered by Admin*/
        String fullName = editFullName.getText().toString();
        String mobileNumber = editMobileNumber.getText().toString();

        /*Mapping Society Service mobile number with Society Service UID under societyServices->all*/
        DatabaseReference societyServicesAllReference = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
        societyServicesAllReference.child(mobileNumber).setValue(societyServiceUID);

        /*Mapping UID with societyServiceType*/
        DatabaseReference societyTypeReference = Constants.SOCIETY_SERVICE_TYPE_REFERENCE.child(societyServiceUID);
        societyTypeReference.child(serviceType).setValue(true);

        /*Storing the Society Service personal details under societyServices->societyServiceType->data->private->societyServiceUID*/
        SocietyServiceData societyServiceData = new SocietyServiceData(fullName,
                mobileNumber, societyServiceUID);
        societyServiceDetailsReference.setValue(societyServiceData).addOnSuccessListener(aVoid -> {
            hideProgressDialog();
            showNotificationDialog(getString(R.string.society_service_added_title),
                    getString(R.string.society_service_added_message),
                    null);
        });
    }

}
