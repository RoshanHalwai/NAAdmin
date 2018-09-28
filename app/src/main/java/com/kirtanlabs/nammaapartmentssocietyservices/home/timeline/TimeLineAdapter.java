package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.capitalizeString;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.PlumberServicesHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<SocietyServiceNotification> requestDetailsList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    TimeLineAdapter(Context mCtx, List<SocietyServiceNotification> requestDetailsList) {
        this.mCtx = mCtx;
        this.requestDetailsList = requestDetailsList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public PlumberServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_resident_details, parent, false);
        return new PlumberServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlumberServicesHolder holder, int position) {

        /*Creating an instance of class SocietyServiceNotification and retrieving the values from Firebase*/
        SocietyServiceNotification societyServiceNotification = requestDetailsList.get(position);
        holder.textServiceTypeValue.setText(capitalizeString(societyServiceNotification.getSocietyServiceType()));
        holder.textTimeSlotValue.setText(societyServiceNotification.getTimeSlot());
        holder.textProblemDescriptionValue.setText(societyServiceNotification.getProblem());
        holder.textResidentNameValue.setText(societyServiceNotification.getNaUser().getPersonalDetails().getFullName());
        holder.textApartmentValue.setText(societyServiceNotification.getNaUser().getFlatDetails().getApartmentName());
        holder.textFlatNumberValue.setText(societyServiceNotification.getNaUser().getFlatDetails().getFlatNumber());
        holder.imageActionTaken.setVisibility(View.VISIBLE);
        if (societyServiceNotification.getSocietyServiceResponse().equals(Constants.FIREBASE_CHILD_ACCEPTED))
            holder.imageActionTaken.setImageResource(R.drawable.accepted);
        else
            holder.imageActionTaken.setImageResource(R.drawable.rejected);
    }

    @Override
    public int getItemCount() {
        return requestDetailsList.size();
    }

    /* ------------------------------------------------------------- *
     * History Holder class
     * ------------------------------------------------------------- */

    class PlumberServicesHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textResidentName;
        private final TextView textApartment;
        private final TextView textFlatNumber;
        private final TextView textServiceType;
        private final TextView textTimeSlot;
        private final TextView textProblemDescription;
        private final TextView textResidentNameValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNumberValue;
        private final TextView textServiceTypeValue;
        private final TextView textTimeSlotValue;
        private final TextView textProblemDescriptionValue;
        private final ImageView imageActionTaken;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        PlumberServicesHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textServiceType = itemView.findViewById(R.id.textServiceType);
            textTimeSlot = itemView.findViewById(R.id.textTimeSlot);
            textProblemDescription = itemView.findViewById(R.id.textProblemDescription);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textServiceTypeValue = itemView.findViewById(R.id.textServiceTypeValue);
            textTimeSlotValue = itemView.findViewById(R.id.textTimeSlotValue);
            textProblemDescriptionValue = itemView.findViewById(R.id.textProblemDescriptionValue);
            imageActionTaken = itemView.findViewById(R.id.imageActionTaken);

            /*Setting font for all the views*/
            textResidentName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textTimeSlot.setTypeface(Constants.setLatoRegularFont(mCtx));
            textProblemDescription.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textTimeSlotValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textProblemDescriptionValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }
}
