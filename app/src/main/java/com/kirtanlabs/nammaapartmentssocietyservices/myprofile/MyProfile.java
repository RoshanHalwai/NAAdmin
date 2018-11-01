package com.kirtanlabs.nammaapartmentssocietyservices.myprofile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_CARPENTER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ELECTRICIAN;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PLUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.GARBAGE_COLLECTOR;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.capitalizeString;


public class MyProfile extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textSocietyServiceName;
    private TextView textSocietyServiceType;
    private TextView textSocietyServiceMobileNumber;
    private TextView textTotalAcceptedCount, textTotalCount, textTotalRejectedCount;
    private TextView textMonthlyAcceptedCount, textMonthlyRejectedCount, textMonthlyTotalCount;
    private TextView textSocietyServiceRatingValue;
    private CircleImageView imageSocietyService;
    private RelativeLayout layoutMyProfile;
    private int totalAcceptedCount, totalRejectedCount, monthlyAcceptedCount, monthlyRejectedCount, monthlyTotalCount;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        textSocietyServiceName = findViewById(R.id.textSocietyServiceName);
        textSocietyServiceType = findViewById(R.id.textSocietyServiceType);
        textSocietyServiceMobileNumber = findViewById(R.id.textSocietyServiceMobileNumber);
        TextView textSocietyServiceRating = findViewById(R.id.textSocietyServiceRating);
        textSocietyServiceRatingValue = findViewById(R.id.textSocietyServiceRatingValue);
        imageSocietyService = findViewById(R.id.imageSocietyService);
        TextView textMonthly = findViewById(R.id.textMonthly);
        TextView textTillDate = findViewById(R.id.textTillDate);
        TextView textMonthlyAccepted = findViewById(R.id.textMonthlyAccepted);
        textMonthlyAcceptedCount = findViewById(R.id.textMonthlyAcceptedCount);
        TextView textTotalAccepted = findViewById(R.id.textTotalAccepted);
        textTotalAcceptedCount = findViewById(R.id.textTotalAcceptedCount);
        TextView textMonthlyRejected = findViewById(R.id.textMonthlyRejected);
        textMonthlyRejectedCount = findViewById(R.id.textMonthlyRejectedCount);
        TextView textTotalRejected = findViewById(R.id.textTotalRejected);
        textTotalRejectedCount = findViewById(R.id.textTotalRejectedCount);
        TextView textMonthlyTotal = findViewById(R.id.textMonthlyTotal);
        textMonthlyTotalCount = findViewById(R.id.textMonthlyTotalCount);
        TextView textTotal = findViewById(R.id.textTotal);
        textTotalCount = findViewById(R.id.textTotalCount);
        layoutMyProfile = findViewById(R.id.layoutMyProfile);

        /*Setting font for all the views*/
        textSocietyServiceName.setTypeface(setLatoBoldFont(this));
        textSocietyServiceType.setTypeface(setLatoBoldFont(this));
        textSocietyServiceMobileNumber.setTypeface(setLatoBoldFont(this));
        textSocietyServiceRating.setTypeface(setLatoBoldFont(this));
        textSocietyServiceRatingValue.setTypeface(setLatoBoldFont(this));
        textMonthly.setTypeface(setLatoBoldFont(this));
        textTillDate.setTypeface(setLatoBoldFont(this));
        textMonthlyAccepted.setTypeface(setLatoRegularFont(this));
        textMonthlyAcceptedCount.setTypeface(setLatoBoldFont(this));
        textTotalAccepted.setTypeface(setLatoRegularFont(this));
        textTotalAcceptedCount.setTypeface(setLatoBoldFont(this));
        textMonthlyRejected.setTypeface(setLatoRegularFont(this));
        textMonthlyRejectedCount.setTypeface(setLatoBoldFont(this));
        textTotalRejected.setTypeface(setLatoRegularFont(this));
        textTotalRejectedCount.setTypeface(setLatoBoldFont(this));
        textMonthlyTotal.setTypeface(setLatoRegularFont(this));
        textMonthlyTotalCount.setTypeface(setLatoBoldFont(this));
        textTotal.setTypeface(setLatoRegularFont(this));
        textTotalCount.setTypeface(setLatoBoldFont(this));

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Details Society Service who is currently logged in*/
        getSocietyServiceDetails();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to Retrieve details of Society Service who is logged in and
     * display details in My Profile screen
     */
    @SuppressLint("SetTextI18n")
    private void getSocietyServiceDetails() {
        /*Getting Current Month here*/
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        String currentMonth = new SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.getTime());

        /*Getting Society Service Personal details*/
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData(MyProfile.this, societyServiceUID);
        retrievingNotificationData.getSocietyServiceData(societyServiceData -> {
            String societyServiceName = societyServiceData.getFullName();
            String societyServiceMobile = societyServiceData.getMobileNumber();
            String societyServiceType = societyServiceData.getSocietyServiceType();
            int societyServiceRating = societyServiceData.getRating();

            textSocietyServiceName.setText(societyServiceName);
            textSocietyServiceMobileNumber.setText(societyServiceMobile);
            /*Displaying Garbage Collector if societyService Type is Garbage Collection*/
            if (!societyServiceType.equals(FIREBASE_CHILD_GARBAGE_COLLECTION)) {
                textSocietyServiceType.setText(capitalizeString(societyServiceType));
            } else {
                textSocietyServiceType.setText(GARBAGE_COLLECTOR);
            }
            textSocietyServiceRatingValue.setText(String.valueOf(societyServiceRating));

            switch (societyServiceType) {
                case FIREBASE_CHILD_PLUMBER:
                    imageSocietyService.setImageResource(R.drawable.plumber);
                    break;
                case FIREBASE_CHILD_CARPENTER:
                    imageSocietyService.setImageResource(R.drawable.carpenter);
                    break;
                case FIREBASE_CHILD_ELECTRICIAN:
                    imageSocietyService.setImageResource(R.drawable.electrician);
                    break;
                case FIREBASE_CHILD_GARBAGE_COLLECTION:
                    imageSocietyService.setImageResource(R.drawable.garbage_bag);
                    break;
            }
        });

        /*Getting No. of Accepted, Rejected and Total request for monthly and till date for particular society service*/
        retrievingNotificationData.getHistoryNotificationDataList(historyNotificationDataList -> {
            if (historyNotificationDataList != null) {
                /*Getting Total no. of request came till date to that society service*/
                int totalCount = historyNotificationDataList.size();

                for (SocietyServiceNotification societyServiceNotification : historyNotificationDataList) {
                    String status = societyServiceNotification.getSocietyServiceResponse();

                    /*Getting Total no. of Accepted and Rejected Request for that particular society service Till Date*/
                    if (status.equals(getString(R.string.accepted))) {
                        totalAcceptedCount++;
                    } else {
                        totalRejectedCount++;
                    }

                    /*Decoding Time Stamp to Month*/
                    long timestamp = societyServiceNotification.getTimeStamp();
                    calendar.setTimeInMillis(timestamp);
                    String notificationMonth = DateFormat.format("MMM", calendar).toString();

                    /*Getting No. of Accepted, Rejected and Total request came to that particular society service in current month*/
                    if (currentMonth.equals(notificationMonth)) {
                        monthlyTotalCount++;
                        if (status.equals(getString(R.string.accepted))) {
                            monthlyAcceptedCount++;
                        } else {
                            monthlyRejectedCount++;
                        }
                    }
                }

                textMonthlyAcceptedCount.setText(String.valueOf(monthlyAcceptedCount));
                textMonthlyRejectedCount.setText(String.valueOf(monthlyRejectedCount));
                textMonthlyTotalCount.setText(String.valueOf(monthlyTotalCount));

                textTotalCount.setText(String.valueOf(totalCount));
                textTotalAcceptedCount.setText(String.valueOf(totalAcceptedCount));
                textTotalRejectedCount.setText(String.valueOf(totalRejectedCount));
            }

            /*Displaying My Profile layout after retrieving all details*/
            hideProgressIndicator();
            layoutMyProfile.setVisibility(View.VISIBLE);

        });
    }
}
