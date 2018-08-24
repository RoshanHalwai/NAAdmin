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

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public StaffAdapter(Context context) {
        this.context = context;
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
        /*TODO:HardCoded Data will be replaced when we actually retrieve data from firebase.*/
        holder.textStaffName.setText("Avinash");
        holder.textStaffName.setTypeface(Constants.setLatoRegularFont(context));
        holder.textStaffType.setText(R.string.guard);
        holder.textStaffType.setTypeface(Constants.setLatoRegularFont(context));
        holder.textStaffMobileNumber.setText("9885665744");
        holder.textStaffMobileNumber.setTypeface(Constants.setLatoRegularFont(context));
        holder.textStaffRemove.setText("Remove");
        holder.textStaffMobileNumber.setTypeface(Constants.setLatoRegularFont(context));
        holder.staffProfilePic.setImageResource(R.drawable.security_guard);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class StaffViewHolder extends RecyclerView.ViewHolder {
        final TextView textStaffName;
        final TextView textStaffType;
        final TextView textStaffMobileNumber;
        final TextView textStaffRemove;
        final ImageView staffProfilePic;
        private final Context context;

        StaffViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            staffProfilePic = itemView.findViewById(R.id.staffProfilePic);
            textStaffName = itemView.findViewById(R.id.textStaffName);
            textStaffType = itemView.findViewById(R.id.textStaffType);
            textStaffMobileNumber = itemView.findViewById(R.id.textStaffMobileNumber);
            textStaffRemove = itemView.findViewById(R.id.textStaffRemove);
        }
    }
}
