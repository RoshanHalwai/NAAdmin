package com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.adapters;

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
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private final List<SocietyServiceData> staffDataList;
    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public StaffAdapter(Context context, List<SocietyServiceData> staffDataList, int screenTitle) {
        this.context = context;
        this.staffDataList = staffDataList;
        this.screenTitle = screenTitle;
    }

    /* ------------------------------------------------------------- *
     * Overriding Recycler View Adapter Object
     * ------------------------------------------------------------- */
    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_staffs, parent, false);
        return new StaffViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffAdapter.StaffViewHolder holder, int position) {
        SocietyServiceData societyServiceData = staffDataList.get(position);
        holder.textStaffName.setText(societyServiceData.getFullName());
        holder.textStaffMobileNumber.setText(societyServiceData.getMobileNumber());

        switch (screenTitle) {
            case R.string.plumbers:
                holder.staffProfilePic.setImageResource(R.drawable.plumber);
                break;
            case R.string.carpenters:
                holder.staffProfilePic.setImageResource(R.drawable.carpenter);
                break;
            case R.string.electricians:
                holder.staffProfilePic.setImageResource(R.drawable.electrician);
                break;
            case R.string.garbageCollectors:
                holder.staffProfilePic.setImageResource(R.drawable.garbage_bag);
                break;
            case R.string.guards:
                holder.staffProfilePic.setImageResource(R.drawable.security_guard);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return staffDataList.size();
    }

    /* ------------------------------------------------------------- *
     * Staff View Holder Class
     * ------------------------------------------------------------- */

    class StaffViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textStaffName;
        private final TextView textStaffMobileNumber;
        private final ImageView staffProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        StaffViewHolder(View itemView, Context context) {
            super(itemView);

            /*Getting Id's for all the views*/
            staffProfilePic = itemView.findViewById(R.id.staffProfilePic);
            textStaffName = itemView.findViewById(R.id.textStaffName);
            textStaffMobileNumber = itemView.findViewById(R.id.textStaffMobileNumber);

            /*Setting font for all the views*/
            textStaffName.setTypeface(Constants.setLatoBoldFont(context));
            textStaffMobileNumber.setTypeface(Constants.setLatoBoldFont(context));
        }
    }
}
