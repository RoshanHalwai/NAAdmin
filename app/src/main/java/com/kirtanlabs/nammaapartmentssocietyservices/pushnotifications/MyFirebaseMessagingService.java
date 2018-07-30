package com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications;

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
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REJECT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_SOCIETY_SERVICE_TYPE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/27/2018
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String message = remoteMessageData.get(Constants.MESSAGE);
        String mobileNumber = remoteMessageData.get(REMOTE_MOBILE_NUMBER);
        String notificationUID = remoteMessageData.get(REMOTE_NOTIFICATION_UID);
        String societyServiceType = remoteMessageData.get(REMOTE_SOCIETY_SERVICE_TYPE);

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

        Intent acceptButtonIntent = new Intent(ACCEPT_BUTTON_CLICKED);
        acceptButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
        acceptButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
        acceptButtonIntent.putExtra(MOBILE_NUMBER, mobileNumber);
        acceptButtonIntent.putExtra(SOCIETY_SERVICE_TYPE, societyServiceType);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

        Intent rejectButtonIntent = new Intent(REJECT_BUTTON_CLICKED);
        rejectButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
        rejectButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
        rejectButtonIntent.putExtra(MOBILE_NUMBER, mobileNumber);
        rejectButtonIntent.putExtra(SOCIETY_SERVICE_TYPE, societyServiceType);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 123, rejectButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonReject, rejectPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);
    }
}