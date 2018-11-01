package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.SocietyAdminHome;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.pojo.NoticeBoardPojo;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTICE_BOARD;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTICE_BOARD_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_ADMIN_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class AddNoticeActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textSocietyServiceAdminName, textErrorNoticeDesc;
    private EditText editTitle, editDescription;
    private String nameOfAdmin;

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

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Getting Id's for all the views*/
        TextView textNoticeTitle = findViewById(R.id.textNoticeTitle);
        TextView textNoticeDescription = findViewById(R.id.textNoticeDescription);
        textSocietyServiceAdminName = findViewById(R.id.textSocietyServiceAdminName);
        TextView textSocietyServiceDesignation = findViewById(R.id.textSocietyServiceDesignation);
        textErrorNoticeDesc = findViewById(R.id.textErrorNoticeDesc);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        Button buttonAddNotice = findViewById(R.id.buttonAddNotice);

        /*Setting fonts for all the views*/
        textNoticeTitle.setTypeface(setLatoBoldFont(this));
        textNoticeDescription.setTypeface(setLatoBoldFont(this));
        textSocietyServiceAdminName.setTypeface(setLatoBoldFont(this));
        textSocietyServiceDesignation.setTypeface(setLatoBoldFont(this));
        textErrorNoticeDesc.setTypeface(setLatoBoldFont(this));
        editTitle.setTypeface(setLatoRegularFont(this));
        editDescription.setTypeface(setLatoRegularFont(this));
        buttonAddNotice.setTypeface(setLatoLightFont(this));

        /*This method retrieves admin Name from Firebase*/
        retrieveAdminName();
        /*Setting events for the views*/
        buttonAddNotice.setOnClickListener(this);
        historyButton.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAddNotice) {
            if (validateFields()) {
                /*Storing Notice Details Entered by the admin*/
                storeNoticeDetailsInFirebase();
            }
        } else if (v.getId() == R.id.historyButton) {
            startActivity(new Intent(AddNoticeActivity.this, NoticeBoardHistoryActivity.class));
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
        DatabaseReference noticeBoardReference = NOTICE_BOARD_REFERENCE;
        String noticeBoardUID = noticeBoardReference.push().getKey();

        /*Setting the noticeBoardUid Value under societyservices->admin->noticeBoard*/
        DatabaseReference societyAdminReference = SOCIETY_SERVICES_ADMIN_REFERENCE.child(FIREBASE_CHILD_NOTICE_BOARD);
        societyAdminReference.child(noticeBoardUID).setValue(true);

        /*Add Notice record under noticeBoard->noticeBoardUID*/
        String titleValue = editTitle.getText().toString();
        String descriptionValue = editDescription.getText().toString().trim();

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

    /**
     * This method retrieves the admin name well  before to display in UI.
     */
    private void retrieveAdminName() {
        DatabaseReference adminNameReference = SOCIETY_SERVICES_ADMIN_REFERENCE.child(FIREBASE_CHILD_FULLNAME);
        adminNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameOfAdmin = dataSnapshot.getValue(String.class);
                textSocietyServiceAdminName.setText(nameOfAdmin);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Performs validation and returns results accordingly
     *
     * @return true if validation is successful else returns false
     */
    private boolean validateFields() {
        if (!editTitle.getText().toString().isEmpty() && !editDescription.getText().toString().isEmpty())
            return true;
        else if (editTitle.getText().toString().isEmpty())
            editTitle.setError(getString(R.string.enter_notice_title));
        else
            textErrorNoticeDesc.setVisibility(View.VISIBLE);
        return false;
    }
}
