package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class AddNoticeActivity extends BaseActivity {

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
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editDescription = findViewById(R.id.editDescription);
        Button buttonAddNotice = findViewById(R.id.buttonAddNotice);

        /*Setting fonts for all the views*/
        textNoticeTitle.setTypeface(Constants.setLatoBoldFont(this));
        textNoticeDescription.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceAdminName.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceDesignation.setTypeface(Constants.setLatoBoldFont(this));
        editTitle.setTypeface(Constants.setLatoRegularFont(this));
        editDescription.setTypeface(Constants.setLatoRegularFont(this));
        buttonAddNotice.setTypeface(Constants.setLatoLightFont(this));
    }
}
