package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.OTP_CODE_MAX_LENGTH;

public class EndService extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editFirstOTPDigit;
    private EditText editSecondOTPDigit;
    private EditText editThirdOTPDigit;
    private EditText editFourthOTPDigit;
    private EditText editFifthOTPDigit;
    private EditText editSixthOTPDigit;
    private Button buttonVerifyOTP;
    private TextView textWaitingForOTPOrResendOTP;
    private TextView textTimer;
    private int RESEND_OTP_SECONDS;
    private int RESEND_OTP_MINUTE;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_end_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.end_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textDescription = findViewById(R.id.textDescription);
        editFirstOTPDigit = findViewById(R.id.editFirstOTPDigit);
        editSecondOTPDigit = findViewById(R.id.editSecondOTPDigit);
        editThirdOTPDigit = findViewById(R.id.editThirdOTPDigit);
        editFourthOTPDigit = findViewById(R.id.editFourthOTPDigit);
        editFifthOTPDigit = findViewById(R.id.editFifthOTPDigit);
        editSixthOTPDigit = findViewById(R.id.editSixthOTPDigit);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        textWaitingForOTPOrResendOTP = findViewById(R.id.textWaitingForOTPOrResendOTP);
        textTimer = findViewById(R.id.textTimer);

        /*Setting font for all the views*/
        textDescription.setTypeface(Constants.setLatoRegularFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));
        textWaitingForOTPOrResendOTP.setTypeface(Constants.setLatoRegularFont(this));
        textTimer.setTypeface(Constants.setLatoRegularFont(this));

        // To start timer of 120seconds on load of activity.
        startResendOTPTimer();

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyOTP.setOnClickListener(this);
        textWaitingForOTPOrResendOTP.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyOTP:
                boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{
                        editFirstOTPDigit,
                        editSecondOTPDigit,
                        editThirdOTPDigit,
                        editFourthOTPDigit,
                        editFifthOTPDigit,
                        editSixthOTPDigit
                });
                if (allFieldsFilled) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.textWaitingForOTPOrResendOTP:
                startResendOTPTimer();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * Once user enters a digit in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editFirstOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editFirstOTPDigit.requestFocus();
                } else {
                    editSecondOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editSecondOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editFirstOTPDigit.requestFocus();
                } else {
                    editThirdOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editThirdOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editSecondOTPDigit.requestFocus();
                } else {
                    editFourthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editFourthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editThirdOTPDigit.requestFocus();
                } else {
                    editFifthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editFifthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editFourthOTPDigit.requestFocus();
                } else {
                    editSixthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editSixthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < OTP_CODE_MAX_LENGTH) {
                    editFifthOTPDigit.requestFocus();
                    buttonVerifyOTP.setVisibility(View.INVISIBLE);
                } else {
                    buttonVerifyOTP.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * This method is invoked to start timer again on click of Resend OTP, if the user doesn't receive after 120 seconds
     */
    private void startResendOTPTimer() {
        textWaitingForOTPOrResendOTP.setText(R.string.waiting_for_otp);
        textTimer.setVisibility(View.VISIBLE);
        RESEND_OTP_MINUTE = 1;
        RESEND_OTP_SECONDS = 59;
        String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
        textTimer.setText(timer);
        textTimer.setEnabled(false);
        textWaitingForOTPOrResendOTP.setEnabled(false);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    RESEND_OTP_SECONDS -= 1;
                    String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                    textTimer.setText(timer);
                    if (RESEND_OTP_MINUTE == 0 && RESEND_OTP_SECONDS == 0) {
                        t.cancel();

                        /* User can Resend OTP to owners mobile number*/
                        textWaitingForOTPOrResendOTP.setText(R.string.resend_otp);
                        textWaitingForOTPOrResendOTP.setEnabled(true);
                        textTimer.setEnabled(true);
                        textTimer.setVisibility(View.INVISIBLE);
                    } else if (RESEND_OTP_SECONDS == 0) {
                        timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                        textTimer.setText(timer);
                        RESEND_OTP_SECONDS = 60;
                        RESEND_OTP_MINUTE = RESEND_OTP_MINUTE - 1;
                    }
                });
            }
        }, 0, 1000);
    }

    private String timeFormatter(int time) {
        return String.format(Locale.ENGLISH, "%02d", time % 60);
    }
}