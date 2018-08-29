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

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_CARPENTER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ELECTRICIAN;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GARBAGE_MANAGEMENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_GUARD;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PLUMBER;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private List<SocietyServiceData> staffDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public StaffAdapter(Context context, List<SocietyServiceData> staffDataList) {
        this.context = context;
        this.staffDataList = staffDataList;
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
        String serviceType = societyServiceData.getSocietyServiceType();
        holder.textStaffName.setText(societyServiceData.getFullName());
        holder.textStaffMobileNumber.setText(societyServiceData.getMobileNumber());

        switch (serviceType) {
            case FIREBASE_CHILD_PLUMBER:
                holder.staffProfilePic.setImageResource(R.drawable.plumber);
                break;
            case FIREBASE_CHILD_CARPENTER:
                holder.staffProfilePic.setImageResource(R.drawable.carpenter);
                break;
            case FIREBASE_CHILD_ELECTRICIAN:
                holder.staffProfilePic.setImageResource(R.drawable.electrician);
                break;
            case FIREBASE_CHILD_GARBAGE_MANAGEMENT:
                holder.staffProfilePic.setImageResource(R.drawable.garbage_bag);
                break;
            case FIREBASE_CHILD_GUARD:
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
        private final TextView textStaffRemove;
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
            textStaffRemove = itemView.findViewById(R.id.textStaffRemove);

            /*Setting font for all the views*/
            textStaffName.setTypeface(Constants.setLatoBoldFont(context));
            textStaffMobileNumber.setTypeface(Constants.setLatoBoldFont(context));
            textStaffRemove.setTypeface(Constants.setLatoLightFont(context));
        }
    }
}
