package com.kirtanlabs.nammaapartmentssocietyservices;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FUTURE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_HISTORY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SERVING;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/3/2018
 */
public class SocietyServiceGlobal extends Application {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static String societyServiceUID;
    private SocietyServiceData societyServiceData;

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    private DatabaseReference getNotificationReference() {
        return SOCIETY_SERVICES_REFERENCE
                .child(societyServiceData.getSocietyServiceType())
                .child(FIREBASE_CHILD_PRIVATE)
                .child(FIREBASE_CHILD_DATA)
                .child(societyServiceUID)
                .child(FIREBASE_CHILD_NOTIFICATIONS);
    }

    public DatabaseReference getServingNotificationReference() {
        return getNotificationReference()
                .child(FIREBASE_CHILD_SERVING);
    }

    public DatabaseReference getFutureNotificationReference() {
        return getNotificationReference()
                .child(FIREBASE_CHILD_FUTURE);
    }

    public DatabaseReference getHistoryNotificationReference() {
        return getNotificationReference()
                .child(FIREBASE_CHILD_HISTORY);
    }

    public SocietyServiceData getSocietyServiceData() {
        return societyServiceData;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setSocietyServiceData(SocietyServiceData societyServiceData) {
        this.societyServiceData = societyServiceData;
        societyServiceUID = societyServiceData.getUid();
    }

}
