package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.SocietyAdminHome;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.pojo.NoticeBoardPojo;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTICE_BOARD;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_ADMIN_REFERENCE;

public class AddNoticeActivity extends BaseActivity implements View.OnClickListener {

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

        /*Setting events for the views*/
        buttonAddNotice.setOnClickListener(this);

    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAddNotice) {
            /*Storing Notice Details Entered by the admin*/
            storeNoticeDetailsInFirebase();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when Admin adds any notice to the society.
     */
    private void storeNoticeDetailsInFirebase() {

        /*Creating a new parent as noticeBoard  in firebase and setting the uid associated with it */
        DatabaseReference noticeBoardReference = Constants.NOTICE_BOARD_REFERENCE;
        String noticeBoardUID = noticeBoardReference.push().getKey();

        /*Setting the noticeBoardUid Value under societyservices->admin->noticeBoard*/
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
        String nameOfAdmin = getString(R.string.admin_name);
        NoticeBoardPojo noticeBoardPojo = new NoticeBoardPojo(nameOfAdmin, titleValue, descriptionValue, concatenatedDateAndTime);

        /*Here we are setting value under noticeBoard->noticeBoardUID */
        DatabaseReference noticeBoardDataReference = noticeBoardReference.child(noticeBoardUID);
        noticeBoardDataReference.setValue(noticeBoardPojo);

        /*Notify admin that they have successfully added their notice*/
        Intent adminHomeIntent = new Intent(AddNoticeActivity.this, SocietyAdminHome.class);
        showNotificationDialog(getResources().getString(R.string.notice_board_dialog_title),
                getResources().getString(R.string.notice_board_success),
                adminHomeIntent);
    }

}
