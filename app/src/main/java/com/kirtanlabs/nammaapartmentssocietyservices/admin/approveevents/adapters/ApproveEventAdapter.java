package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class ApproveEventAdapter extends RecyclerView.Adapter<ApproveEventAdapter.ApproveEventViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private final BaseActivity baseActivity;
    private List<SocietyServiceNotification> eventsDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ApproveEventAdapter(Context context, List<SocietyServiceNotification> eventsDataList) {
        this.context = context;
        baseActivity = (BaseActivity) context;
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

    /**
     * This method is invoked to update to the status the User's Request for Event Management
     *
     * @param position of the cardView
     * @param response to the request that user has made for Event.
     */
    private void responseToUserEventRequest(int position, String response) {
        SocietyServiceNotification societyServiceNotification = eventsDataList.get(position);
        String notificationUID = societyServiceNotification.getNotificationUID();
        String eventDate = societyServiceNotification.getEventDate();
        String eventTimeSlot = societyServiceNotification.getTimeSlot();

        /*Setting EventManagement Notification UID to false in firebase(societyServicesNotification->eventManagement->NotificationUID)
         whenever Admin responds to that request*/
        DatabaseReference eventManagementNotificationReference = Constants.EVENT_MANAGEMENT_NOTIFICATION_REFERENCE
                .child(notificationUID);
        eventManagementNotificationReference.setValue(false);

        DatabaseReference eventsNotificationStatusReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                .child(notificationUID).child(Constants.FIREBASE_CHILD_STATUS);
        /*Updating User's Event Request status*/
        eventsNotificationStatusReference.setValue(response).addOnSuccessListener(aVoid -> {
            /* Removing User's Event request data from the list, once Admin responds to that request*/
            eventsDataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventsDataList.size());
        });

        /*Removing Time slot of particular event date from firebase if admin rejects user's event request*/
        if (response.equals(context.getString(R.string.booking_rejected))) {
            removeBookedTimeSlot(eventDate, eventTimeSlot);
        }
    }

    /**
     * This method is used to remove time slot which was booked for particular event date
     *
     * @param date     of event
     * @param timeSlot of booked for event
     */
    private void removeBookedTimeSlot(String date, String timeSlot) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        Date formattedDate = null;
        try {
            formattedDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date = simpleDateFormat.format(formattedDate);

        switch (timeSlot) {
            case Constants.MORNING:
                timeSlot = context.getString(R.string.slot_one);
                break;
            case Constants.NOON:
                timeSlot = context.getString(R.string.slot_two);
                break;
            case Constants.EVENING:
                timeSlot = context.getString(R.string.slot_three);
                break;
            case Constants.NIGHT:
                timeSlot = context.getString(R.string.slot_four);
                break;
        }
        /*Removing Time Slot from (eventManagement->eventDate->eventTimeSlot) in firebase*/
        DatabaseReference eventTimeSlotReference = Constants.EVENT_MANAGEMENT_TIME_SLOT_REFERENCE
                .child(date)
                .child(timeSlot);
        eventTimeSlotReference.removeValue();
    }

    class ApproveEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        private final Button buttonAccept;
        private final Button buttonReject;

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
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonReject = itemView.findViewById(R.id.buttonReject);

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
            buttonAccept.setTypeface(setLatoLightFont(context));
            buttonReject.setTypeface(setLatoLightFont(context));

            /*Setting event for views*/
            buttonAccept.setOnClickListener(this);
            buttonReject.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding On Click Method
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            String eventRequestMessage = "";
            switch (v.getId()) {
                case R.id.buttonAccept:
                    responseToUserEventRequest(position, context.getString(R.string.booking_accepted));
                    eventRequestMessage = context.getString(R.string.event_request_accepted_message);
                    break;
                case R.id.buttonReject:
                    responseToUserEventRequest(position, context.getString(R.string.booking_rejected));
                    eventRequestMessage = context.getString(R.string.event_request_rejected_message);
                    break;
            }
            baseActivity.showNotificationDialog(context.getString(R.string.event_request_title), eventRequestMessage,
                    null);
        }
    }

}
