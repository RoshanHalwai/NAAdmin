package com.kirtanlabs.nammaapartmentssocietyservices.nammaapartmentssocietyserviceshome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/27/2018
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String message = remoteMessageData.get(Constants.MESSAGE);
        String notificationUID = remoteMessageData.get("society_service_uid");
        String ownerUID = remoteMessageData.get("owner_uid");

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

        remoteViews.setTextViewText(R.id.textNotificationMessage, message);

        Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                .setPriority(PRIORITY_DEFAULT)
                .build();

        int mNotificationID = (int) System.currentTimeMillis();

        Intent acceptButtonIntent = new Intent("accept_button_clicked");
        acceptButtonIntent.putExtra("Notification_Id", mNotificationID);
        acceptButtonIntent.putExtra("Notification_UID", notificationUID);
        acceptButtonIntent.putExtra("User_UID", ownerUID);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

        Intent rejectButtonIntent = new Intent("reject_button_clicked");
        rejectButtonIntent.putExtra("Notification_UID", notificationUID);
        rejectButtonIntent.putExtra("Notification_Id", mNotificationID);
        rejectButtonIntent.putExtra("User_UID", ownerUID);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 123, rejectButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonReject, rejectPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int notificationID = (int) System.currentTimeMillis();
        Objects.requireNonNull(notificationManager).notify(notificationID, notification);
    }
}