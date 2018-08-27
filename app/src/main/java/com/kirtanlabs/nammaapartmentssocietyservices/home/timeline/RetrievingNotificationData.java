package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE_REFERENCE;

public class RetrievingNotificationData {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String societyServiceUID;
    private Context context;
    private int index = 0;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingNotificationData(Context context, String societyServiceUID) {
        this.societyServiceUID = societyServiceUID;
        this.context = context;
    }

    /* ------------------------------------------------------------- *
     * Public APIs
     * ------------------------------------------------------------- */

    /**
     * Returns the data of the current society service
     *
     * @param societyServiceDataCallback callback to return the data of the current society service
     */
    public void getSocietyServiceData(SocietyServiceDataCallback societyServiceDataCallback) {
        getServiceType(serviceType -> {
            DatabaseReference societyServiceDataRef = SOCIETY_SERVICES_REFERENCE.child(serviceType)
                    .child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA)
                    .child(societyServiceUID);
            societyServiceDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SocietyServiceData societyServiceData = dataSnapshot.getValue(SocietyServiceData.class);
                    Objects.requireNonNull(societyServiceData).setSocietyServiceType(serviceType);
                    societyServiceDataCallback.onCallback(societyServiceData);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }

    public void getServingNotificationData(ServingNotificationDataCallback servingNotificationDataCallback) {
        getServingNotificationUID(servingUID -> {
            if (servingUID == null) {
                servingNotificationDataCallback.onCallback(null);
            } else {
                getNotificationData(servingUID, societyServiceNotification -> getUserData(societyServiceNotification.getUserUID(), NAUser -> {
                    societyServiceNotification.setNaUser(NAUser);
                    servingNotificationDataCallback.onCallback(societyServiceNotification);
                }));
            }
        });
    }

    /**
     * Returns a list of all notification data which society service has received till date
     *
     * @param historyNotificationDataListCallback callback to return the list of history notification data
     */
    public void getHistoryNotificationDataList(HistoryNotificationDataListCallback historyNotificationDataListCallback) {
        getHistoryUIDMap(historyUIDMap ->
        {
            if (historyUIDMap != null) {
                getNotificationDataList(new ArrayList<>(historyUIDMap.keySet()), societyServiceNotificationList -> {
                    List<SocietyServiceNotification> serviceHistoryList = new ArrayList<>();
                    for (SocietyServiceNotification societyServiceNotification : societyServiceNotificationList) {
                        getUserData(societyServiceNotification.getUserUID(), NAUser -> {
                            societyServiceNotification.setNaUser(NAUser);
                            societyServiceNotification.setSocietyServiceResponse(historyUIDMap.get(societyServiceNotification.getNotificationUID()));
                            serviceHistoryList.add(societyServiceNotification);

                            if (societyServiceNotificationList.size() == serviceHistoryList.size()) {
                                historyNotificationDataListCallback.onCallback(serviceHistoryList);
                            }
                        });
                    }
                });
            } else {
                historyNotificationDataListCallback.onCallback(null);
            }
        });
    }

    /**
     * Returns a list of all notification data which society service has received till date
     *
     * @param futureNotificationDataListCallback callback to return the list of history notification data
     */
    public void getFutureNotificationDataList(FutureNotificationDataListCallback futureNotificationDataListCallback) {
        getFutureUIDMap(futureUIDMap -> {
            if (futureUIDMap != null) {
                getNotificationDataList(new ArrayList<>(futureUIDMap.keySet()), societyServiceNotificationList -> {
                    List<SocietyServiceNotification> serviceFutureList = new ArrayList<>();
                    for (SocietyServiceNotification societyServiceNotification : societyServiceNotificationList) {
                        getUserData(societyServiceNotification.getUserUID(), NAUser -> {
                            societyServiceNotification.setNaUser(NAUser);
                            societyServiceNotification.setSocietyServiceResponse(futureUIDMap.get(societyServiceNotification.getNotificationUID()));
                            serviceFutureList.add(societyServiceNotification);

                            if (societyServiceNotificationList.size() == serviceFutureList.size()) {
                                futureNotificationDataListCallback.onCallback(serviceFutureList);
                            }
                        });
                    }
                });
            } else {
                futureNotificationDataListCallback.onCallback(null);
            }
        });
    }

    /**
     * Returns a list of all unapproved users data
     *
     * @param unApprovedUsersDataListCallback callback to return the list of unapproved user data
     */
    public void getUnapprovedUserDataList(UnApprovedUsersDataListCallback unApprovedUsersDataListCallback) {
        getAllUserUidList(userUIDList -> {
            if (!userUIDList.isEmpty()) {
                List<NAUser> UnapprovedUsersDataList = new ArrayList<>();
                for (String userUID : userUIDList) {
                    getUserData(userUID, NAUser -> {
                        count++;
                        boolean isVerified = NAUser.getPrivileges().isVerified();
                        if (!isVerified) {
                            UnapprovedUsersDataList.add(index++, NAUser);
                        }
                        if (count == userUIDList.size())
                            unApprovedUsersDataListCallback.onCallBack(UnapprovedUsersDataList);
                    });
                }
            } else {
                unApprovedUsersDataListCallback.onCallBack(new ArrayList<>());
            }
        });
    }

    /**
     * Returns a list of all approved users data
     *
     * @param approvedUsersDataListCallback callback to return the list of approved user data
     */
    public void getApprovedUserDataList(ApprovedUsersDataListCallback approvedUsersDataListCallback) {
        getAllUserUidList(userUIDList -> {
            if (userUIDList != null) {
                List<NAUser> approvedUsersDataList = new ArrayList<>();
                for (String userUID : userUIDList) {
                    getUserData(userUID, NAUser -> {
                        boolean isVerified = NAUser.getPrivileges().isVerified();
                        if (isVerified) {
                            approvedUsersDataList.add(index++, NAUser);
                        }
                        approvedUsersDataListCallback.onCallBack(approvedUsersDataList);
                    });
                }
            } else {
                approvedUsersDataListCallback.onCallBack(null);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private APIs
     * ------------------------------------------------------------- */

    /**
     * Returns the Society Service Type
     *
     * @param serviceTypeCallback callback to return Society Service Type
     */
    private void getServiceType(ServiceTypeCallback serviceTypeCallback) {
        DatabaseReference societyServiceTypeReference = SOCIETY_SERVICE_TYPE_REFERENCE.
                child(societyServiceUID);
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
     * Returns a map with Key as Notification UID and value as response for the Notification
     *
     * @param historyUIDMapCallback callback to return a map of all notification UID which society service has received till date
     */
    private void getHistoryUIDMap(HistoryUIDMapCallback historyUIDMapCallback) {
        DatabaseReference historyReference = ((SocietyServiceGlobal) context.getApplicationContext())
                .getHistoryNotificationReference();
        historyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    historyUIDMapCallback.onCallback((Map<String, String>) dataSnapshot.getValue());
                else
                    historyUIDMapCallback.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns a map with Key as Notification UID and value as response for the Notification
     *
     * @param futureUIDMapCallback callback to return a map of all notification UID which society service will
     *                             serve in future
     */
    protected void getFutureUIDMap(FutureUIDMapCallback futureUIDMapCallback) {
        DatabaseReference futureReference = ((SocietyServiceGlobal) context.getApplicationContext())
                .getFutureNotificationReference();
        futureReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    futureUIDMapCallback.onCallback((Map<String, String>) dataSnapshot.getValue());
                else
                    futureUIDMapCallback.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns a list of Notification data by taking a list of notification UID as input
     *
     * @param notificationUIDList          whose notification data needs to be returned
     * @param notificationDataListCallback callback to return list of notification data
     */
    private void getNotificationDataList(List<String> notificationUIDList, NotificationDataListCallback notificationDataListCallback) {
        List<SocietyServiceNotification> notificationDataList = new ArrayList<>();
        for (String notificationUID : notificationUIDList) {
            getNotificationData(notificationUID, societyServiceNotification -> {
                notificationDataList.add(societyServiceNotification);
                if (notificationUIDList.size() == notificationDataList.size()) {
                    notificationDataListCallback.onCallback(notificationDataList);
                }

            });
        }
    }

    /**
     * Returns the details of the Notification by taking notification UID as input
     *
     * @param notificationUID     the unique ID to identify the notification
     * @param servingDataCallback callback to return details of the Notification
     */
    private void getNotificationData(String notificationUID, ServingDataCallback servingDataCallback) {
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
     * Returns currently serving Notification UID
     *
     * @param servingUIDCallback callback to return Notification UID
     */
    private void getServingNotificationUID(ServingUIDCallback servingUIDCallback) {
        DatabaseReference notificationsReference = ((SocietyServiceGlobal) context.getApplicationContext())
                .getServingNotificationReference();
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

    /**
     * Returns the list of All Users UID
     *
     * @param userUIDCallback callback to return users UID list
     */
    private void getAllUserUidList(UserUIDCallback userUIDCallback) {
        ALL_USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> userUIDList = new ArrayList<>();
                    for (DataSnapshot userUIDDataSnapshot : dataSnapshot.getChildren()) {
                        userUIDList.add(userUIDDataSnapshot.getValue(String.class));
                    }
                    userUIDCallback.onCallback(userUIDList);
                } else {
                    userUIDCallback.onCallback(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
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

    public interface FutureUIDMapCallback {
        void onCallback(Map<String, String> futureUIDMap);
    }

    public interface NotificationDataListCallback {
        void onCallback(List<SocietyServiceNotification> societyServiceNotificationList);
    }

    public interface ServingNotificationDataCallback {
        void onCallback(SocietyServiceNotification societyServiceNotification);
    }

    public interface HistoryNotificationDataListCallback {
        void onCallback(List<SocietyServiceNotification> historyNotificationDataList);
    }

    public interface FutureNotificationDataListCallback {
        void onCallback(List<SocietyServiceNotification> historyNotificationDataList);
    }

    public interface SocietyServiceDataCallback {
        void onCallback(SocietyServiceData societyServiceData);
    }

    public interface UserUIDCallback {
        void onCallback(List<String> userUIDList);
    }

    public interface UnApprovedUsersDataListCallback {
        void onCallBack(List<NAUser> unapprovedUsersList);
    }

    public interface ApprovedUsersDataListCallback {
        void onCallBack(List<NAUser> approvedUsersList);
    }

}
