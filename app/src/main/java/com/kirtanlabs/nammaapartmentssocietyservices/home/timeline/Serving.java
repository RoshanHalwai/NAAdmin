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
import android.widget.TextView;

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
        TextView textAwaitingResponse = view.findViewById(R.id.textAwaitingResponse);
        TextView textResidentName = view.findViewById(R.id.textResidentName);
        TextView textApartment = view.findViewById(R.id.textApartment);
        TextView textFlatNumber = view.findViewById(R.id.textFlatNumber);
        TextView textDate = view.findViewById(R.id.textDate);
        TextView textProblemDescription = view.findViewById(R.id.textProblemDescription);
        TextView textResidentNameValue = view.findViewById(R.id.textResidentNameValue);
        TextView textApartmentValue = view.findViewById(R.id.textApartmentValue);
        TextView textFlatNumberValue = view.findViewById(R.id.textFlatNumberValue);
        TextView textDateValue = view.findViewById(R.id.textDateValue);
        TextView textProblemDescriptionValue = view.findViewById(R.id.textProblemDescriptionValue);
        Button buttonCallResident = view.findViewById(R.id.buttonCallResident);
        Button buttonEndService = view.findViewById(R.id.buttonEndService);

        /*Setting font for all the views*/
        textAwaitingResponse.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textResidentName.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textApartment.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textFlatNumber.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textDate.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textProblemDescription.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(getActivity())));
        textResidentNameValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textApartmentValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textDateValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        textProblemDescriptionValue.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(getActivity())));
        buttonCallResident.setTypeface(Constants.setLatoLightFont(Objects.requireNonNull(getActivity())));
        buttonEndService.setTypeface(Constants.setLatoLightFont(Objects.requireNonNull(getActivity())));

        baseActivity = (BaseActivity) getActivity();

        //Setting listener for item in card view
        buttonCallResident.setOnClickListener(this);
        buttonEndService.setOnClickListener(this);
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
}