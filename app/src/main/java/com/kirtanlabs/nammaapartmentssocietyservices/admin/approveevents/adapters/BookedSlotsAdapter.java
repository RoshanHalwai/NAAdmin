package com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kirtanlabs.nammaapartmentssocietyservices.R;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/03/2018
 */
public class BookedSlotsAdapter extends RecyclerView.Adapter<BookedSlotsAdapter.BookedSlotsHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<String> bookedSlots;
    private Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    BookedSlotsAdapter(Context mCtx, List<String> bookedSlots) {
        this.bookedSlots = bookedSlots;
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter API's
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public BookedSlotsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_booked_slots, parent, false);
        return new BookedSlotsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedSlotsHolder holder, int position) {
        holder.buttonBookedSlots.setText(bookedSlots.get(position));
    }

    @Override
    public int getItemCount() {
        return bookedSlots.size();
    }

    /* ------------------------------------------------------------- *
     * View Holder Class
     * ------------------------------------------------------------- */

    class BookedSlotsHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final Button buttonBookedSlots;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        BookedSlotsHolder(View itemView) {
            super(itemView);
            /*Getting Id's for the view*/
            buttonBookedSlots = itemView.findViewById(R.id.buttonBookedSlots);

            /*Setting font for the view*/
            buttonBookedSlots.setTypeface(setLatoBoldFont(mCtx));
        }
    }
}
