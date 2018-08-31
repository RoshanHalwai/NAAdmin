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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.ManageUsers;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.ApprovedUsersFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.UserFlatDetails;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_VERIFIED_APPROVED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_VERIFIED_DECLINED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldItalicFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.ManageUserHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private BaseActivity baseActivity;
    private int userType;
    private List<NAUser> usersList;
    private DatabaseReference userReference;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ManageUsersAdapter(Context mCtx, int userType, List<NAUser> usersList) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.userType = userType;
        this.usersList = usersList;
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
        /*Creating an instance of NammaApartments User class and retrieving the values from Firebase*/
        NAUser naUser = usersList.get(position);

        Glide.with(mCtx.getApplicationContext()).load(naUser.getPersonalDetails().getProfilePhoto()).into(holder.usersProfilePic);
        holder.textUserNameValue.setText(naUser.getPersonalDetails().getFullName());
        holder.textUserApartmentNameValue.setText(naUser.getFlatDetails().getApartmentName());
        holder.textUserFlatNumberValue.setText(naUser.getFlatDetails().getFlatNumber());
        holder.textUserMobileNumberValue.setText(naUser.getPersonalDetails().getPhoneNumber());

        switch (userType) {
            case R.string.approved_users:
                holder.layoutIcons.setVisibility(View.VISIBLE);
                break;
            case R.string.unapproved_users:
                holder.layoutButtons.setVisibility(View.VISIBLE);
                break;
            case R.string.removed_users:
                holder.line.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to Approve the Users, by changing its verified value to 'true'
     * and Update Approved Users and Unapproved Users Tab's List.
     *
     * @param position of card view
     */
    private void approveUsers(int position) {
        NAUser naUser = usersList.get(position);
        userReference = Constants.PRIVATE_USERS_REFERENCE
                .child(naUser.getUID())
                .child(Constants.FIREBASE_CHILD_PRIVILEGES)
                .child(Constants.FIREBASE_CHILD_VERIFIED);

        /*Setting User's verified value to 1 (i.e User has been verified)*/
        userReference.setValue(FIREBASE_CHILD_VERIFIED_APPROVED).addOnSuccessListener(aVoid -> {
            /*Updating Unapproved Users List*/
            usersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, usersList.size());

            /*Getting Tag of Approved Users Fragment*/
            String approvedUsersFragmentTag = ((ManageUsers) mCtx).getApprovedUsersFragmentTag();
            ApprovedUsersFragment approvedUsersFragment = (ApprovedUsersFragment) ((ManageUsers) mCtx).getSupportFragmentManager()
                    .findFragmentByTag(approvedUsersFragmentTag);

            /*Updating Approved Users List*/
            approvedUsersFragment.retrieveApprovedUserDetails();
        });
    }

    /**
     * This method is invoked to Decline the Users, by changing its verified value to 'declined'
     * and Update Unapproved Users Tab's List.
     *
     * @param position of card view.
     */
    private void declineUsers(int position) {
        NAUser naUser = usersList.get(position);
        UserFlatDetails userFlatDetails = naUser.getFlatDetails();
        DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                .child(userFlatDetails.getCity())
                .child(userFlatDetails.getSocietyName())
                .child(userFlatDetails.getApartmentName())
                .child(userFlatDetails.getFlatNumber());
        userDataReference.removeValue();

        userReference = Constants.PRIVATE_USERS_REFERENCE
                .child(naUser.getUID())
                .child(Constants.FIREBASE_CHILD_PRIVILEGES)
                .child(Constants.FIREBASE_CHILD_VERIFIED);
        /*Setting User's verified value to 2 (i.e User has been declined)*/
        userReference.setValue(FIREBASE_CHILD_VERIFIED_DECLINED);

        usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, usersList.size());

    }

    /* ------------------------------------------------------------- *
     * Manage User View Holder class
     * ------------------------------------------------------------- */

    class ManageUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private CircleImageView usersProfilePic;
        private TextView textUserName;
        private TextView textUserApartmentName;
        private TextView textTextUserFlatNumber;
        private TextView textUserMobileNumber;
        private TextView textUserNameValue;
        private TextView textUserApartmentNameValue;
        private TextView textUserFlatNumberValue;
        private TextView textUserMobileNumberValue;
        private TextView textCall;
        private TextView textMessage;
        private TextView textEmail;
        private TextView textRemove;
        private LinearLayout layoutIcons;
        private LinearLayout layoutButtons;
        private View line;
        private Button buttonApproveUser;
        private Button buttonDeclineUser;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ManageUserHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            usersProfilePic = itemView.findViewById(R.id.usersProfilePic);
            textUserName = itemView.findViewById(R.id.textUserName);
            textUserApartmentName = itemView.findViewById(R.id.textUserApartmentName);
            textTextUserFlatNumber = itemView.findViewById(R.id.textTextUserFlatNumber);
            textUserMobileNumber = itemView.findViewById(R.id.textUserMobileNumber);
            textUserNameValue = itemView.findViewById(R.id.textUserNameValue);
            textUserApartmentNameValue = itemView.findViewById(R.id.textUserApartmentNameValue);
            textUserFlatNumberValue = itemView.findViewById(R.id.textUserFlatNumberValue);
            textUserMobileNumberValue = itemView.findViewById(R.id.textUserMobileNumberValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEmail = itemView.findViewById(R.id.textEmail);
            textRemove = itemView.findViewById(R.id.textRemove);
            layoutIcons = itemView.findViewById(R.id.layoutIcons);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
            line = itemView.findViewById(R.id.line);
            buttonApproveUser = itemView.findViewById(R.id.buttonApproveUser);
            buttonDeclineUser = itemView.findViewById(R.id.buttonDeclineUser);

            /*Setting fonts to the views*/
            textUserName.setTypeface(setLatoRegularFont(mCtx));
            textUserApartmentName.setTypeface(setLatoRegularFont(mCtx));
            textTextUserFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textUserMobileNumber.setTypeface(setLatoRegularFont(mCtx));
            textUserNameValue.setTypeface(setLatoBoldFont(mCtx));
            textUserApartmentNameValue.setTypeface(setLatoBoldFont(mCtx));
            textUserFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textUserMobileNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(setLatoBoldItalicFont(mCtx));
            textEmail.setTypeface(setLatoBoldItalicFont(mCtx));
            textRemove.setTypeface(setLatoBoldItalicFont(mCtx));
            buttonApproveUser.setTypeface(setLatoLightFont(mCtx));
            buttonDeclineUser.setTypeface(setLatoLightFont(mCtx));

            /*Setting onClickListener for view*/
            buttonApproveUser.setOnClickListener(this);
            buttonDeclineUser.setOnClickListener(this);
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEmail.setOnClickListener(this);

        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NAUser naUser = usersList.get(position);
            String userMobileNumber = naUser.getPersonalDetails().getPhoneNumber();
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(userMobileNumber);
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(userMobileNumber);
                    break;
                case R.id.textEmail:
                    baseActivity.sendEmail(naUser.getPersonalDetails().getEmail());
                    break;
                case R.id.buttonApproveUser:
                    approveUsers(position);
                    baseActivity.showNotificationDialog(mCtx.getString(R.string.approve_user_title),
                            mCtx.getString(R.string.approve_user_message),
                            null);
                    break;
                case R.id.buttonDeclineUser:
                    declineUsers(position);
                    baseActivity.showNotificationDialog(mCtx.getString(R.string.decline_user_title),
                            mCtx.getString(R.string.decline_user_message),
                            null);
                    break;
            }
        }
    }

}
