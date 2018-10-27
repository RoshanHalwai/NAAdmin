package com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.staffs.activities.EditStaffActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.GATE_NUMBER_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NAME_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PROFILE_PIC_OLD_CONTENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_UID;

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
                holder.progressIndicator.smoothToShow();
                holder.progressIndicator.setVisibility(View.VISIBLE);
                Glide.with(context.getApplicationContext()).load(societyServiceData.getProfilePhoto())
                        .into(holder.staffProfilePic);
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

    class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textStaffName;
        private final TextView textStaffMobileNumber;
        private final ImageView staffProfilePic, imageEditStaff;
        private final AVLoadingIndicatorView progressIndicator;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        StaffViewHolder(View itemView, Context context) {
            super(itemView);

            /*Getting Id's for all the views*/
            staffProfilePic = itemView.findViewById(R.id.staffProfilePic);
            textStaffName = itemView.findViewById(R.id.textStaffName);
            textStaffMobileNumber = itemView.findViewById(R.id.textStaffMobileNumber);
            imageEditStaff = itemView.findViewById(R.id.imageEditStaff);
            progressIndicator = itemView.findViewById(R.id.progressIndicator);

            /*Setting font for all the views*/
            textStaffName.setTypeface(Constants.setLatoBoldFont(context));
            textStaffMobileNumber.setTypeface(Constants.setLatoBoldFont(context));

            /*Setting events for items in card view*/
            imageEditStaff.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners Methods
         * ------------------------------------------------------------- */
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            SocietyServiceData societyServiceData = staffDataList.get(position);
            switch (v.getId()) {
                case R.id.imageEditStaff:
                    Intent editStaffIntent = new Intent(context, EditStaffActivity.class);
                    editStaffIntent.putExtra(NAME_OLD_CONTENT, societyServiceData.getFullName());
                    editStaffIntent.putExtra(SCREEN_TITLE, screenTitle);
                    editStaffIntent.putExtra(SOCIETY_SERVICE_UID, societyServiceData.getUid());
                    if (screenTitle == R.string.guards) {
                        editStaffIntent.putExtra(PROFILE_PIC_OLD_CONTENT, societyServiceData.getProfilePhoto());
                        editStaffIntent.putExtra(GATE_NUMBER_OLD_CONTENT, societyServiceData.getGateNumber());
                    }
                    context.startActivity(editStaffIntent);
                    break;
            }
        }
    }
}
