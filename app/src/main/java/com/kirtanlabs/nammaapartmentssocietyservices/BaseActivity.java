package com.kirtanlabs.nammaapartmentssocietyservices;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.History;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.login.SignIn;
import com.kirtanlabs.nammaapartmentssocietyservices.myprofile.MyProfile;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;
import java.util.regex.Pattern;

import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ENABLE_LOCATION_PERMISSION_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_AUTH;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_LATITUDE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_LONGITUDE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SEND_SMS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;

/**
 * Root activity for most of the Activities of this project.
 * Responsible for creating toolbar by getting title from the activity
 * and implementing events on back button.
 */
public abstract class BaseActivity extends AppCompatActivity implements LocationListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ImageView backButton, imageMenu, imageTODOIcon;
    private AVLoadingIndicatorView progressIndicator;
    private Intent callIntent, msgIntent;
    private ProgressDialog progressDialog;

    /* ------------------------------------------------------------- *
     * Abstract Methods
     * ------------------------------------------------------------- */

    protected abstract int getLayoutResourceId();

    protected abstract int getActivityTitle();

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void setActivityTitle(int resourceId) {
        TextView activityTitle = findViewById(R.id.textActivityTitle);
        activityTitle.setTypeface(Constants.setLatoRegularFont(this));
        activityTitle.setText(resourceId);
    }

    private void setBackButtonListener() {
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    /**
     * This method is used to display a pop menu on click of menu icon and performs actions based on click of item in the list.
     */
    private void setMenuIconListener(String loginType) {
        imageMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, imageMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

            /*Hiding 'My Profile' and 'History' items from Menu when Login type is Admin*/
            if (loginType.equals(Constants.FIREBASE_CHILD_ADMIN)) {
                popupMenu.getMenu().findItem(R.id.myProfile).setVisible(false);
                popupMenu.getMenu().findItem(R.id.history).setVisible(false);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.myProfile:
                        startActivity(new Intent(this, MyProfile.class));
                        break;
                    case R.id.history:
                        startActivity(new Intent(this, History.class));
                        break;
                    case R.id.logout:
                        /*Removing Society Service Uid from Shared Preference and Updating Login value to false*/
                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_SOCIETY_SERVICES_PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.LOGGED_IN, false);
                        editor.putString(Constants.SOCIETY_SERVICE_UID, null);
                        editor.putString(Constants.LOGIN_TYPE, null);
                        editor.apply();
                        startActivity(new Intent(this, SignIn.class));
                        finish();
                        break;
                }
                return super.onOptionsItemSelected(item);
            });
            popupMenu.show();
        });
    }

    /**
     * This method is invoked to display a dialog box when user user click on "Deny"
     * button in location permission dialog for the App.
     */
    private void showNotificationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.location_error_title));
        alertDialog.setMessage(getString(R.string.location_error_message));
        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            dialog.cancel();
            enableLocationService();
        });
        alertDialog.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        new Dialog(this);
        alertDialog.show();
    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    protected void hideBackButton() {
        backButton.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is used to display menu icon in title bar wherever its needed.
     */
    protected void showMenuIcon(String loginType) {
        imageMenu = findViewById(R.id.imageMenu);
        imageMenu.setVisibility(View.VISIBLE);

        /*Setting Click listener for the items in the menu*/
        setMenuIconListener(loginType);
    }

    /**
     * This method is invoked to hide virtual keyboard from screen.
     */
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * This method shows the to do icon.
     */
    protected void showTODOIcon() {
        imageTODOIcon.setVisibility(View.VISIBLE);
    }

    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        backButton = findViewById(R.id.backButton);
        imageTODOIcon = findViewById(R.id.imageTODOIcon);
        setActivityTitle(getActivityTitle());
        setBackButtonListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PLACE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(callIntent);
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    EasyImage.openCamera(this, 0);
                }
                break;
            case SEND_SMS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(msgIntent);
                }
                break;
            case ENABLE_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation();
                } else {
                    showNotificationDialog();
                }
                break;
        }
    }


    /* ------------------------------------------------------------- *
     * Overriding Location Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onLocationChanged(Location location) {
        SocietyServiceData societyServiceData = ((SocietyServiceGlobal) getApplicationContext()).getSocietyServiceData();
        if (societyServiceData == null) {
            new RetrievingNotificationData(this, FIREBASE_AUTH.getCurrentUser().getUid())
                    .getSocietyServiceData(societyServiceData1 -> {
                        setLocation(societyServiceData1.getSocietyServiceType(), location);
                    });
        } else {
            setLocation(societyServiceData.getSocietyServiceType(), location);
        }
    }

    private void setLocation(final String serviceType, final Location location) {
        /*Setting the value of Latitude and Longitude under (societyServices->serviceType->private->data->societyServiceUID)*/
        DatabaseReference societyServiceLocationReference = Constants.SOCIETY_SERVICES_REFERENCE
                .child(serviceType)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(FIREBASE_CHILD_DATA)
                .child(societyServiceUID);
        societyServiceLocationReference.child(FIREBASE_CHILD_LATITUDE).setValue(location.getLatitude());
        societyServiceLocationReference.child(FIREBASE_CHILD_LONGITUDE).setValue(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user is trying to enter improper format of entering name.
     *
     * @param name contains that particular editText of name
     * @throws NumberFormatException because if user tries to enter number in place of name.
     */
    protected boolean isPersonNameValid(String name) throws NumberFormatException {
        return !Pattern.matches("[a-zA-Z0-9.@() ]+", name);
    }

    /**
     * This method checks if all the editTexts are filled or not.
     *
     * @param fields consists of array of EditTexts.
     * @return consists of boolean variable based on the context.
     */
    public boolean isAllFieldsFilled(EditText[] fields) {
        for (EditText currentField : fields) {
            if (TextUtils.isEmpty(currentField.getText().toString())) {
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }


    public void showProgressDialog(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * Displays Feature Unavailable layout along with a message passed by the activity
     *
     * @param text feature unavailable message
     */
    public void showFeatureUnavailableLayout(int text) {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutFeatureUnavailable);
        featureUnavailableLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textFeatureUnavailable);
        textView.setTypeface(setLatoItalicFont(this));
        textView.setText(text);
    }

    public void showProgressIndicator() {
        progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.smoothToShow();
    }

    public void hideProgressIndicator() {
        if (progressIndicator == null)
            progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.setVisibility(View.INVISIBLE);
        progressIndicator.smoothToHide();
    }

    /**
     * Shows message box with title, message and activity to be called when user
     * clicks on Ok button
     *
     * @param title   - Title of the message
     * @param message - Body of the message
     * @param intent  - If null then on click of Ok, the dialog will disappear
     *                else intent activity will be called
     */
    public void showNotificationDialog(String title, String message, Intent intent) {
        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(this);
        alertNotifyGateDialog.setCancelable(false);
        alertNotifyGateDialog.setTitle(title);
        alertNotifyGateDialog.setMessage(message);
        if (intent == null) {
            alertNotifyGateDialog.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        } else {
            alertNotifyGateDialog.setPositiveButton("Ok", (dialog, which) -> {
                startActivity(intent);
                finish();
            });
        }

        new Dialog(this);
        alertNotifyGateDialog.show();
    }

    /**
     * We check if permissions are granted to place calls if granted then we directly start Dialer Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param mobileNumber to which call needs to be placed.
     */
    public void makePhoneCall(String mobileNumber) {
        callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PLACE_CALL_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(callIntent);
        }
    }

    /**
     * We check if permissions are granted to send SMS if granted then we directly start SMS Activity
     * else we show Request permission dialog to allow users to give access.
     *
     * @param mobileNumber - to which message needs to be sent
     */
    public void sendTextMessage(String mobileNumber) {
        msgIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mobileNumber, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(msgIntent);
        }
    }

    /**
     * This message is used to send to user whose E-Mail Id has been Passed.
     *
     * @param emailId - to which e-mail needs to be sent
     */
    public void sendEmail(String emailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
    }

    /**
     * This method checks if user is entering proper phone number or not.
     *
     * @param phone consists of string value of mobile number.
     * @return returns a boolean variable based on the context.
     */

    protected boolean isValidPhone(String phone) {
        return !Pattern.matches("[a-zA-Z]+", phone) && phone.length() == PHONE_NUMBER_MAX_LENGTH;
    }

    /**
     * We check if permissions are granted to access location of the user, if granted user's longitude and longitude can be fetched
     * else we show Request permission dialog to allow users to give access.
     */
    protected void enableLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_LOCATION_PERMISSION_CODE);
        } else {
            getLocation();
        }
    }

    /**
     * We are using the Location Manager class to get the latitude and longitude coordinates of the user
     */
    private void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Objects.requireNonNull(locationManager).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
