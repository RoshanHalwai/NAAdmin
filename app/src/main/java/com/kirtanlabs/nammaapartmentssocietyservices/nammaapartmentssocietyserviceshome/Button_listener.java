package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class Button_listener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String action = intent.getAction();
            String notificationUID = Objects.requireNonNull(intent.getExtras()).getString("Notification_UID");
            String mobileNumber = Objects.requireNonNull(intent.getExtras()).getString("Mobile_Number");
            int notificationId = intent.getExtras().getInt("Notification_Id");

            /*If Society Service clicks on Accept Button*/
            if (action.equals("accept_button_clicked")) {
                replyNotification(notificationUID, mobileNumber, "Accepted");
                Log.d("Notification Test", action);
                Log.d("notificationUID", notificationUID);
            } else {
                /*If Society Service clicks on Reject Button*/
                replyNotification(notificationUID, mobileNumber, "Rejected");
                Log.d("Notification Test", action);
                Log.d("notificationUID", notificationUID);
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
    private void replyNotification(final String notificationUID, final String mobileNumber, final String status) {

        DatabaseReference allSocietyServiceReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child(mobileNumber);
        allSocietyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String societyServiceUID = dataSnapshot.getValue(String.class);

                /*Once the notification gets triggered, a 'notifications' child will be created in
                societyServices->societyServiceType->private->data->uid->notifications->notificationUID
                Value will be set to either Accepted/Rejected*/
                DatabaseReference societyServicesReference = Constants.SOCIETY_SERVICES_REFERENCE.child("plumber")
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(FIREBASE_CHILD_DATA)
                        .child(Objects.requireNonNull(societyServiceUID))
                        .child("notifications");
                societyServicesReference.child(notificationUID).setValue(status).addOnSuccessListener(aVoid -> {

                    if (status.equals("Accepted")) {
                        DatabaseReference societyServiceNotificationsReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(notificationUID);
                        societyServiceNotificationsReference.child("takenBy").setValue(societyServiceUID);
                    } else {
                        //We need to send notifications to next available society service
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}