package com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldItalicFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.ManageUserHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private int userType;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ManageUsersAdapter(Context mCtx, int userType) {
        this.mCtx = mCtx;
        this.userType = userType;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ManageUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_manage_users, parent, false);
        return new ManageUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUserHolder holder, int position) {
        switch (userType) {
            case R.string.approved_users:
                holder.layoutIcons.setVisibility(View.VISIBLE);
                break;
            case R.string.unapproved_users:
                holder.buttonApproveUser.setVisibility(View.VISIBLE);
                break;
            case R.string.removed_users:
                holder.line.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        //TODO: To change item count here
        return 5;
    }

    /* ------------------------------------------------------------- *
     * Manage User View Holder class
     * ------------------------------------------------------------- */

    class ManageUserHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textUserName;
        private TextView textUserApartmentName;
        private TextView textTextUserFlatNumber;
        private TextView textUserMobileNumber;
        private TextView textUserNameValue;
        private TextView textUserApartmentNameValue;
        private TextView textTextUserFlatNumberValue;
        private TextView textUserMobileNumberValue;
        private TextView textCall;
        private TextView textMessage;
        private TextView textEmail;
        private TextView textRemove;
        private LinearLayout layoutIcons;
        private View line;
        private Button buttonApproveUser;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ManageUserHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textUserName = itemView.findViewById(R.id.textUserName);
            textUserApartmentName = itemView.findViewById(R.id.textUserApartmentName);
            textTextUserFlatNumber = itemView.findViewById(R.id.textTextUserFlatNumber);
            textUserMobileNumber = itemView.findViewById(R.id.textUserMobileNumber);
            textUserNameValue = itemView.findViewById(R.id.textUserNameValue);
            textUserApartmentNameValue = itemView.findViewById(R.id.textUserApartmentNameValue);
            textTextUserFlatNumberValue = itemView.findViewById(R.id.textTextUserFlatNumberValue);
            textUserMobileNumberValue = itemView.findViewById(R.id.textUserMobileNumberValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEmail = itemView.findViewById(R.id.textEmail);
            textRemove = itemView.findViewById(R.id.textRemove);
            layoutIcons = itemView.findViewById(R.id.layoutIcons);
            line = itemView.findViewById(R.id.line);
            buttonApproveUser = itemView.findViewById(R.id.buttonApproveUser);

            /*Setting fonts to the views*/
            textUserName.setTypeface(setLatoRegularFont(mCtx));
            textUserApartmentName.setTypeface(setLatoRegularFont(mCtx));
            textTextUserFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textUserMobileNumber.setTypeface(setLatoRegularFont(mCtx));
            textUserNameValue.setTypeface(setLatoBoldFont(mCtx));
            textUserApartmentNameValue.setTypeface(setLatoBoldFont(mCtx));
            textTextUserFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textUserMobileNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(setLatoBoldItalicFont(mCtx));
            textEmail.setTypeface(setLatoBoldItalicFont(mCtx));
            textRemove.setTypeface(setLatoBoldItalicFont(mCtx));
            buttonApproveUser.setTypeface(setLatoLightFont(mCtx));
        }
    }
}
