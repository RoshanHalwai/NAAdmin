package com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.SocietyAdminHome;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.approveevents.activities.ApproveEventsActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.manageusers.ManageUsers;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;

import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_EMERGENCY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_EVENT_MANAGEMENT;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SCRAP_COLLECTION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.MESSAGE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_EXPAND_MSG;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_EXPAND_TITLE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REJECT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_CANCELLED_SERVICE_REQUEST;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_SOCIETY_SERVICE_TYPE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_USER_ACCOUNT_NOTIFICATION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.REMOTE_USER_DONATE_FOOD_NOTIFICATION;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/27/2018
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String message = remoteMessageData.get(MESSAGE);
        String mobileNumber = remoteMessageData.get(REMOTE_MOBILE_NUMBER);
        String notificationUID = remoteMessageData.get(REMOTE_NOTIFICATION_UID);
        String societyServiceType = remoteMessageData.get(REMOTE_SOCIETY_SERVICE_TYPE);

        if (societyServiceType.equals(FIREBASE_CHILD_EVENT_MANAGEMENT) ||
                societyServiceType.equals(REMOTE_USER_ACCOUNT_NOTIFICATION) ||
                societyServiceType.equals(FIREBASE_CHILD_EMERGENCY) ||
                societyServiceType.equals(REMOTE_CANCELLED_SERVICE_REQUEST) ||
                societyServiceType.equals(REMOTE_USER_DONATE_FOOD_NOTIFICATION) ||
                societyServiceType.equals(FIREBASE_CHILD_SCRAP_COLLECTION)) {

            Intent intent;
            switch (societyServiceType) {
                case FIREBASE_CHILD_EVENT_MANAGEMENT:
                    intent = new Intent(this, ApproveEventsActivity.class);
                    break;
                case Constants.REMOTE_USER_ACCOUNT_NOTIFICATION:
                    intent = new Intent(this, ManageUsers.class);
                    break;
                case REMOTE_CANCELLED_SERVICE_REQUEST:
                    intent = new Intent(this, HomeViewPager.class);
                    break;
                default:
                    intent = new Intent(this, SocietyAdminHome.class);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.NEW_USER_OR_NEW_EVENT_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId;

            /*To support Android Oreo Devices and higher*/
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        getString(R.string.default_notification_channel_id), "Namma Apartments Channel", NotificationManager.IMPORTANCE_HIGH);
                Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
                channelId = mChannel.getId();
            } else {
                channelId = getString(R.string.default_notification_channel_id);
            }

            /*Event Management Notification - These do not require any user actions*/
            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .build();

            int mNotificationID = (int) System.currentTimeMillis();
            Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);
        } else {

            /*Other Society Service Notification here requires Users Action*/
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
            remoteViews.setTextViewText(R.id.textNotificationMessage, message);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId;

            /*To support Android Oreo Devices and higher*/
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        getString(R.string.default_notification_channel_id), "Namma Apartments Channel", NotificationManager.IMPORTANCE_HIGH);
                android.media.AudioAttributes attributes = new android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .build();
                mChannel.setSound(android.net.Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.society_service_alarm), attributes);
                Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
                channelId = mChannel.getId();
                IntentFilter actionIntents = new IntentFilter();
                actionIntents.addAction(ACCEPT_BUTTON_CLICKED);
                actionIntents.addAction(REJECT_BUTTON_CLICKED);
                getApplicationContext().registerReceiver(new ActionButtonListener(), actionIntents);
            } else {
                channelId = getString(R.string.default_notification_channel_id);
            }

            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(NOTIFICATION_EXPAND_TITLE)
                    .setContentText(NOTIFICATION_EXPAND_MSG)
                    .setAutoCancel(true)
                    .setCustomBigContentView(remoteViews)
                    .setPriority(PRIORITY_DEFAULT)
                    .build();

             /* Setting Push Notification Custom Sound */
            notification.sound = android.net.Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.society_service_alarm);

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

            Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);

            Handler h = new Handler(Looper.getMainLooper());
            long delayInMilliseconds = 15000;
            h.postDelayed(() -> notificationManager.cancel(mNotificationID), delayInMilliseconds);
        }
    }
}