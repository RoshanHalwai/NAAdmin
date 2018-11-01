package com.kirtanlabs.nammaapartmentssocietyservices;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceData;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.BETA_ENV;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.DEV_ENV;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_FUTURE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_HISTORY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_SERVING;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.PACKAGE_NAME;
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
    public static String BUILD_VARIANT;
    private SocietyServiceData societyServiceData;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions firebaseOptions;
        BUILD_VARIANT = getBuildVariant();

        if (BUILD_VARIANT.equals(BETA_ENV)) {
            //TODO:Change the database URL when we deploy to new society.
            firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                    .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                    .setDatabaseUrl("https://nammaapartments-beta.firebaseio.com/")
                    .setStorageBucket("nammaapartments-beta.appspot.com")
                    .setGcmSenderId("896005326129")
                    .setProjectId("nammaapartments-beta")
                    .build();
        } else {
            //TODO:Change the database URL when we deploy to new society.
            firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                    .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                    .setDatabaseUrl("https://brigadegateway.firebaseio.com/")
                    .setStorageBucket("nammaapartments-development.appspot.com")
                    .setGcmSenderId("703896080530")
                    .setProjectId("nammaapartments-development")
                    .build();
        }
        initializeApp(this, firebaseOptions, BUILD_VARIANT);
    }

    private String getBuildVariant() {
        return getApplicationContext().getPackageName().equals(PACKAGE_NAME) ? DEV_ENV : BETA_ENV;
    }

    private void initializeApp(final Context context, final FirebaseOptions firebaseOptions, final String buildVariant) {
        try {
            FirebaseApp.initializeApp(context, firebaseOptions, buildVariant);
        } catch (Exception e) {
            Log.d("Nammma Apartments", e.getLocalizedMessage());
        }
    }

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
