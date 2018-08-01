package com.kirtanlabs.nammaapartmentssocietyservices.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class PlumberServicesAdapter extends RecyclerView.Adapter<PlumberServicesAdapter.PlumberServicesHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final BaseActivity baseActivity;
    private Context mCtx;
    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public PlumberServicesAdapter(Context mCtx, int screenTitle) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.screenTitle = screenTitle;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public PlumberServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_resident_details, parent, false);
        return new PlumberServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlumberServicesHolder holder, int position) {
        if (screenTitle == R.string.history) {
            holder.buttonCallResident.setVisibility(View.GONE);
            holder.imageActionTaken.setVisibility(View.VISIBLE);

            //TODO: To remove this switch statement from here
            switch (position) {
                case 0:
                    holder.imageActionTaken.setImageResource(R.drawable.rejected);
                    break;
                case 1:
                    holder.imageActionTaken.setImageResource(R.drawable.accepted);
                    break;
                case 2:
                    holder.imageActionTaken.setImageResource(R.drawable.rejected);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        //TODO: To change item count here
        return 3;
    }

    /* ------------------------------------------------------------- *
     * History Holder class
     * ------------------------------------------------------------- */

    class PlumberServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textResidentName;
        private TextView textApartment;
        private TextView textFlatNumber;
        private TextView textDate;
        private TextView textProblemDescription;
        private TextView textResidentNameValue;
        private TextView textApartmentValue;
        private TextView textFlatNumberValue;
        private TextView textDateValue;
        private TextView textProblemDescriptionValue;
        private Button buttonCallResident;
        private ImageView imageActionTaken;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        PlumberServicesHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textDate = itemView.findViewById(R.id.textTimeSlot);
            textProblemDescription = itemView.findViewById(R.id.textProblemDescription);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textDateValue = itemView.findViewById(R.id.textTimeSlotValue);
            textProblemDescriptionValue = itemView.findViewById(R.id.textProblemDescriptionValue);
            buttonCallResident = itemView.findViewById(R.id.buttonCallResident);
            imageActionTaken = itemView.findViewById(R.id.imageActionTaken);

            /*Setting font for all the views*/
            textResidentName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textDate.setTypeface(Constants.setLatoRegularFont(mCtx));
            textProblemDescription.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textDateValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textProblemDescriptionValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonCallResident.setTypeface(Constants.setLatoLightFont(mCtx));

            //Setting listener for item in card view
            buttonCallResident.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            baseActivity.makePhoneCall();
        }
    }
}
