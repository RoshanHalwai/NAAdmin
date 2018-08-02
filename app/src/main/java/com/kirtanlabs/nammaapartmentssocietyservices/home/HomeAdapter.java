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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.capitalizeString;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PlumberServicesHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final BaseActivity baseActivity;
    private Context mCtx;
    private List<SocietyServiceNotification> requestDetailsList;
    private int screenTitle;
    private String userUid;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public HomeAdapter(Context mCtx, List<SocietyServiceNotification> requestDetailsList, int screenTitle) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.requestDetailsList = requestDetailsList;
        this.screenTitle = screenTitle;
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
        userUid = societyServiceNotification.getUserUID();
        holder.textServiceTypeValue.setText(capitalizeString(societyServiceNotification.getSocietyServiceType()));
        holder.textTimeSlotValue.setText(societyServiceNotification.getTimeSlot());
        holder.textProblemDescriptionValue.setText(societyServiceNotification.getProblem());

        /*To retrieve of user details from firebase*/
        getUserDetails(holder.textResidentNameValue, holder.textApartmentValue, holder.textFlatNumberValue);

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
        return requestDetailsList.size();
    }

    /**
     * This method is invoked to retrieve details of user who has requested for society services
     *
     * @param textResidentNameValue - to display user name in this view
     * @param textApartmentValue-   to display  user apartment name in this view
     * @param textFlatNumberValue-  to display user flat number in this view
     */
    private void getUserDetails(final TextView textResidentNameValue, final TextView textApartmentValue, final TextView textFlatNumberValue) {
        /*Getting user details from (users->private->userUID) in firebase*/
        DatabaseReference userReference = Constants.PRIVATE_USERS_REFERENCE
                .child(userUid);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NAUser naUser = dataSnapshot.getValue(NAUser.class);
                assert naUser != null;
                String residentName = naUser.getPersonalDetails().getFullName();
                String apartment = naUser.getFlatDetails().getApartmentName();
                String flatNumber = naUser.getFlatDetails().getFlatNumber();
                textResidentNameValue.setText(residentName);
                textApartmentValue.setText(apartment);
                textFlatNumberValue.setText(flatNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * HistoryFragment Holder class
     * ------------------------------------------------------------- */

    class PlumberServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textResidentName;
        private TextView textApartment;
        private TextView textFlatNumber;
        private TextView textServiceType;
        private TextView textTimeSlot;
        private TextView textProblemDescription;
        private TextView textResidentNameValue;
        private TextView textApartmentValue;
        private TextView textFlatNumberValue;
        private TextView textServiceTypeValue;
        private TextView textTimeSlotValue;
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
            textServiceType = itemView.findViewById(R.id.textServiceType);
            textTimeSlot = itemView.findViewById(R.id.textTimeSlot);
            textProblemDescription = itemView.findViewById(R.id.textProblemDescription);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textServiceTypeValue = itemView.findViewById(R.id.textServiceTypeValue);
            textTimeSlotValue = itemView.findViewById(R.id.textTimeSlotValue);
            textProblemDescriptionValue = itemView.findViewById(R.id.textProblemDescriptionValue);
            buttonCallResident = itemView.findViewById(R.id.buttonCallResident);
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
