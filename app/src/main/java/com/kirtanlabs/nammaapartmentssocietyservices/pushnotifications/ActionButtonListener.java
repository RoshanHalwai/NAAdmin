package com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ACCEPTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_AVAILABLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FUTURE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_HISTORY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_REJECTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SERVING;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_TAKEN_BY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.generateOTP;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class ActionButtonListener extends BroadcastReceiver {

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
     * @param status          is the notification status, if it has been accepted/rejected
     */
    private void replyNotification(final Context context, final String notificationUID, final String mobileNumber, final String societyServiceType, final String status) {

        DatabaseReference allSocietyServiceReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child(mobileNumber);
        allSocietyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String societyServiceUID = dataSnapshot.getValue(String.class);

                DatabaseReference notificationsReference = Constants.SOCIETY_SERVICES_REFERENCE.child(societyServiceType)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(Objects.requireNonNull(societyServiceUID))
                        .child(FIREBASE_CHILD_NOTIFICATIONS);

                if (status.equals(FIREBASE_CHILD_ACCEPTED)) {
                    /*Using the notificationsReference to differentiate between "serving" & "future" after the request has been 'Accepted'*/
                    notificationsReference.child(FIREBASE_CHILD_SERVING).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference notificationTabReference;
                            /*If no UID exists in 'Serving', the request will to 'serving'*/
                            if (!dataSnapshot.exists()) {
                                notificationTabReference = notificationsReference.child(FIREBASE_CHILD_SERVING);
                            }
                            /*If a UID exists in 'serving', the new request will move to 'future'*/
                            else {
                                notificationTabReference = notificationsReference.child(FIREBASE_CHILD_FUTURE);
                            }
                            notificationTabReference.child(notificationUID).setValue(status).addOnSuccessListener(aVoid -> {
                                DatabaseReference societyServiceNotificationsReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(notificationUID);
                                societyServiceNotificationsReference.child(FIREBASE_CHILD_TAKEN_BY).setValue(societyServiceUID);
                                societyServiceNotificationsReference.child("endOTP").setValue(generateOTP());
                                Intent homeIntent = new Intent(context, HomeViewPager.class);
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(homeIntent);
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    notificationsReference.child(FIREBASE_CHILD_HISTORY).child(notificationUID).setValue(status).addOnSuccessListener(aVoid -> {
                        context.startActivity(new Intent(context, HomeViewPager.class));
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}