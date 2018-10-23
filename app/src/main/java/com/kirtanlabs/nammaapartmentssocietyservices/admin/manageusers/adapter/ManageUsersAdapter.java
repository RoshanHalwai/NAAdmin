package com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.ManageUsers;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.fragments.ApprovedUsersFragment;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.UserFlatDetails;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PENDING_DUES;
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

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final int userType;
    private final List<NAUser> usersList;
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
        long timestamp = naUser.getTimestamp();

        /*Decoding Time Stamp to Date*/
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("MMM dd, yyyy", calendar).toString();
        holder.textUserCreatedDateValue.setText(date);

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
     * This method is invoked to Approve the Users, by changing its verified value to '1'
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
     * This method is invoked to Decline the Users, by changing its verified value to '2'
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

    /**
     * This method is invoked to display dialog box where it ask Society Admin to add maintenance cost
     * for user.
     *
     * @param position of the user in the list
     */
    private void showMaintenanceCostDialog(int position) {
        View maintenanceCostDialog = View.inflate(mCtx, R.layout.layout_maintenance_cost_dialog, null);

        /*Getting Id's for all the views*/
        TextView textMaintenanceCostDescription = maintenanceCostDialog.findViewById(R.id.textMaintenanceCostDescription);
        TextView textIndianRupeeSymbol = maintenanceCostDialog.findViewById(R.id.textIndianRupeeSymbol);
        EditText editMaintenanceCost = maintenanceCostDialog.findViewById(R.id.editMaintenanceCost);
        Button buttonAddMaintenanceCost = maintenanceCostDialog.findViewById(R.id.buttonAddMaintenanceCost);

        /*Setting font for all the views*/
        textMaintenanceCostDescription.setTypeface(setLatoBoldFont(mCtx));
        textIndianRupeeSymbol.setTypeface(setLatoBoldFont(mCtx));
        editMaintenanceCost.setTypeface(setLatoRegularFont(mCtx));
        buttonAddMaintenanceCost.setTypeface(setLatoLightFont(mCtx));

        AlertDialog.Builder alertValidationDialog = new AlertDialog.Builder(mCtx);
        alertValidationDialog.setView(maintenanceCostDialog);
        AlertDialog dialog = alertValidationDialog.create();

        new Dialog(mCtx);
        dialog.show();

        /*Setting On Click Listener for view*/
        buttonAddMaintenanceCost.setOnClickListener(v -> {
            if (baseActivity.isAllFieldsFilled(new EditText[]{editMaintenanceCost})) {
                String maintenanceCost = editMaintenanceCost.getText().toString().trim();

                NAUser naUser = usersList.get(position);
                UserFlatDetails userFlatDetails = naUser.getFlatDetails();
                DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber())
                        .child(Constants.FIREBASE_CHILD_MAINTENANCE_COST);

                /*Setting Maintenance Cost for User in (userData->userCity->userSociety->userApartment->userFlat->maintenanceCost) in firebase*/
                userDataReference.setValue(Integer.parseInt(maintenanceCost)).addOnSuccessListener(aVoid -> {
                    approveUsers(position);
                    baseActivity.showNotificationDialog(mCtx.getString(R.string.approve_user_title),
                            mCtx.getString(R.string.approve_user_message),
                            null);
                    dialog.cancel();
                });
            } else {
                editMaintenanceCost.setError(mCtx.getText(R.string.please_enter_amount));
            }
        });
    }

    private void showPendingDuesDialog(final UserFlatDetails userFlatDetails) {
        baseActivity.showProgressDialog(mCtx, "Getting Pending Dues", mCtx.getString(R.string.please_wait_a_moment));
        getPendingDues(userFlatDetails, numberOfMonths -> {
            baseActivity.hideProgressDialog();
            if (numberOfMonths == 0) {
                baseActivity.showNotificationDialog("Pending Dues", "User does not have any Pending dues.", null);
            } else {
                final String months = (numberOfMonths == 1) ? "month." : "months.";
                baseActivity.showNotificationDialog("Pending Dues", "User has not paid maintenance for" + " " + numberOfMonths + " " + months, null);
            }
        });
    }

    private void getPendingDues(final UserFlatDetails userFlatDetails, final PendingDues pendingDues) {
        final String city = userFlatDetails.getCity();
        final String society = userFlatDetails.getSocietyName();
        final String apartment = userFlatDetails.getApartmentName();
        final String flat = userFlatDetails.getFlatNumber();
        DatabaseReference userPrivateData = PRIVATE_USER_DATA_REFERENCE.child(city)
                .child(society).child(apartment).child(flat).child(FIREBASE_CHILD_PENDING_DUES);
        userPrivateData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pendingDues.onCallback(dataSnapshot.getChildrenCount());
                } else {
                    pendingDues.onCallback(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private interface PendingDues {
        void onCallback(long numberOfMonths);
    }

    /* ------------------------------------------------------------- *
     * Manage User View Holder class
     * ------------------------------------------------------------- */

    class ManageUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final CircleImageView usersProfilePic;
        private final TextView textUserName;
        private final TextView textUserApartmentName;
        private final TextView textTextUserFlatNumber;
        private final TextView textUserMobileNumber;
        private final TextView textUserCreatedDate;
        private final TextView textUserNameValue;
        private final TextView textUserApartmentNameValue;
        private final TextView textUserFlatNumberValue;
        private final TextView textUserMobileNumberValue;
        private final TextView textUserCreatedDateValue;
        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textEmail;
        private final TextView textDues;
        private final LinearLayout layoutIcons;
        private final LinearLayout layoutButtons;
        private final View line;
        private final Button buttonApproveUser;
        private final Button buttonDeclineUser;

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
            textUserCreatedDate = itemView.findViewById(R.id.textUserCreatedDate);
            textUserNameValue = itemView.findViewById(R.id.textUserNameValue);
            textUserApartmentNameValue = itemView.findViewById(R.id.textUserApartmentNameValue);
            textUserFlatNumberValue = itemView.findViewById(R.id.textUserFlatNumberValue);
            textUserMobileNumberValue = itemView.findViewById(R.id.textUserMobileNumberValue);
            textUserCreatedDateValue = itemView.findViewById(R.id.textUserCreatedDateValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEmail = itemView.findViewById(R.id.textEmail);
            textDues = itemView.findViewById(R.id.textDues);
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
            textUserCreatedDate.setTypeface(setLatoRegularFont(mCtx));
            textUserNameValue.setTypeface(setLatoBoldFont(mCtx));
            textUserApartmentNameValue.setTypeface(setLatoBoldFont(mCtx));
            textUserFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textUserMobileNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textUserCreatedDateValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(setLatoBoldItalicFont(mCtx));
            textEmail.setTypeface(setLatoBoldItalicFont(mCtx));
            textDues.setTypeface(setLatoBoldItalicFont(mCtx));
            buttonApproveUser.setTypeface(setLatoLightFont(mCtx));
            buttonDeclineUser.setTypeface(setLatoLightFont(mCtx));

            /*Setting onClickListener for view*/
            buttonApproveUser.setOnClickListener(this);
            buttonDeclineUser.setOnClickListener(this);
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEmail.setOnClickListener(this);
            textDues.setOnClickListener(this);
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
                case R.id.textDues:
                    showPendingDuesDialog(naUser.getFlatDetails());
                    break;
                case R.id.buttonApproveUser:
                    showMaintenanceCostDialog(position);
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
