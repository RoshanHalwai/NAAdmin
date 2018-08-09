package com.kirtanlabs.nammaapartmentssocietyservices.myprofile;

import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;


public class MyProfile extends BaseActivity {

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
        TextView textSocietyServiceName = findViewById(R.id.textSocietyServiceName);
        TextView textSocietyServiceType = findViewById(R.id.textSocietyServiceType);
        TextView textSocietyServiceMobileNumber = findViewById(R.id.textSocietyServiceMobileNumber);
        TextView textSocietyServiceRating = findViewById(R.id.textSocietyServiceRating);
        TextView textWeekly = findViewById(R.id.textWeekly);
        TextView textMonthly = findViewById(R.id.textMonthly);
        TextView textTillDate = findViewById(R.id.textTillDate);
        TextView textWeeklyAccepted = findViewById(R.id.textWeeklyAccepted);
        TextView textWeeklyAcceptedCount = findViewById(R.id.textWeeklyAcceptedCount);
        TextView textMonthlyAccepted = findViewById(R.id.textMonthlyAccepted);
        TextView textMonthlyAcceptedCount = findViewById(R.id.textMonthlyAcceptedCount);
        TextView textTotalAccepted = findViewById(R.id.textTotalAccepted);
        TextView textTotalAcceptedCount = findViewById(R.id.textTotalAcceptedCount);
        TextView textWeeklyRejected = findViewById(R.id.textWeeklyRejected);
        TextView textWeeklyRejectedCount = findViewById(R.id.textWeeklyRejectedCount);
        TextView textMonthlyRejected = findViewById(R.id.textMonthlyRejected);
        TextView textMonthlyRejectedCount = findViewById(R.id.textMonthlyRejectedCount);
        TextView textTotalRejected = findViewById(R.id.textTotalRejected);
        TextView textTotalRejectedCount = findViewById(R.id.textTotalRejectedCount);
        TextView textWeeklyTotal = findViewById(R.id.textWeeklyTotal);
        TextView textWeeklyTotalCount = findViewById(R.id.textWeeklyTotalCount);
        TextView textMonthlyTotal = findViewById(R.id.textMonthlyTotal);
        TextView textMonthlyTotalCount = findViewById(R.id.textMonthlyTotalCount);
        TextView textTotal = findViewById(R.id.textTotal);
        TextView textTotalCount = findViewById(R.id.textTotalCount);

        /*Setting font for all the views*/
        textSocietyServiceName.setTypeface(setLatoBoldFont(this));
        textSocietyServiceType.setTypeface(setLatoBoldFont(this));
        textSocietyServiceMobileNumber.setTypeface(setLatoBoldFont(this));
        textSocietyServiceRating.setTypeface(setLatoBoldFont(this));
        textWeekly.setTypeface(setLatoBoldFont(this));
        textMonthly.setTypeface(setLatoBoldFont(this));
        textTillDate.setTypeface(setLatoBoldFont(this));
        textWeeklyAccepted.setTypeface(setLatoRegularFont(this));
        textWeeklyAcceptedCount.setTypeface(setLatoBoldFont(this));
        textMonthlyAccepted.setTypeface(setLatoRegularFont(this));
        textMonthlyAcceptedCount.setTypeface(setLatoBoldFont(this));
        textTotalAccepted.setTypeface(setLatoRegularFont(this));
        textTotalAcceptedCount.setTypeface(setLatoBoldFont(this));
        textWeeklyRejected.setTypeface(setLatoRegularFont(this));
        textWeeklyRejectedCount.setTypeface(setLatoBoldFont(this));
        textMonthlyRejected.setTypeface(setLatoRegularFont(this));
        textMonthlyRejectedCount.setTypeface(setLatoBoldFont(this));
        textTotalRejected.setTypeface(setLatoRegularFont(this));
        textTotalRejectedCount.setTypeface(setLatoBoldFont(this));
        textWeeklyTotal.setTypeface(setLatoRegularFont(this));
        textWeeklyTotalCount.setTypeface(setLatoBoldFont(this));
        textMonthlyTotal.setTypeface(setLatoRegularFont(this));
        textMonthlyTotalCount.setTypeface(setLatoBoldFont(this));
        textTotal.setTypeface(setLatoRegularFont(this));
        textTotalCount.setTypeface(setLatoBoldFont(this));
    }
}
