package com.kirtanlabs.nammaapartmentssocietyservices.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/2/2018
 */
public class SocietyServiceData {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String mobileNumber;
    private String uid;
    private String societyServiceType;
    private Integer serviceCount;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public SocietyServiceData() {

    }

    public SocietyServiceData(String fullName, String mobileNumber, String uid, Integer serviceCount) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.uid = uid;
        this.serviceCount = serviceCount;
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

    public String getSocietyServiceType() {
        return societyServiceType;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setSocietyServiceType(String societyServiceType) {
        this.societyServiceType = societyServiceType;
    }
}
