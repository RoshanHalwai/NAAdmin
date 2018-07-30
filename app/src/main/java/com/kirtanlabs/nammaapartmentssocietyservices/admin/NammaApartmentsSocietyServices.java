package com.kirtanlabs.nammaapartmentssocietyservices.admin;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/29/2018
 */

public class NammaApartmentsSocietyServices {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String mobileNumber;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentsSocietyServices() {
    }

    NammaApartmentsSocietyServices(String fullName, String mobileNumber, String uid) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.uid = uid;

    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getUid() {
        return uid;
    }
}
