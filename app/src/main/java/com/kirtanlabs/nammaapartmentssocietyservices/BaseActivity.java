package com.kirtanlabs.nammaapartmentssocietyservices;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.History;
import com.kirtanlabs.nammaapartmentssocietyservices.login.SignIn;
import com.kirtanlabs.nammaapartmentssocietyservices.myprofile.MyProfile;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.regex.Pattern;

import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SEND_SMS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoItalicFont;

/**
 * Root activity for most of the Activities of this project.
 * Responsible for creating toolbar by getting title from the activity
 * and implementing events on back button.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ImageView backButton, imageMenu;
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
    private void setMenuIconListener() {
        imageMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, imageMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
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

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    protected void hideBackButton() {
        backButton.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is used to display menu icon in title bar wherever its needed.
     */
    protected void showMenuIcon() {
        imageMenu = findViewById(R.id.imageMenu);
        imageMenu.setVisibility(View.VISIBLE);
        setMenuIconListener();
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

    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        backButton = findViewById(R.id.backButton);
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
        }
    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    /**
     * This method checks if all the editTexts are filled or not.
     *
     * @param fields consists of array of EditTexts.
     * @return consists of boolean variable based on the context.
     */
    protected boolean isAllFieldsFilled(EditText[] fields) {
        for (EditText currentField : fields) {
            if (TextUtils.isEmpty(currentField.getText().toString())) {
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }


    protected void showProgressDialog(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
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

    public boolean isValidPhone(String phone) {
        return !Pattern.matches("[a-zA-Z]+", phone) && phone.length() == PHONE_NUMBER_MAX_LENGTH;
    }

}
