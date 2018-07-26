package com.kirtanlabs.nammaapartmentssocietyservices.endservice;

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

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.EDIT_TEXT_EMPTY_LENGTH;


public class OTP extends BaseActivity implements View.OnClickListener {

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

        /*Setting font for all the views*/
        textDescription.setTypeface(Constants.setLatoRegularFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyOTP.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        //TODO:To Write Logic to compare the OTP entered by the Society Service Person matches with the OTP sent to user’s Mobile number
        boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{
                editFirstOTPDigit,
                editSecondOTPDigit,
                editThirdOTPDigit,
                editFourthOTPDigit,
                editFifthOTPDigit,
                editSixthOTPDigit
        });
        if (allFieldsFilled) {
            finish();
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
                if (count > EDIT_TEXT_EMPTY_LENGTH) {
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
                if (count == EDIT_TEXT_EMPTY_LENGTH) {
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
                if (count == EDIT_TEXT_EMPTY_LENGTH) {
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
                if (count == EDIT_TEXT_EMPTY_LENGTH) {
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
                if (count == EDIT_TEXT_EMPTY_LENGTH) {
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
                if (count == EDIT_TEXT_EMPTY_LENGTH) {
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
}