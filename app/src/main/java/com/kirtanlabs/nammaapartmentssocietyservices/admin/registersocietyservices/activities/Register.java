package com.kirtanlabs.nammaapartmentssocietyservices.admin.registersocietyservices.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.SocietyAdminHome;
import com.kirtanlabs.nammaapartmentssocietyservices.login.OTP;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_GUARD_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_AUTH;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ADMIN;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GATE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_GUARD_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_DEFAULT_RATING_VALUE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_TAKE_PICTURE;

public class Register extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editFullName, editMobileNumber, editGateNumber;
    private Button buttonYes, buttonNo;
    private CircleImageView profilePic;
    private TextView textErrorProfilePic;
    private String serviceType, registrationOf, mobileNumber, gateNumber;
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
        TextView textGateNumber = findViewById(R.id.textGateNumber);
        editGateNumber = findViewById(R.id.editGateNumber);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        LinearLayout layoutYesNo = findViewById(R.id.layoutYesNo);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        textFullName.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoBoldFont(this));
        textAdmin.setTypeface(setLatoBoldFont(this));
        textGateNumber.setTypeface(setLatoBoldFont(this));
        editFullName.setTypeface(setLatoRegularFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
        editGateNumber.setTypeface(setLatoRegularFont(this));
        buttonYes.setTypeface(setLatoLightFont(this));
        buttonNo.setTypeface(setLatoLightFont(this));
        buttonRegister.setTypeface(setLatoLightFont(this));

        registrationOf = getIntent().getStringExtra(SOCIETY_SERVICE_TYPE);
        serviceType = registrationOf.toLowerCase();

        if (registrationOf.equals(getString(R.string.guard))) {
            profilePic.setVisibility(View.VISIBLE);
            layoutYesNo.setVisibility(View.VISIBLE);
            textGateNumber.setVisibility(View.VISIBLE);
            editGateNumber.setVisibility(View.VISIBLE);
            textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        } else if (registrationOf.equals(getString(R.string.garbage_management))) {
            serviceType = FIREBASE_CHILD_GARBAGE_COLLECTION;
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
                mobileNumber = editMobileNumber.getText().toString().trim();
                if (validateFields()) {
                    Intent intent = new Intent(Register.this, OTP.class);
                    intent.putExtra(SCREEN_TITLE, R.string.register);
                    intent.putExtra(SOCIETY_SERVICE_MOBILE_NUMBER, mobileNumber);
                    startActivityForResult(intent, SOCIETY_SERVICE_REGISTRATION_REQUEST_CODE);
                }
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
        String societyServiceUID = Objects.requireNonNull(FIREBASE_AUTH.getCurrentUser()).getUid();

        /*Getting the data of the Society Service entered by Admin*/
        String fullName = editFullName.getText().toString();
        String mobileNumber = editMobileNumber.getText().toString();

        /*Storing the Society Service personal details */
        SocietyServiceData societyServiceData = new SocietyServiceData(fullName,
                mobileNumber, societyServiceUID);

        Intent adminHomeIntent = new Intent(Register.this, SocietyAdminHome.class);
        adminHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (registrationOf.equals(getString(R.string.guard))) {
            /*Storing the Security Guard personal details under guard->private->data->securityGuardUID*/
            DatabaseReference securityGuardDetailsReference = PRIVATE_GUARD_REFERENCE
                    .child(FIREBASE_CHILD_DATA)
                    .child(societyServiceUID);

            /*Getting the storage reference*/
            StorageReference storageReference = FIREBASE_STORAGE.getReference(serviceType)
                    .child(FIREBASE_CHILD_PRIVATE)
                    .child(societyServiceUID);

            UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(Register.this, profilePhotoPath));

            /*Adding the profile photo to storage reference and Guard Data to real time database */
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                /*creating the upload object to store uploaded image details and guard data*/
                securityGuardDetailsReference.child(FIREBASE_CHILD_PROFILE_PHOTO).setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                securityGuardDetailsReference.child(FIREBASE_CHILD_STATUS).setValue(getString(R.string.available).toLowerCase());
                securityGuardDetailsReference.child(FIREBASE_CHILD_GATE_NUMBER).setValue(Integer.parseInt(gateNumber));

                /*We want to map Admin Guard with UID to ensure all User Security Notifications are sent only to this Guard
                 * This will ensure we do not iterate over each of the Guard's UID and find the Admin, hence making this
                 * operation time complexity to O(1)*/
                if (isAdmin) {
                    PRIVATE_GUARD_REFERENCE.child(FIREBASE_CHILD_ADMIN).setValue(societyServiceUID);
                }

            }).addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show());

            /*Mapping Security Guard mobile number with security guard UID under guard->all*/
            DatabaseReference securityGuardAllReference = ALL_GUARD_REFERENCE.child(mobileNumber);
            securityGuardAllReference.setValue(societyServiceUID);

            /*Storing the Security Guard personal details under guard->private->data->securityGuardUID*/
            securityGuardDetailsReference.setValue(societyServiceData).addOnSuccessListener(aVoid -> {
                hideProgressDialog();
                showNotificationDialog(getString(R.string.security_guard_added_title),
                        getString(R.string.security_guard_added_message),
                        adminHomeIntent);
            });

        } else {
            /*Getting the reference of 'Data' child under 'societyServices'*/
            DatabaseReference societyServicesReference = SOCIETY_SERVICES_REFERENCE.child(serviceType).child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA);
            DatabaseReference societyServiceDetailsReference = societyServicesReference.child(societyServiceUID);

            /*Mapping societyServiceUID with societyServiceType*/
            DatabaseReference societyTypeReference = SOCIETY_SERVICE_TYPE_REFERENCE.child(societyServiceUID);
            societyTypeReference.child(serviceType).setValue(true);

            /*Mapping Society Service mobile number with Society Service UID under societyServices->all*/
            DatabaseReference societyServicesAllReference = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
            societyServicesAllReference.child(mobileNumber).setValue(societyServiceUID);

            /*Setting Society Service Service Count and Rating to its Default value at the time of registration */
            societyServiceData.setServiceCount(0);
            societyServiceData.setRating(SOCIETY_SERVICE_DEFAULT_RATING_VALUE);

            /*Storing the Society Service personal details under societyServices->societyServiceType->private->unavailable->societyServiceUID*/
            societyServiceDetailsReference.setValue(societyServiceData).addOnSuccessListener(aVoid -> {
                hideProgressDialog();
                showNotificationDialog(getString(R.string.society_service_added_title),
                        getString(R.string.society_service_added_message),
                        adminHomeIntent);
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

    /**
     * This method gets invoked to check all the validation fields of editTexts
     */
    private boolean validateFields() {
        String fullName = editFullName.getText().toString().trim();
        gateNumber = editGateNumber.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editFullName, editMobileNumber, editGateNumber});

        if (profilePhotoPath == null && (registrationOf.equals(getString(R.string.guard)))) {
            textErrorProfilePic.setVisibility(View.VISIBLE);
            textErrorProfilePic.requestFocus();
            return false;
        } else {
            textErrorProfilePic.setVisibility(View.GONE);
        }

        if (!fieldsFilled) {
            if (TextUtils.isEmpty(fullName)) {
                editFullName.setError(getString(R.string.name_validation));
                return false;
            }
            if (TextUtils.isEmpty(mobileNumber) || !isValidPhone(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.mobile_number_validation));
                return false;
            }
            if (TextUtils.isEmpty(gateNumber) && (registrationOf.equals(getString(R.string.guard)))) {
                editGateNumber.setError(getString(R.string.gate_number_validation));
                return false;
            }
        } else {
            if (isPersonNameValid(fullName)) {
                editFullName.setError(getString(R.string.accept_alphabets));
                return false;
            }
            if (!isValidPhone(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.mobile_number_validation));
                return false;
            }
        }
        return true;
    }
}
