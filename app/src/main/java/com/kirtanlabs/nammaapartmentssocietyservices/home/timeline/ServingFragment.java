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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;
import com.kirtanlabs.nammaapartmentssocietyservices.login.OTP;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.END_SERVICE_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.capitalizeString;

public class ServingFragment extends Fragment implements View.OnClickListener {

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
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData();
        retrievingNotificationData.getServiceType(serviceType ->
                retrievingNotificationData.getServingNotificationUID(serviceType, servingUID -> {
                    /*Indicates Person is not serving any notification at the moment*/
                    if (servingUID == null) {
                        return;
                    }
                    /*Person is currently serving flat, get the details and show in Card View*/
                    retrievingNotificationData.getNotificationData(servingUID, societyServiceNotification ->
                            retrievingNotificationData.getUserData(societyServiceNotification.getUserUID(), NAUser -> {
                                textResidentNameValue.setText(NAUser.getPersonalDetails().getFullName());
                                textApartmentValue.setText(NAUser.getFlatDetails().getApartmentName());
                                textFlatNumberValue.setText(NAUser.getFlatDetails().getFlatNumber());
                                textServiceTypeValue.setText(capitalizeString(societyServiceNotification.getSocietyServiceType()));
                                textTimeSlotValue.setText(societyServiceNotification.getTimeSlot());
                                textProblemDescriptionValue.setText(societyServiceNotification.getProblem());
                                layoutAwaitingResponse.setVisibility(View.GONE);
                                layoutAcceptedUserDetails.setVisibility(View.VISIBLE);
                            })
                    );
                })
        );
    }

}
