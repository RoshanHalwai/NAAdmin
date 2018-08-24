package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities.pojo.NoticeBoardPojo;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTICE_BOARD;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_ADMIN_REFERENCE;

public class AddNoticeActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editTitle, editDescription;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_notice;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_notice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textNoticeTitle = findViewById(R.id.textNoticeTitle);
        TextView textNoticeDescription = findViewById(R.id.textNoticeDescription);
        TextView textSocietyServiceAdminName = findViewById(R.id.textSocietyServiceAdminName);
        TextView textSocietyServiceDesignation = findViewById(R.id.textSocietyServiceDesignation);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        Button buttonAddNotice = findViewById(R.id.buttonAddNotice);

        /*Setting fonts for all the views*/
        textNoticeTitle.setTypeface(Constants.setLatoBoldFont(this));
        textNoticeDescription.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceAdminName.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceDesignation.setTypeface(Constants.setLatoBoldFont(this));
        editTitle.setTypeface(Constants.setLatoRegularFont(this));
        editDescription.setTypeface(Constants.setLatoRegularFont(this));
        buttonAddNotice.setTypeface(Constants.setLatoLightFont(this));

        /*Storing Notice Details Entered by the admin*/
        storeNoticeDetailsInFirebase();

    }

    /**
     * This method gets invoked when Admin add any notice to the society.
     */
    private void storeNoticeDetailsInFirebase() {
        /*displaying progress dialog while image is uploading*/
        showProgressDialog(this,
                getResources().getString(R.string.notice_dialog_title),
                getResources().getString(R.string.please_wait_a_moment));

        DatabaseReference noticeBoardReference = Constants.NOTICE_BOARD_REFERENCE;
        String noticeBoardUID = noticeBoardReference.push().getKey();
        noticeBoardReference.setValue(noticeBoardUID);

        DatabaseReference societyAdminReference = SOCIETY_SERVICES_ADMIN_REFERENCE.child(FIREBASE_CHILD_NOTICE_BOARD);
        societyAdminReference.child(noticeBoardUID).setValue(true);
        /*Add Notice record under noticeBoard->noticeBoardUID*/
        String titleValue = editTitle.getText().toString();
        String descriptionValue = editDescription.getText().toString();

        /*Utility Functions to get Date and Time*/
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        String formattedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
        String concatenatedDateAndTime = formattedDate + "\t\t" + " " + formattedTime;
        String nameOfAdmin = "Ashish Jha";
        NoticeBoardPojo noticeBoardPojo = new NoticeBoardPojo(nameOfAdmin, titleValue, descriptionValue, concatenatedDateAndTime);

        DatabaseReference noticeBoardDataReference = noticeBoardReference.child(noticeBoardUID);
        noticeBoardDataReference.setValue(noticeBoardPojo);

        /*dismissing the progress dialog*/
        hideProgressDialog();

        /*Notify users that they have successfully invited their visitor*/
        showNotificationDialog(getResources().getString(R.string.notice_board_dialog_title),
                getResources().getString(R.string.notice_board_success),
                null);

    }
}
