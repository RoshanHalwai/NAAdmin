package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.pojo.DonateFoodPojo;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_ACCEPTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_OTHER_DETAILS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_RATING;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_VERIFIED_APPROVED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_VERIFIED_PENDING;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FOOD_DONATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICE_TYPE_REFERENCE;

public class RetrievingNotificationData {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final String societyServiceUID;
    private final Context context;
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
                    societyServiceData.setRating(dataSnapshot.child(FIREBASE_CHILD_RATING).getValue(Integer.class));
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
                getNotificationDataList(new LinkedList<>(historyUIDMap.keySet()), societyServiceNotificationList -> {
                    List<SocietyServiceNotification> serviceHistoryList = new LinkedList<>();
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
                getNotificationDataList(new LinkedList<>(futureUIDMap.values()), societyServiceNotificationList -> {
                    List<SocietyServiceNotification> serviceFutureList = new LinkedList<>();
                    for (SocietyServiceNotification societyServiceNotification : societyServiceNotificationList) {
                        getUserData(societyServiceNotification.getUserUID(), NAUser -> {
                            societyServiceNotification.setNaUser(NAUser);
                            societyServiceNotification.setSocietyServiceResponse(FIREBASE_CHILD_ACCEPTED);
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
     * Return the list of all food collection Data
     *
     * @param foodCollectionDataListCallback - callback to return the list of all food collection data.
     */
    public void getFoodCollectionDataList(FoodCollectionDataListCallback foodCollectionDataListCallback) {
        /*Getting the list of all food collection UID*/
        getFoodCollectionUIDList(foodCollectionUIDList -> {
            if (!foodCollectionUIDList.isEmpty()) {
                ArrayList<DonateFoodPojo> foodCollectionDataList = new ArrayList<>();

                for (String foodCollectionUID : foodCollectionUIDList) {
                    getFoodCollectionData(foodCollectionUID, donateFoodData -> {
                        foodCollectionDataList.add(donateFoodData);

                        if (foodCollectionDataList.size() == foodCollectionUIDList.size()) {
                            foodCollectionDataListCallback.onCallBack(foodCollectionDataList);
                        }
                    });
                }

            } else {
                foodCollectionDataListCallback.onCallBack(new ArrayList<>());
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
                        int verified = NAUser.getPrivileges().getVerified();
                        if (verified == FIREBASE_CHILD_VERIFIED_PENDING) {
                            UnapprovedUsersDataList.add(index++, NAUser);
                        }
                        if (count == userUIDList.size()) {
                            unApprovedUsersDataListCallback.onCallBack(UnapprovedUsersDataList);
                            count = 0;
                        }
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
            if (!userUIDList.isEmpty()) {
                List<NAUser> approvedUsersDataList = new ArrayList<>();
                for (String userUID : userUIDList) {
                    getUserData(userUID, NAUser -> {
                        count++;
                        int verified = NAUser.getPrivileges().getVerified();
                        if (verified == FIREBASE_CHILD_VERIFIED_APPROVED) {
                            approvedUsersDataList.add(index++, NAUser);
                        }
                        if (count == userUIDList.size()) {
                            approvedUsersDataListCallback.onCallBack(approvedUsersDataList);
                            count = 0;
                        }
                    });
                }
            } else {
                approvedUsersDataListCallback.onCallBack(new ArrayList<>());
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
        Query historyReference = ((SocietyServiceGlobal) context.getApplicationContext())
                .getHistoryNotificationReference().orderByValue();
        historyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
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
    void getFutureUIDMap(FutureUIDMapCallback futureUIDMapCallback) {
        Query futureReference = ((SocietyServiceGlobal) context.getApplicationContext())
                .getFutureNotificationReference().orderByKey();
        HashMap<String, String> futureUIDMap = new LinkedHashMap<>();
        futureReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot uidSnapshot : dataSnapshot.getChildren()) {
                        futureUIDMap.put(uidSnapshot.getKey(), uidSnapshot.getValue(String.class));
                    }
                    futureUIDMapCallback.onCallback(futureUIDMap);
                } else
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
        List<SocietyServiceNotification> notificationDataList = new LinkedList<>();
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
                NAUser naUser = dataSnapshot.getValue(NAUser.class);
                long timestamp = Objects.requireNonNull(dataSnapshot.child(FIREBASE_CHILD_OTHER_DETAILS).child(FIREBASE_CHILD_TIMESTAMP).getValue(Long.class));
                Objects.requireNonNull(naUser).setTimestamp(timestamp);
                userDataCallback.onCallBack(naUser);
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

    /**
     * Return the list of all food collection UID
     *
     * @param foodCollectionUIDListCallBack - callback to return all UID of food collection
     */
    private void getFoodCollectionUIDList(FoodCollectionUIDListCallBack foodCollectionUIDListCallBack) {
        FOOD_DONATION_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> foodDonationUIDList = new ArrayList<>();
                    for (DataSnapshot foodDonationUIDSnapshot : dataSnapshot.getChildren()) {
                        foodDonationUIDList.add(foodDonationUIDSnapshot.getKey());
                    }

                    if (foodDonationUIDList.size() == dataSnapshot.getChildrenCount()) {
                        foodCollectionUIDListCallBack.onCallBack(foodDonationUIDList);
                    }
                } else {
                    foodCollectionUIDListCallBack.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Return the food collection data for a particular UID
     *
     * @param foodCollectionUID          - UID of particular food donation
     * @param foodCollectionDataCallBack - return the Food Donation data for a particular UID
     */
    private void getFoodCollectionData(final String foodCollectionUID, FoodCollectionDataCallBack foodCollectionDataCallBack) {
        DatabaseReference foodDonationDataReference = FOOD_DONATION_REFERENCE
                .child(foodCollectionUID);

        foodDonationDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DonateFoodPojo donateFoodPojo = dataSnapshot.getValue(DonateFoodPojo.class);

                getUserData(donateFoodPojo.getUserUID(), NAUser -> {
                    donateFoodPojo.setNaUser(NAUser);
                    foodCollectionDataCallBack.onCallBack(donateFoodPojo);
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    interface ServiceTypeCallback {
        void onCallBack(String serviceType);
    }

    interface ServingDataCallback {
        void onCallBack(SocietyServiceNotification societyServiceNotification);
    }

    public interface UserDataCallback {
        void onCallBack(NAUser NAUser);
    }

    interface ServingUIDCallback {
        void onCallBack(String servingUID);
    }


    interface HistoryUIDMapCallback {
        void onCallback(Map<String, String> historyUIDMap);
    }

    public interface FutureUIDMapCallback {
        void onCallback(Map<String, String> futureUIDMap);
    }

    interface NotificationDataListCallback {
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

    interface UserUIDCallback {
        void onCallback(List<String> userUIDList);
    }

    public interface UnApprovedUsersDataListCallback {
        void onCallBack(List<NAUser> unapprovedUsersList);
    }

    public interface ApprovedUsersDataListCallback {
        void onCallBack(List<NAUser> approvedUsersList);
    }

    interface FoodCollectionUIDListCallBack {
        void onCallBack(List<String> foodCollectionUIDList);
    }

    interface FoodCollectionDataCallBack {
        void onCallBack(DonateFoodPojo donateFoodData);
    }

    public interface FoodCollectionDataListCallback {
        void onCallBack(List<DonateFoodPojo> foodCollectionDataList);
    }

}
