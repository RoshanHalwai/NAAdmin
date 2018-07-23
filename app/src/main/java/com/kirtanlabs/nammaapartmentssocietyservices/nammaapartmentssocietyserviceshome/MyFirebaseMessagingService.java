package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/22/2018
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String message = remoteMessageData.get(Constants.MESSAGE);

        //TODO: Custom Notification tray to be added

        Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                .setPriority(PRIORITY_DEFAULT)
                .build();

        //TODO: Accept and Reject button functionality to be added

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int notificationID = (int) System.currentTimeMillis();
        Objects.requireNonNull(notificationManager).notify(notificationID, notification);
    }
}