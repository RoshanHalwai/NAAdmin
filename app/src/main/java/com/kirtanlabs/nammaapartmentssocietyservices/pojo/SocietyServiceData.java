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
    private Integer rating;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public SocietyServiceData() {

    }

    public SocietyServiceData(String fullName, String mobileNumber, String uid) {
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

    public String getSocietyServiceType() {
        return societyServiceType;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public Integer getRating() {
        return rating;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setSocietyServiceType(String societyServiceType) {
        this.societyServiceType = societyServiceType;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
