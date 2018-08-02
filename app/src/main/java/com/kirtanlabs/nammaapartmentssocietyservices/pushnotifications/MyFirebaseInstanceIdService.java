package com.kirtanlabs.nammaapartmentssocietyservices.pushnotifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/22/2018
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    public static String getRefreshedToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }

}
