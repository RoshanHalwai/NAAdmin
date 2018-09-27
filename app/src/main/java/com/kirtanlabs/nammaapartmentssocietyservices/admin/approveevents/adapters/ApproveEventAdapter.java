package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class ApproveEventAdapter extends RecyclerView.Adapter<ApproveEventAdapter.ApproveEventViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private final List<SocietyServiceNotification> eventsDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ApproveEventAdapter(Context context, List<SocietyServiceNotification> eventsDataList) {
        this.context = context;
        this.eventsDataList = eventsDataList;
    }

    /* ------------------------------------------------------------- *
     * Overriding Recycler View Adapter Object
     * ------------------------------------------------------------- */
    @NonNull
    @Override
    public ApproveEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_approve_events, parent, false);
        return new ApproveEventViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveEventAdapter.ApproveEventViewHolder holder, int position) {
        /*Creating an instance of Society Service Notification class and retrieving the values from Firebase*/
        SocietyServiceNotification societyServiceNotification = eventsDataList.get(position);

        holder.textEventTitleValue.setText(societyServiceNotification.getEventTitle());
        holder.textEventDateValue.setText(societyServiceNotification.getEventDate());
        holder.textEventSlotValue.setText(societyServiceNotification.getTimeSlot());

        /*Retrieving User's Details*/
        new RetrievingNotificationData(context, "").getUserData(societyServiceNotification.getUserUID(), NAUser -> {
            holder.textUserNameValue.setText(NAUser.getPersonalDetails().getFullName());
            holder.textApartmentValue.setText(NAUser.getFlatDetails().getApartmentName());
            holder.textFlatNoValue.setText(NAUser.getFlatDetails().getFlatNumber());
        });
    }

    @Override
    public int getItemCount() {
        return eventsDataList.size();
    }

    /* ------------------------------------------------------------- *
     * Staff View Holder Class
     * ------------------------------------------------------------- */

    class ApproveEventViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textUserName;
        private final TextView textApartmentName;
        private final TextView textFlatNo;
        private final TextView textEventTitle;
        private final TextView textEventDate;
        private final TextView textEventSlot;
        private final TextView textUserNameValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNoValue;
        private final TextView textEventTitleValue;
        private final TextView textEventDateValue;
        private final TextView textEventSlotValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ApproveEventViewHolder(View itemView, Context context) {
            super(itemView);

            /*Getting Id's for all the views*/
            textUserName = itemView.findViewById(R.id.textUserName);
            textApartmentName = itemView.findViewById(R.id.textApartmentName);
            textFlatNo = itemView.findViewById(R.id.textFlatNo);
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventDate = itemView.findViewById(R.id.textEventDate);
            textEventSlot = itemView.findViewById(R.id.textEventSlot);
            textUserNameValue = itemView.findViewById(R.id.textUserNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNoValue = itemView.findViewById(R.id.textFlatNoValue);
            textEventTitleValue = itemView.findViewById(R.id.textEventTitleValue);
            textEventDateValue = itemView.findViewById(R.id.textEventDateValue);
            textEventSlotValue = itemView.findViewById(R.id.textEventSlotValue);

            /*Setting font for all the views*/
            textUserName.setTypeface(setLatoRegularFont(context));
            textApartmentName.setTypeface(setLatoRegularFont(context));
            textFlatNo.setTypeface(setLatoRegularFont(context));
            textEventTitle.setTypeface(setLatoRegularFont(context));
            textEventDate.setTypeface(setLatoRegularFont(context));
            textEventSlot.setTypeface(setLatoRegularFont(context));
            textUserNameValue.setTypeface(setLatoBoldFont(context));
            textApartmentValue.setTypeface(setLatoBoldFont(context));
            textFlatNoValue.setTypeface(setLatoBoldFont(context));
            textEventTitleValue.setTypeface(setLatoBoldFont(context));
            textEventDateValue.setTypeface(setLatoBoldFont(context));
            textEventSlotValue.setTypeface(setLatoBoldFont(context));

        }

    }

}
