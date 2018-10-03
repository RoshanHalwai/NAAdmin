package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.timeline.RetrievingNotificationData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.TIME_SLOT_FULL_DAY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class ApproveEventAdapter extends RecyclerView.Adapter<ApproveEventAdapter.ApproveEventViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private final List<SocietyServiceNotification> eventsDataList;
    private Set<String> timeSlotsSet = new LinkedHashSet<>();

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ApproveEventAdapter(Context context, List<SocietyServiceNotification> eventsDataList) {
        this.context = context;
        this.eventsDataList = eventsDataList;
        initializeMap();
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
        holder.recyclerViewBookedSlots.setAdapter(new BookedSlotsAdapter(context, getBookedSlots(societyServiceNotification)));

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
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to initialize the list of all the time slots for event in timeSlotsSet Set.
     */
    private void initializeMap() {
        timeSlotsSet.add(context.getString(R.string.time_slot_one));
        timeSlotsSet.add(context.getString(R.string.time_slot_two));
        timeSlotsSet.add(context.getString(R.string.time_slot_three));
        timeSlotsSet.add(context.getString(R.string.time_slot_four));
        timeSlotsSet.add(context.getString(R.string.time_slot_five));
        timeSlotsSet.add(context.getString(R.string.time_slot_six));
        timeSlotsSet.add(context.getString(R.string.time_slot_seven));
        timeSlotsSet.add(context.getString(R.string.time_slot_eight));
        timeSlotsSet.add(context.getString(R.string.time_slot_nine));
        timeSlotsSet.add(context.getString(R.string.time_slot_ten));
        timeSlotsSet.add(context.getString(R.string.time_slot_eleven));
        timeSlotsSet.add(context.getString(R.string.time_slot_twelve));
        timeSlotsSet.add(context.getString(R.string.time_slot_thirteen));
        timeSlotsSet.add(context.getString(R.string.time_slot_fourteen));
        timeSlotsSet.add(TIME_SLOT_FULL_DAY);
    }

    /**
     * Returns a sorted list of time slots selected by the user.
     *
     * @param societyServiceNotification to get unsorted time slots selected by user for the event
     * @return sorted list of time slots selected by the user for the event
     */
    private List<String> getBookedSlots(final SocietyServiceNotification societyServiceNotification) {
        Set<String> bookedSlotsSet = societyServiceNotification.getTimeSlots().keySet();
        Set<String> bookedSlotsSetRef = new LinkedHashSet<>(timeSlotsSet);
        if (bookedSlotsSet.contains(TIME_SLOT_FULL_DAY)) {
            bookedSlotsSetRef.clear();
            bookedSlotsSetRef.add(context.getString(R.string.full_day));
        } else {
            for (String slot : timeSlotsSet) {
                if (!bookedSlotsSet.contains(slot)) {
                    bookedSlotsSetRef.remove(slot);
                }
            }
        }
        return new LinkedList<>(bookedSlotsSetRef);
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
        private final TextView textBookedSlots;
        private final TextView textUserNameValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNoValue;
        private final TextView textEventTitleValue;
        private final TextView textEventDateValue;
        private final RecyclerView recyclerViewBookedSlots;

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
            textBookedSlots = itemView.findViewById(R.id.textBookedSlots);
            textUserNameValue = itemView.findViewById(R.id.textUserNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNoValue = itemView.findViewById(R.id.textFlatNoValue);
            textEventTitleValue = itemView.findViewById(R.id.textEventTitleValue);
            textEventDateValue = itemView.findViewById(R.id.textEventDateValue);
            recyclerViewBookedSlots = itemView.findViewById(R.id.recyclerViewBookedSlots);
            recyclerViewBookedSlots.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            /*Setting font for all the views*/
            textUserName.setTypeface(setLatoRegularFont(context));
            textApartmentName.setTypeface(setLatoRegularFont(context));
            textFlatNo.setTypeface(setLatoRegularFont(context));
            textEventTitle.setTypeface(setLatoRegularFont(context));
            textEventDate.setTypeface(setLatoRegularFont(context));
            textBookedSlots.setTypeface(setLatoRegularFont(context));
            textUserNameValue.setTypeface(setLatoBoldFont(context));
            textApartmentValue.setTypeface(setLatoBoldFont(context));
            textFlatNoValue.setTypeface(setLatoBoldFont(context));
            textEventTitleValue.setTypeface(setLatoBoldFont(context));
            textEventDateValue.setTypeface(setLatoBoldFont(context));

        }

    }

}
