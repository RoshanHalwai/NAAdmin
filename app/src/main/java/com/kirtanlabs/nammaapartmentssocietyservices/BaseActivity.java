package com.kirtanlabs.nammaapartmentssocietyservices;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.login.SignIn;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.regex.Pattern;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;

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
    private Intent callIntent;

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
                startActivity(new Intent(this, SignIn.class));
                finish();
                return true;
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

        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startActivity(callIntent);
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

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    public void showProgressIndicator() {
        progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.smoothToShow();
    }

    public void hideProgressIndicator() {
        if (progressIndicator == null)
            progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.smoothToHide();
    }

    /**
     * We check if permissions are granted to place calls if granted then we directly start Dialer Activity
     * else we show Request permission dialog to allow users to give access.
     */
    public void makePhoneCall() {
        //TODO: To remove this mobile number from here.
        String mobileNumber = "9885665744";
        callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PLACE_CALL_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(callIntent);
        }
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
