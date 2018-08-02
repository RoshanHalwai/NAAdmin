package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_HISTORY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE_REFERENCE;

public class RetrievingNotificationData {


    /**
     * Returns the Society Service Type
     *
     * @param serviceTypeCallback callback to return Society Service Type
     */
    public void getServiceType(ServiceTypeCallback serviceTypeCallback) {
        DatabaseReference societyServiceTypeReference = SOCIETY_SERVICE_TYPE_REFERENCE.
                child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        societyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceTypeCallback.onCallBack(dataSnapshot.getChildren().iterator().next().getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns currently serving Notification UID
     *
     * @param serviceType        service Type of the Society Service
     * @param servingUIDCallback callback to return Notification UID
     */
    public void getServingNotificationUID(String serviceType, ServingUIDCallback servingUIDCallback) {
        DatabaseReference notificationsReference = SOCIETY_SERVICES_REFERENCE.child(serviceType)
                .child(FIREBASE_CHILD_PRIVATE)
                .child(FIREBASE_CHILD_DATA)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(FIREBASE_CHILD_NOTIFICATIONS)
                .child("serving");
        notificationsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    servingUIDCallback.onCallBack(dataSnapshot.getChildren().iterator().next().getKey());
                else
                    servingUIDCallback.onCallBack(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns the details of the Notification
     *
     * @param notificationUID     the unique ID to identify the notification
     * @param servingDataCallback callback to return details of the Notification
     */
    public void getNotificationData(String notificationUID, ServingDataCallback servingDataCallback) {
        DatabaseReference societyServiceNotificationsRef = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                .child(notificationUID);
        societyServiceNotificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servingDataCallback.onCallBack(dataSnapshot.getValue(SocietyServiceNotification.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns the data of the user who has triggered the notification
     *
     * @param userUID          whose data is to be retrieved
     * @param userDataCallback callback to return user data
     */
    public void getUserData(String userUID, UserDataCallback userDataCallback) {
        DatabaseReference usersPrivateReference = PRIVATE_USERS_REFERENCE.child(userUID);
        usersPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDataCallback.onCallBack(dataSnapshot.getValue(NAUser.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getNotificationDataList(List<String> notificationUIDList, NotificationDataListCallback notificationDataListCallback) {
        List<SocietyServiceNotification> notificationDataList = new ArrayList<>();
        for (String notificationUID : notificationUIDList) {
            new RetrievingNotificationData().getNotificationData(notificationUID, new RetrievingNotificationData.ServingDataCallback() {
                @Override
                public void onCallBack(SocietyServiceNotification societyServiceNotification) {
                    notificationDataList.add(societyServiceNotification);

                    if (notificationUIDList.size() == notificationDataList.size()) {
                        notificationDataListCallback.onCallback(notificationDataList);
                    }

                }
            });
        }
    }

    public void getHistoryUIDMap(String societyServiceUID, HistoryUIDMapCallback historyUIDMapCallback) {
        new RetrievingNotificationData().getServiceType(serviceType -> {
            DatabaseReference historyRference = SOCIETY_SERVICES_REFERENCE.child(serviceType)
                    .child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA)
                    .child(societyServiceUID)
                    .child(FIREBASE_CHILD_NOTIFICATIONS)
                    .child(FIREBASE_CHILD_HISTORY);
            historyRference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    historyUIDMapCallback.onCallback((Map<String, String>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface ServiceTypeCallback {
        void onCallBack(String serviceType);
    }

    public interface ServingDataCallback {
        void onCallBack(SocietyServiceNotification societyServiceNotification);
    }

    public interface UserDataCallback {
        void onCallBack(NAUser NAUser);
    }

    public interface ServingUIDCallback {
        void onCallBack(String servingUID);
    }


    public interface HistoryUIDMapCallback {
        void onCallback(Map<String, String> historyUIDMap);
    }

    public interface NotificationDataListCallback {
        void onCallback(List<SocietyServiceNotification> societyServiceNotificationList);
    }

}
