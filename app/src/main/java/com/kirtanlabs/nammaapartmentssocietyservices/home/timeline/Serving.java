package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.endservice.OTP;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.END_SERVICE_REQUEST_CODE;

public class Serving extends Fragment implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private BaseActivity baseActivity;
    private RelativeLayout layoutAwaitingResponse, layoutAcceptedUserDetails;
    private TextView textResidentNameValue;
    private TextView textApartmentValue;
    private TextView textFlatNumberValue;
    private TextView textTimeSlotValue;
    private TextView textProblemDescriptionValue;
    private TextView textServiceTypeValue;
    private String societyServiceUid;

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serving, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        layoutAwaitingResponse = view.findViewById(R.id.layoutAwaitingResponse);
        layoutAcceptedUserDetails = view.findViewById(R.id.layoutAcceptedUserDetails);
        TextView textAwaitingResponse = view.findViewById(R.id.textAwaitingResponse);
        TextView textResidentName = view.findViewById(R.id.textResidentName);
        TextView textApartment = view.findViewById(R.id.textApartment);
        TextView textFlatNumber = view.findViewById(R.id.textFlatNumber);
        TextView textServiceType = view.findViewById(R.id.textServiceType);
        TextView textTimeSlot = view.findViewById(R.id.textTimeSlot);
        TextView textProblemDescription = view.findViewById(R.id.textProblemDescription);
        textResidentNameValue = view.findViewById(R.id.textResidentNameValue);
        textApartmentValue = view.findViewById(R.id.textApartmentValue);
        textFlatNumberValue = view.findViewById(R.id.textFlatNumberValue);
        textServiceTypeValue = view.findViewById(R.id.textServiceTypeValue);
        textTimeSlotValue = view.findViewById(R.id.textTimeSlotValue);
        textProblemDescriptionValue = view.findViewById(R.id.textProblemDescriptionValue);
        Button buttonCallResident = view.findViewById(R.id.buttonCallResident);
        Button buttonEndService = view.findViewById(R.id.buttonEndService);

        /*Setting font for all the views*/
        textAwaitingResponse.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textResidentName.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textApartment.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textFlatNumber.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textServiceType.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textTimeSlot.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textProblemDescription.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textResidentNameValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textApartmentValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textTimeSlotValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textProblemDescriptionValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        buttonCallResident.setTypeface(Constants.setLatoLightFont(Objects.requireNonNull(getActivity())));
        buttonEndService.setTypeface(Constants.setLatoLightFont(Objects.requireNonNull(getActivity())));

        baseActivity = (BaseActivity) getActivity();

        /*Setting listener for item in card view*/
        buttonCallResident.setOnClickListener(this);
        buttonEndService.setOnClickListener(this);

        /* This method is used to display Details of User to whom Society service is working currently*/
        getUserRequestDetails();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCallResident:
                baseActivity.makePhoneCall();
                break;
            case R.id.buttonEndService:
                Intent intent = new Intent(getActivity(), OTP.class);
                intent.putExtra(Constants.SCREEN_TITLE, R.string.serving);
                startActivityForResult(intent, END_SERVICE_REQUEST_CODE);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to retrieve user request details to whom society service in currently working from firebase.
     */
    private void getUserRequestDetails() {

        if (getArguments() != null) {
            societyServiceUid = getArguments().getString(Constants.SOCIETY_SERVICE_UID);
        }

        DatabaseReference societyServiceType = Constants.SOCIETY_SERVICE_TYPE_REFERENCE.child(societyServiceUid);
        societyServiceType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot societyServiceTypeSnapshot : dataSnapshot.getChildren()) {
                    /*Getting the societyServiceType*/
                    String societyServiceType = societyServiceTypeSnapshot.getKey();

                    /*Getting notificationUID*/
                    DatabaseReference notificationReference = Constants.SOCIETY_SERVICES_REFERENCE
                            .child(societyServiceType)
                            .child(Constants.FIREBASE_CHILD_PRIVATE)
                            .child(Constants.FIREBASE_CHILD_DATA)
                            .child(societyServiceUid)
                            .child(Constants.FIREBASE_CHILD_NOTIFICATIONS);
                    notificationReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                layoutAwaitingResponse.setVisibility(View.GONE);
                                layoutAcceptedUserDetails.setVisibility(View.VISIBLE);

                                for (DataSnapshot notificationsDataSnapshot : dataSnapshot.getChildren()) {
                                    /*Getting Status of notificationUID whether Society Service has "Accepted" or "Rejected" user request*/
                                    String notificationValue = notificationsDataSnapshot.getValue(String.class);
                                    if (notificationValue.equals(getString(R.string.accepted))) {

                                        String notificationUID = notificationsDataSnapshot.getKey();

                                        /*Retrieving details of request to whom society service is serving*/
                                        DatabaseReference userRequestDetailsReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                                                .child(notificationUID);
                                        userRequestDetailsReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String date = dataSnapshot.child(Constants.FIREBASE_CHILD_TIME_SLOT).getValue(String.class);
                                                String problemDescription = dataSnapshot.child(Constants.FIREBASE_CHILD_PROBLEM).getValue(String.class);
                                                String userUID = dataSnapshot.child(Constants.FIREBASE_CHILD_USER_UID).getValue(String.class);

                                                textServiceTypeValue.setText(societyServiceType);
                                                textTimeSlotValue.setText(date);
                                                textProblemDescriptionValue.setText(problemDescription);

                                                /*Retrieving details of users to whom society service is serving*/
                                                DatabaseReference userDatabaseReference = Constants.PRIVATE_USERS_REFERENCE
                                                        .child(userUID);
                                                userDatabaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String residentName = dataSnapshot.child(Constants.FIREBASE_CHILD_PERSONAL_DETAILS)
                                                                .child(Constants.FIREBASE_CHILD_FULL_NAME).getValue(String.class);
                                                        String apartment = dataSnapshot.child(Constants.FIREBASE_CHILD_FLAT_DETAILS)
                                                                .child(Constants.FIREBASE_CHILD_APARTMENT_NAME).getValue(String.class);
                                                        String flat = dataSnapshot.child(Constants.FIREBASE_CHILD_FLAT_DETAILS)
                                                                .child(Constants.FIREBASE_CHILD_FLAT_NUMBER).getValue(String.class);

                                                        textResidentNameValue.setText(residentName);
                                                        textApartmentValue.setText(apartment);
                                                        textFlatNumberValue.setText(flat);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
