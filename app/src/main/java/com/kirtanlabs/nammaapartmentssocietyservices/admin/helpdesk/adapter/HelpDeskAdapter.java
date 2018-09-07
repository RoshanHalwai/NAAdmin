package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.pojo.HelpDeskPojo;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.UserFlatDetails;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.UserPersonalDetails;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PERSONAL_DETAILS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PHONE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class HelpDeskAdapter extends RecyclerView.Adapter<HelpDeskAdapter.HelpDeskViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<HelpDeskPojo> helpDeskList;
    private final BaseActivity baseActivity;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public HelpDeskAdapter(List<HelpDeskPojo> helpDeskList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
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
        /*Creating an instance of NammaApartmentsNotice class and retrieving the values from Firebase*/
        HelpDeskPojo helpdeskPojo = helpDeskList.get(position);
        holder.textCategoryValue.setText(helpdeskPojo.getServiceCategory());
        holder.textTypeValue.setText(helpdeskPojo.getServiceType());
        holder.textDescriptionValue.setText(helpdeskPojo.getProblemDescription());

        long timestamp = helpdeskPojo.getTimestamp();

        /*Decoding Time Stamp to Date*/
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("MMM dd, yyyy", calendar).toString();
        holder.textComplainDate.setText(date);

        /*Getting the user->private reference to get the user personal details and flat details*/
        DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(helpdeskPojo.getUserUID());
        userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NAUser naUser = dataSnapshot.getValue(NAUser.class);
                UserPersonalDetails userPersonalDetails = Objects.requireNonNull(naUser).getPersonalDetails();
                UserFlatDetails userFlatDetails = naUser.getFlatDetails();
                String userName = userPersonalDetails.getFullName();
                holder.textRaisedByValue.setText(userName);
                String apartmentName = userFlatDetails.getApartmentName();
                holder.textApartmentValue.setText(apartmentName);
                String flatNumber = userFlatDetails.getFlatNumber();
                holder.textFlatNumberValue.setText(flatNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return helpDeskList.size();
    }

    /**
     * This method is invoked when Admin presses on the 'WhatsApp' icon in Card View. Admin will then be able
     * to contact the user via WhatsApp
     */
    private void redirectUserToWhatsApp(int position, String mobileNumber) {
        PackageManager pm = mCtx.getPackageManager();
        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://api.whatsapp.com/send?phone=" + "+91" + mobileNumber;
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.setData(Uri.parse(url));
            if (whatsappIntent.resolveActivity(pm) != null) {
                mCtx.startActivity(whatsappIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ------------------------------------------------------------- *
     * Notice Board View Holder class
     * ------------------------------------------------------------- */

    class HelpDeskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textComplainDate;
        private final TextView textRaisedBy;
        private final TextView textOwnerApartmentName;
        private final TextView textOwnerFlatNumber;
        private final TextView textCategory;
        private final TextView textType;
        private final TextView textProblemDescription;
        private final TextView textRaisedByValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNumberValue;
        private final TextView textCategoryValue;
        private final TextView textTypeValue;
        private final TextView textDescriptionValue;
        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textWhatsapp;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        HelpDeskViewHolder(View itemView) {
            super(itemView);
            /*Getting id's for all the views on cardview*/
            textComplainDate = itemView.findViewById(R.id.textComplainDate);
            textRaisedBy = itemView.findViewById(R.id.textRaisedBy);
            textOwnerApartmentName = itemView.findViewById(R.id.textOwnerApartmentName);
            textOwnerFlatNumber = itemView.findViewById(R.id.textOwnerFlatNo);
            textCategory = itemView.findViewById(R.id.textCategory);
            textType = itemView.findViewById(R.id.textType);
            textProblemDescription = itemView.findViewById(R.id.textProblemDescription);
            textRaisedByValue = itemView.findViewById(R.id.textRaisedByValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textCategoryValue = itemView.findViewById(R.id.textCategoryValue);
            textTypeValue = itemView.findViewById(R.id.textTypeValue);
            textDescriptionValue = itemView.findViewById(R.id.textDescriptionValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textWhatsapp = itemView.findViewById(R.id.textWhatsapp);

            /*Setting Fonts for all the views on cardview*/
            textComplainDate.setTypeface(setLatoBoldFont(mCtx));
            textRaisedBy.setTypeface(setLatoRegularFont(mCtx));
            textOwnerApartmentName.setTypeface(setLatoRegularFont(mCtx));
            textOwnerFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textCategory.setTypeface(setLatoRegularFont(mCtx));
            textType.setTypeface(setLatoRegularFont(mCtx));
            textProblemDescription.setTypeface(setLatoRegularFont(mCtx));
            textRaisedByValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textCategoryValue.setTypeface(setLatoBoldFont(mCtx));
            textTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textDescriptionValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textWhatsapp.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            /*Setting onClick Listeners*/
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textWhatsapp.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            HelpDeskPojo helpDeskPojo = helpDeskList.get(position);
            DatabaseReference phoneNumberReference = PRIVATE_USERS_REFERENCE.child(helpDeskPojo.getUserUID())
                    .child(FIREBASE_CHILD_PERSONAL_DETAILS);
            phoneNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String phoneNumber = dataSnapshot.child(FIREBASE_CHILD_PHONE_NUMBER).getValue(String.class);
                    switch (v.getId()) {
                        case R.id.textCall:
                            baseActivity.makePhoneCall(phoneNumber);
                            break;
                        case R.id.textMessage:
                            baseActivity.sendTextMessage(phoneNumber);
                            break;
                        case R.id.textWhatsapp:
                            redirectUserToWhatsApp(position, phoneNumber);
                            break;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
