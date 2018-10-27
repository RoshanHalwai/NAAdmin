package com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import java.io.File;
import java.util.Objects;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_CARPENTER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ELECTRICIAN;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GATE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PLUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.GATE_NUMBER_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.GUARD;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.GUARDS_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NAME_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PROFILE_PIC_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartmentssocietyservices.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_TAKE_PICTURE;

public class EditStaffActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editStaffName, editGuardGateNumber;
    private de.hdodenhof.circleimageview.CircleImageView guardProfilePic;
    private String nameOldContent, newContent, societyServiceUID, societyServiceType;
    private int screenTitle, gateNumberOldContent;
    private File profilePhotoPath;
    private AlertDialog imageSelectionDialog;

    /* ------------------------------------------------------------- *
     * Overriding base class methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_edit_staff;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.edit_staff;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Appropriate Screen Title from previous Activity*/
        screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();


        /*Getting id's for all the views*/
        editStaffName = findViewById(R.id.editStaffName);
        editGuardGateNumber = findViewById(R.id.editGuardGateNumber);
        guardProfilePic = findViewById(R.id.guardProfilePic);
        RelativeLayout layoutGuardProfilePic = findViewById(R.id.layoutGuardProfilePic);
        Button buttonDone = findViewById(R.id.buttonDone);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        /*Setting fonts for all the views*/
        editStaffName.setTypeface(setLatoRegularFont(this));
        editGuardGateNumber.setTypeface(setLatoRegularFont(this));
        buttonDone.setTypeface(setLatoLightFont(this));
        buttonCancel.setTypeface(setLatoLightFont(this));

        /*Getting Staff Name From Previous Activity*/
        nameOldContent = getIntent().getStringExtra(NAME_OLD_CONTENT);

        /*Getting Society Service UID from previous activity*/
        societyServiceUID = getIntent().getStringExtra(SOCIETY_SERVICE_UID);

        /*Based on the screen title getting from previous activity we are assigning appropriate society type*/
        switch (screenTitle) {
            case R.string.plumbers:
                societyServiceType = FIREBASE_CHILD_PLUMBER;
                break;
            case R.string.carpenters:
                societyServiceType = FIREBASE_CHILD_CARPENTER;
                break;
            case R.string.electricians:
                societyServiceType = FIREBASE_CHILD_ELECTRICIAN;
                break;
            case R.string.garbageCollectors:
                societyServiceType = FIREBASE_CHILD_GARBAGE_COLLECTION;
                break;
            case R.string.guards:
                societyServiceType = GUARD.toLowerCase();
                break;
        }

        /*Differentiating UI according to the screen title*/
        if (screenTitle == R.string.guards) {
            /*This indicator will be shown in guard profile pic until it gets the pic from previous activity*/
            showProgressIndicator();

            /*Showing appropriate views for edit guard screen*/
            layoutGuardProfilePic.setVisibility(View.VISIBLE);
            editGuardGateNumber.setVisibility(View.VISIBLE);

            /*This method retrieves the guard data coming from previous screen*/
            retrieveGuardOldData();
        } else {
            editStaffName.setText(nameOldContent);
        }

        /*Setting on click listeners to the views*/
        guardProfilePic.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

    }

    /* ------------------------------------------------------------- *
     * Overriding onClick from View Interface
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guardProfilePic:
                hideKeyboard();
                imageSelectionDialog.show();
                break;
            case R.id.buttonDone:
                if (screenTitle == R.string.guards) {
                    /*This method updates guard details in firebase*/
                    updateGuardDetails();
                } else {
                    if (isFieldChanged()) {
                        if (newContent.isEmpty()) {
                            editStaffName.setError(getString(R.string.name_validation));
                        } else {
                            /*This method updates staff name in firebase*/
                            updateStaffName();
                        }
                    } else {
                        super.onBackPressed();
                    }
                }
                break;
            case R.id.buttonCancel:
                super.onBackPressed();
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
            case REQ_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                        @Override
                        public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                            Bitmap bitmapProfilePic = getBitmapFromFile(EditStaffActivity.this, imageFile);
                            guardProfilePic.setImageBitmap(bitmapProfilePic);
                            profilePhotoPath = imageFile;
                            updateProfilePicInFirebase();
                        }
                    });
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method retrieves the old guard data coming from previous screen
     */
    private void retrieveGuardOldData() {
        String profilePicOldContent = getIntent().getStringExtra(PROFILE_PIC_OLD_CONTENT);
        gateNumberOldContent = getIntent().getIntExtra(GATE_NUMBER_OLD_CONTENT, 0);
        Glide.with(this.getApplicationContext()).load(profilePicOldContent).into(guardProfilePic);
        editStaffName.setText(nameOldContent);
        editGuardGateNumber.setText(String.valueOf(gateNumberOldContent));
    }

    /**
     * Creates a custom dialog with a list view which contains Gallery option. This
     * imageSelectionDialog is displayed when user clicks on profile image which is on top of the screen.
     */
    private void createImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectionOptions = {
                getString(R.string.camera),
                getString(R.string.cancel)
        };
        builder.setItems(selectionOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    launchCamera();
                    break;
                case 1:
                    imageSelectionDialog.cancel();
            }
        });
        imageSelectionDialog = builder.create();
    }

    /**
     * This method compares both old content and new content of the staff name.
     *
     * @return value which user updates accordingly.
     */
    private boolean isFieldChanged() {
        newContent = editStaffName.getText().toString();
        return !newContent.equals(nameOldContent);
    }

    /**
     * This method validates and also updates the guard details entered by society admin.
     */
    private void updateGuardDetails() {
        String updatedGuardName = editStaffName.getText().toString();
        String updatedGuardGateNumber = editGuardGateNumber.getText().toString();
        boolean profilePicChanged = profilePhotoPath != null;

        /*Boolean condition to check if guard name and guard gate number fields are filled or not.*/
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editStaffName, editGuardGateNumber});

        /*This condition checks if all fields are not filled and if user presses on done button
         *it will then display proper error messages.*/
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(updatedGuardName)) {
                editStaffName.setError(getString(R.string.name_validation));
            }
            if (TextUtils.isEmpty(updatedGuardGateNumber)) {
                editGuardGateNumber.setError(getString(R.string.enter_gate_number));
            }
        } else {
            /*Navigating Admin to Staff Details Screen*/
            Intent staffDetailsIntent = new Intent(EditStaffActivity.this, StaffActivity.class);
            staffDetailsIntent.putExtra(SOCIETY_SERVICE_TYPE, getString(R.string.guard));
            staffDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            /*Checking if the old content and new content of guard are same or not*/
            if (updatedGuardName.equals(nameOldContent) &&
                    updatedGuardGateNumber.equals(String.valueOf(gateNumberOldContent)) &&
                    !profilePicChanged) {
                super.onBackPressed();
            } else {

                /*Getting Guard Data Reference*/
                DatabaseReference guardsDataReference = GUARDS_DATA_REFERENCE.child(societyServiceUID);

                /*Guard Name has been changed*/
                if (!updatedGuardName.equals(nameOldContent)) {
                    DatabaseReference guardNameReference = guardsDataReference.child(FIREBASE_CHILD_FULLNAME);
                    guardNameReference.setValue(updatedGuardName);
                }

                /*Guard Gate Number has been changed*/
                if (!updatedGuardGateNumber.equals(String.valueOf(gateNumberOldContent))) {
                    DatabaseReference guardGateNumberReference = guardsDataReference.child(FIREBASE_CHILD_GATE_NUMBER);
                    guardGateNumberReference.setValue(Integer.parseInt(updatedGuardGateNumber));
                }

                /*Checking if admin updates either name or gate number or profile photo and navigates to appropriate screen.*/
                if (!updatedGuardName.equals(nameOldContent) || !updatedGuardGateNumber.equals(String.valueOf(gateNumberOldContent)) || profilePicChanged) {
                    startActivity(staffDetailsIntent);
                }
            }

        }
    }

    /**
     * This method updates the society service staff name in firebase.
     */
    private void updateStaffName() {
        /*Displaying Progress Dialog when admin updates staff details*/
        showProgressDialog(EditStaffActivity.this,
                getString(R.string.updating_profile),
                getString(R.string.please_wait_a_moment));

        /*Navigating Admin to Staff Details Screen*/
        Intent staffDetailsIntent = new Intent(EditStaffActivity.this, StaffActivity.class);
        staffDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*Getting the updated staff name from the edit text field*/
        newContent = editStaffName.getText().toString();
        DatabaseReference societyServiceDataReference = SOCIETY_SERVICES_REFERENCE.child(societyServiceType);
        switch (screenTitle) {
            case R.string.plumbers:
                DatabaseReference plumberNameReference = societyServiceDataReference.child(FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(societyServiceUID)
                        .child(FIREBASE_CHILD_FULLNAME);
                plumberNameReference.setValue(newContent).addOnSuccessListener(task -> {
                    hideProgressDialog();
                    staffDetailsIntent.putExtra(SOCIETY_SERVICE_TYPE, getString(R.string.plumber));
                    startActivity(staffDetailsIntent);
                });
                break;
            case R.string.carpenters:
                DatabaseReference carpenterNameReference = societyServiceDataReference.child(FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(societyServiceUID)
                        .child(FIREBASE_CHILD_FULLNAME);
                carpenterNameReference.setValue(newContent).addOnSuccessListener(task -> {
                    hideProgressDialog();
                    staffDetailsIntent.putExtra(SOCIETY_SERVICE_TYPE, getString(R.string.carpenter));
                    startActivity(staffDetailsIntent);
                });
                break;
            case R.string.electricians:
                DatabaseReference electricianNameReference = societyServiceDataReference.child(FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(societyServiceUID)
                        .child(FIREBASE_CHILD_FULLNAME);
                electricianNameReference.setValue(newContent).addOnSuccessListener(task -> {
                    hideProgressDialog();
                    staffDetailsIntent.putExtra(SOCIETY_SERVICE_TYPE, getString(R.string.electrician));
                    startActivity(staffDetailsIntent);
                });
                break;
            case R.string.garbageCollectors:
                DatabaseReference garbageCollectorsNameReference = societyServiceDataReference.child(FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(societyServiceUID)
                        .child(FIREBASE_CHILD_FULLNAME);
                garbageCollectorsNameReference.setValue(newContent).addOnSuccessListener(task -> {
                    hideProgressDialog();
                    staffDetailsIntent.putExtra(SOCIETY_SERVICE_TYPE, getString(R.string.garbage_management));
                    startActivity(staffDetailsIntent);
                });
                break;
        }
    }

    /**
     * Updates profile pic in firebase
     */
    private void updateProfilePicInFirebase() {
        /*displaying progress dialog while image is uploading*/
        showProgressDialog(this,
                getResources().getString(R.string.update_profile_photo),
                getResources().getString(R.string.please_wait_a_moment));

        /*Getting Guard Data Reference*/
        DatabaseReference guardsDataReference = GUARDS_DATA_REFERENCE.child(societyServiceUID);

        StorageReference storageReference = FIREBASE_STORAGE.getReference(societyServiceType)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(societyServiceUID);

        UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(EditStaffActivity.this, profilePhotoPath));

        /*adding the profile photo to storage reference*/
        uploadTask.addOnSuccessListener(taskSnapshot -> {

            /*creating the upload object to store uploaded image details*/
            String profilePhotoPath = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

            /*Update the new profile photo URL in firebase*/
            DatabaseReference updatedGuardPhotoReference = guardsDataReference.child(FIREBASE_CHILD_PROFILE_PHOTO);
            updatedGuardPhotoReference.setValue(profilePhotoPath);

            updatedGuardPhotoReference.setValue(profilePhotoPath).addOnCompleteListener(task -> hideProgressDialog());
        });
    }
}
