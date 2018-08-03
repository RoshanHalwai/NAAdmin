package com.kirtanlabs.nammaapartmentssocietyservices.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.Register;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;

import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.EDIT_TEXT_EMPTY_LENGTH;


public class OTP extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textDescription, textResendOTPOrVerificationMessage, textChangeNumberOrTimer;
    private EditText editFirstOTPDigit;
    private EditText editSecondOTPDigit;
    private EditText editThirdOTPDigit;
    private EditText editFourthOTPDigit;
    private EditText editFifthOTPDigit;
    private EditText editSixthOTPDigit;
    private Button buttonVerifyOTP;
    private int previousScreenTitle;

    private int RESEND_OTP_SECONDS;
    private int RESEND_OTP_MINUTE;

    /* ------------------------------------------------------------- *
     * Private Members for Phone Authentication
     * ------------------------------------------------------------- */

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String phoneVerificationId, userMobileNumber;

    /* ------------------------------------------------------------- *
     * Private Members for Firebase
     * ------------------------------------------------------------- */

    private FirebaseAuth fbAuth;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.phone_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        fbAuth = FirebaseAuth.getInstance();

        /*Getting Id's for all the views*/
        textDescription = findViewById(R.id.textDescription);
        editFirstOTPDigit = findViewById(R.id.editFirstOTPDigit);
        editSecondOTPDigit = findViewById(R.id.editSecondOTPDigit);
        editThirdOTPDigit = findViewById(R.id.editThirdOTPDigit);
        editFourthOTPDigit = findViewById(R.id.editFourthOTPDigit);
        editFifthOTPDigit = findViewById(R.id.editFifthOTPDigit);
        editSixthOTPDigit = findViewById(R.id.editSixthOTPDigit);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        textResendOTPOrVerificationMessage = findViewById(R.id.textResendOTPOrVerificationMessage);
        textChangeNumberOrTimer = findViewById(R.id.textChangeNumberOrTimer);

        /*Setting font for all the views*/
        textDescription.setTypeface(Constants.setLatoRegularFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));

        /* Since multiple activities make use of this class we get previous
         * screen title and update the views accordingly*/
        getPreviousScreenTitle();
        updatePhoneVerificationText();

        /*If Previous screen title is Serving, we wouldn't want Firebase to generate OTP*/
        if (previousScreenTitle != R.string.serving) {
            /* Generate an OTP to user's mobile number */
            userMobileNumber = getIntent().getStringExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER);
            sendOTP();
            /* Start the Resend OTP timer, valid for 120 seconds*/
            startResendOTPTimer();
        }

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyOTP.setOnClickListener(this);
        textResendOTPOrVerificationMessage.setOnClickListener(v -> resendOTP());
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        /*We wouldn't want to show progress dialog if previous screen title is Serving*/
        if (previousScreenTitle != R.string.serving) {
            /*Displaying progress dialog while OTP is being validated*/
            showProgressDialog(OTP.this,
                    getResources().getString(R.string.verifying_account),
                    getResources().getString(R.string.please_wait_a_moment));
        }

        boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{
                editFirstOTPDigit,
                editSecondOTPDigit,
                editThirdOTPDigit,
                editFourthOTPDigit,
                editFifthOTPDigit,
                editSixthOTPDigit
        });
        if (allFieldsFilled) {
            hideKeyboard();
            String code = editFirstOTPDigit.getText().toString() + editSecondOTPDigit.getText().toString() +
                    editThirdOTPDigit.getText().toString() + editFourthOTPDigit.getText().toString() + editFifthOTPDigit.getText().toString() +
                    editSixthOTPDigit.getText().toString();

            if (previousScreenTitle == R.string.serving) {
                if (code.equals(getIntent().getStringExtra(Constants.END_OTP))) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } else {
                    /*Show this message if user has entered wrong OTP*/
                    textResendOTPOrVerificationMessage.setText(R.string.wrong_otp_entered);
                }
            } else {
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(phoneVerificationId, code));
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to send OTP to mobile that user has entered
     */
    private void sendOTP() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks);
    }

    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                /*displaying progress dialog while OTP is being validated*/
                showProgressDialog(OTP.this,
                        getResources().getString(R.string.verifying_account),
                        getResources().getString(R.string.please_wait_a_moment));

                /*Hiding the Keyboard in case the Auto-Verification is completed*/
                hideKeyboard();
                textResendOTPOrVerificationMessage.setText(R.string.auto_verification_completed);
                textResendOTPOrVerificationMessage.setEnabled(false);
                textChangeNumberOrTimer.setVisibility(View.INVISIBLE);
                if (phoneAuthCredential.getSmsCode() != null) {
                    char[] smsCode = phoneAuthCredential.getSmsCode().toCharArray();
                    editFirstOTPDigit.setText(String.valueOf(smsCode[0]));
                    editSecondOTPDigit.setText(String.valueOf(smsCode[1]));
                    editThirdOTPDigit.setText(String.valueOf(smsCode[2]));
                    editFourthOTPDigit.setText(String.valueOf(smsCode[3]));
                    editFifthOTPDigit.setText(String.valueOf(smsCode[4]));
                    editSixthOTPDigit.setText(String.valueOf(smsCode[5]));
                }
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId = s;
                resendToken = forceResendingToken;
            }

        };
    }

    /**
     * Checking if OTP entered by user and the OTP generated by Firebase is same or not.
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        fbAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, (task) -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        /*Existing user is Logging in*/
                        if (previousScreenTitle == R.string.login) {
                            DatabaseReference allSocietyServiceReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child(userMobileNumber);
                            allSocietyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Society Service has Logged In
                                    if (dataSnapshot.exists()) {
                                        Intent intent = new Intent(OTP.this, HomeViewPager.class);
                                        intent.putExtra(Constants.SOCIETY_SERVICE_MOBILE_NUMBER, userMobileNumber);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        DatabaseReference societyServiceAdminReference = Constants.SOCIETY_SERVICES_ADMIN_REFERENCE;
                                        societyServiceAdminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //Admin has logged In
                                                if (userMobileNumber.equals(dataSnapshot.getValue(String.class))) {
                                                    startActivity(new Intent(OTP.this, Register.class));
                                                    finish();
                                                } else {
                                                    //New member has logged in whose mobile number is not yet registered
                                                    //TODO: Add UI Layout with the below message
                                                    Toast.makeText(OTP.this, "Mobile Number not found, Request Admin to add Mobile Number", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            /*If Admin Registers new user or when the society service is ending the service*/
                            setResult(Activity.RESULT_OK, new Intent());
                            finish();
                        }
                    } else {
                        /*Check if network is available or not*/
                        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (!isConnected) {
                            /*Show this message if user is having no network connection*/
                            textResendOTPOrVerificationMessage.setText(R.string.check_network_connection);
                        } else {
                            /*Show this message if user has entered wrong OTP*/
                            textResendOTPOrVerificationMessage.setText(R.string.wrong_otp_entered);
                        }
                    }
                });
    }

    /**
     * Resend OTP if the user doesn't receive after 120 seconds
     */
    private void resendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
        startResendOTPTimer();
    }

    private void startResendOTPTimer() {
        textResendOTPOrVerificationMessage.setText(R.string.waiting_for_otp);
        RESEND_OTP_MINUTE = 1;
        RESEND_OTP_SECONDS = 59;
        String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
        textChangeNumberOrTimer.setText(timer);
        textChangeNumberOrTimer.setEnabled(false);
        textResendOTPOrVerificationMessage.setEnabled(false);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    RESEND_OTP_SECONDS -= 1;
                    String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                    textChangeNumberOrTimer.setText(timer);
                    if (RESEND_OTP_MINUTE == 0 && RESEND_OTP_SECONDS == 0) {
                        t.cancel();

                        /*User can  Resend OTP to their mobile number*/
                        textChangeNumberOrTimer.setText("");
                        textResendOTPOrVerificationMessage.setText(R.string.resend_otp);
                        textResendOTPOrVerificationMessage.setEnabled(true);
                        textChangeNumberOrTimer.setEnabled(true);
                    } else if (RESEND_OTP_SECONDS == 0) {
                        timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                        textChangeNumberOrTimer.setText(timer);
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


    /**
     * This method to invoked to get the screen title based on the user navigating from previous screen
     */
    private void getPreviousScreenTitle() {
        previousScreenTitle = getIntent().getIntExtra(Constants.SCREEN_TITLE, 0);
    }

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

    /**
     * We update the Phone Verification text based on the activity calling this class.
     */
    private void updatePhoneVerificationText() {
        switch (previousScreenTitle) {
            case R.string.login:
                textDescription.setText(R.string.enter_verification_code);
                break;
            case R.string.register:
                textDescription.setText(R.string.enter_verification_code_for_registration);
                break;
            case R.string.serving:
                textDescription.setText(R.string.enter_verification_code_to_end_service);
                break;
        }
    }
}