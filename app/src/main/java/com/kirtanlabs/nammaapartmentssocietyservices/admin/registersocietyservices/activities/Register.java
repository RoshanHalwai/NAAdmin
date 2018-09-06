package com.kirtanlabs.nammaapartmentssocietyservices.admin.registersocietyservices.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.login.OTP;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ADMIN;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_TAKE_PICTURE;

public class Register extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editFullName, editMobileNumber;
    private Button buttonYes, buttonNo;
    private CircleImageView profilePic;
    private String serviceType, registrationOf;
    private File profilePhotoPath;
    private boolean isAdmin = false;

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

        /*Getting Id's for all the views*/
        profilePic = findViewById(R.id.profilePic);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textAdmin = findViewById(R.id.textAdmin);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        LinearLayout layoutYesNo = findViewById(R.id.layoutYesNo);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textFullName.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        textAdmin.setTypeface(Constants.setLatoBoldFont(this));
        editFullName.setTypeface(Constants.setLatoRegularFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonYes.setTypeface(Constants.setLatoLightFont(this));
        buttonNo.setTypeface(Constants.setLatoLightFont(this));
        buttonRegister.setTypeface(Constants.setLatoLightFont(this));

        registrationOf = getIntent().getStringExtra(Constants.SOCIETY_SERVICE_TYPE);
        serviceType = registrationOf.toLowerCase();

        if (registrationOf.equals(getString(R.string.guard))) {
            profilePic.setVisibility(View.VISIBLE);
            layoutYesNo.setVisibility(View.VISIBLE);
        } else if (registrationOf.equals(getString(R.string.garbage_management))) {
            serviceType = Constants.FIREBASE_CHILD_GARBAGE_MANAGEMENT;
        }

        /*Setting Listeners for views*/
        buttonRegister.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        profilePic.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnItemSelected Listeners Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                String mobileNumber = editMobileNumber.getText().toString().trim();
                Intent intent = new Intent(Register.this, OTP.class);
                intent.putExtra(Constants.SCREEN_TITLE, R.string.register);
                intent.putExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER, mobileNumber);
                startActivityForResult(intent, SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE);
                break;
            case R.id.buttonYes:
                isAdmin = true;
                buttonYes.setBackgroundResource(R.drawable.button_selected);
                buttonNo.setBackgroundResource(R.drawable.button_not_selected);
                buttonYes.setTextColor(Color.WHITE);
                buttonNo.setTextColor(Color.BLACK);
                break;
            case R.id.buttonNo:
                isAdmin = false;
                buttonYes.setBackgroundResource(R.drawable.button_not_selected);
                buttonNo.setBackgroundResource(R.drawable.button_selected);
                buttonYes.setTextColor(Color.BLACK);
                buttonNo.setTextColor(Color.WHITE);
                break;
            case R.id.profilePic:
                launchCamera();
                break;
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    storeSocietyServiceData();
                }
                break;

            case REQ_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                        @Override
                        public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                            Bitmap bitmapProfilePic = getBitmapFromFile(Register.this, imageFile);
                            profilePic.setImageBitmap(bitmapProfilePic);
                            profilePhotoPath = imageFile;
                        }
                    });
                }
                break;
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

        /*Generating the societyServiceUID */
        String societyServiceUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        /*Getting the data of the Society Service entered by Admin*/
        String fullName = editFullName.getText().toString();
        String mobileNumber = editMobileNumber.getText().toString();

        /*Storing the Society Service personal details */
        SocietyServiceData societyServiceData = new SocietyServiceData(fullName,
                mobileNumber, societyServiceUID);

        if (registrationOf.equals(getString(R.string.guard))) {
            /*Storing the Security Guard personal details under guard->private->data->securityGuardUID*/
            DatabaseReference securityGuardDetailsReference = Constants.PRIVATE_GUARD_REFERENCE
                    .child(Constants.FIREBASE_CHILD_DATA)
                    .child(societyServiceUID);

            /*Getting the storage reference*/
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(serviceType)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(societyServiceUID);

            UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(Register.this, profilePhotoPath));

            /*Adding the profile photo to storage reference and Guard Data to real time database */
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                /*creating the upload object to store uploaded image details and guard data*/
                securityGuardDetailsReference.child(Constants.FIREBASE_CHILD_PROFILE_PHOTO).setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                securityGuardDetailsReference.child(Constants.FIREBASE_CHILD_STATUS).setValue(getString(R.string.available).toLowerCase());

                /*We want to map Admin Guard with UID to ensure all User Security Notifications are sent only to this Guard
                 * This will ensure we do not iterate over each of the Guard's UID and find the Admin, hence making this
                 * operation time complexity to O(1)*/
                if (isAdmin) {
                    Constants.PRIVATE_GUARD_REFERENCE.child(FIREBASE_CHILD_ADMIN).setValue(societyServiceUID);
                }

            }).addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show());

            /*Mapping Security Guard mobile number with security guard UID under guard->all*/
            DatabaseReference securityGuardAllReference = Constants.ALL_GUARD_REFERENCE.child(mobileNumber);
            securityGuardAllReference.setValue(societyServiceUID);

            /*Storing the Security Guard personal details under guard->private->data->securityGuardUID*/
            securityGuardDetailsReference.setValue(societyServiceData).addOnSuccessListener(aVoid -> {
                hideProgressDialog();
                showNotificationDialog(getString(R.string.security_guard_added_title),
                        getString(R.string.security_guard_added_message),
                        null);
            });

        } else {
            /*Getting the reference of 'Data' child under 'societyServices'*/
            DatabaseReference societyServicesReference = SOCIETY_SERVICES_REFERENCE.child(serviceType).child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA);
            DatabaseReference societyServiceDetailsReference = societyServicesReference.child(societyServiceUID);

            /*Mapping societyServiceUID with societyServiceType*/
            DatabaseReference societyTypeReference = Constants.SOCIETY_SERVICE_TYPE_REFERENCE.child(societyServiceUID);
            societyTypeReference.child(serviceType).setValue(true);

            /*Mapping Society Service mobile number with Society Service UID under societyServices->all*/
            DatabaseReference societyServicesAllReference = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
            societyServicesAllReference.child(mobileNumber).setValue(societyServiceUID);

            /*Setting Society Service Service Count and Rating to its Default value at the time of registration */
            societyServiceData.setServiceCount(0);
            societyServiceData.setRating(0);

            /*Storing the Society Service personal details under societyServices->societyServiceType->private->unavailable->societyServiceUID*/
            societyServiceDetailsReference.setValue(societyServiceData).addOnSuccessListener(aVoid -> {
                hideProgressDialog();
                showNotificationDialog(getString(R.string.society_service_added_title),
                        getString(R.string.society_service_added_message),
                        null);
            });
        }
    }

    /**
     * This method gets invoked when the Admin presses the profilePic image to capture a photo
     */
    private void launchCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            EasyImage.openCamera(Register.this, 0);
        }
    }
}
