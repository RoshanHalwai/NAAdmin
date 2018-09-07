package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.pojo.HelpDeskPojo;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class HelpDeskAdapter extends RecyclerView.Adapter<HelpDeskAdapter.HelpDeskViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<HelpDeskPojo> helpDeskList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public HelpDeskAdapter(List<HelpDeskPojo> helpDeskList, Context mCtx) {
        this.mCtx = mCtx;
        this.helpDeskList = helpDeskList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public HelpDeskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_helpdesk, parent, false);
        return new HelpDeskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpDeskViewHolder holder, int position) {
        //TODO: Populate Recycler View with real data in Card View
    }

    @Override
    public int getItemCount() {
        //TODO: Item count to be updated with the size of the list
        return 1;
    }

    /* ------------------------------------------------------------- *
     * Notice Board View Holder class
     * ------------------------------------------------------------- */

    class HelpDeskViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textComplainDate;
        private final TextView textOwnerName;
        private final TextView textComplainCategory;
        private final TextView textComplainDescription;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        HelpDeskViewHolder(View itemView) {
            super(itemView);
            /*Getting id's for all the views on cardview*/
            textComplainDate = itemView.findViewById(R.id.textComplainDate);
            textOwnerName = itemView.findViewById(R.id.textOwnerName);
            textComplainCategory = itemView.findViewById(R.id.textComplainCategory);
            textComplainDescription = itemView.findViewById(R.id.textComplainDescription);

            /*Setting Fonts for all the views on cardview*/
            textComplainDate.setTypeface(setLatoRegularFont(mCtx));
            textOwnerName.setTypeface(setLatoBoldFont(mCtx));
            textComplainCategory.setTypeface(setLatoBoldFont(mCtx));
            textComplainDescription.setTypeface(setLatoRegularFont(mCtx));
        }
    }
}
