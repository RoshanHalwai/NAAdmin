package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.home.NammaApartmentsPlumberServices;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ACCEPTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_REJECTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_TAKEN_BY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class Button_listener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String action = intent.getAction();
            String notificationUID = Objects.requireNonNull(intent.getExtras()).getString(NOTIFICATION_UID);
            String mobileNumber = Objects.requireNonNull(intent.getExtras()).getString(MOBILE_NUMBER);
            String societyServiceType = Objects.requireNonNull(intent.getExtras()).getString(SOCIETY_SERVICE_TYPE);
            int notificationId = intent.getExtras().getInt(NOTIFICATION_ID);

            /*If Society Service clicks on Accept Button*/
            if (action.equals(ACCEPT_BUTTON_CLICKED)) {
                replyNotification(context, notificationUID, mobileNumber, societyServiceType, FIREBASE_CHILD_ACCEPTED);
            } else {
                /*If Society Service clicks on Reject Button*/
                replyNotification(context, notificationUID, mobileNumber, societyServiceType, FIREBASE_CHILD_REJECTED);
            }

            /*Clear the notification once button is pressed*/
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(manager).cancel(notificationId);
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when the Society Service presses the Accept/Reject button in the custom notification tray
     *
     * @param notificationUID is the UID of the particular received notification
     * @param status is the notification status, if it has been accepted/rejected
     */
    private void replyNotification(final Context context, final String notificationUID, final String mobileNumber, final String societyServiceType, final String status) {

        DatabaseReference allSocietyServiceReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child(mobileNumber);
        allSocietyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String societyServiceUID = dataSnapshot.getValue(String.class);

                DatabaseReference societyServicesReference = Constants.SOCIETY_SERVICES_REFERENCE.child(societyServiceType)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(Objects.requireNonNull(societyServiceUID))
                        .child(FIREBASE_CHILD_NOTIFICATIONS);
                societyServicesReference.child(notificationUID).setValue(status).addOnSuccessListener(aVoid -> {

                    if (status.equals(FIREBASE_CHILD_ACCEPTED)) {
                        DatabaseReference societyServiceNotificationsReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(notificationUID);
                        societyServiceNotificationsReference.child(FIREBASE_CHILD_TAKEN_BY).setValue(societyServiceUID);
                        context.startActivity(new Intent(context, NammaApartmentsPlumberServices.class));
                    } else {
                        context.startActivity(new Intent(context, NammaApartmentsPlumberServices.class));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}