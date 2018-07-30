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
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class Button_listener extends BroadcastReceiver {

    private String currentUserID;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String action = intent.getAction();
            String notificationUID = Objects.requireNonNull(intent.getExtras()).getString("Notification_UID");
            int notificationId = intent.getExtras().getInt("Notification_Id");

            /*Get current user UID from Messaging Service*/
            currentUserID = intent.getExtras().getString("User_UID");

            /*If Society Service clicks on Accept Button*/
            if (action.equals("accept_button_clicked")) {
                replyNotification(notificationUID, "Accepted");
                Log.d("Notification Test", action);
                Log.d("notificationUID", notificationUID);
            } else {
                /*If Society Service clicks on Reject Button*/
                replyNotification(notificationUID, "Rejected");
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
    private void replyNotification(final String notificationUID, final String status) {
        /*Once the notification gets triggered, a 'notifications' child will be created in societyServices->all->societyServiceType->data->private->uid*/
        DatabaseReference societyServicesReference = Constants.ALL_SOCIETY_SERVICES_REFERENCE.child("plumber").child(FIREBASE_CHILD_DATA)
                .child(FIREBASE_CHILD_PRIVATE).child(notificationUID);
        societyServicesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*When the Society Service selects Accept/Reject in notification tray, the selected value will be updated in Firebase under 'notifications'*/
                DatabaseReference notificationsReference = societyServicesReference.child(FIREBASE_CHILD_NOTIFICATIONS).push();
                notificationsReference.setValue(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}