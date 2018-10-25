package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.pojo;

import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;

public class DonateFoodPojo {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String foodType;
    private String foodQuantity;
    private String uid;
    private String userUID;
    private long timestamp;
    private String status;
    private NAUser naUser;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public DonateFoodPojo() {

    }

    public DonateFoodPojo(String foodType, String foodQuantity, String uid, String userUID, long timestamp, String status) {
        this.foodType = foodType;
        this.foodQuantity = foodQuantity;
        this.uid = uid;
        this.userUID = userUID;
        this.timestamp = timestamp;
        this.status = status;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFoodType() {
        return foodType;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public String getUid() {
        return uid;
    }

    public String getUserUID() {
        return userUID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public NAUser getNaUser() {
        return naUser;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setNaUser(NAUser naUser) {
        this.naUser = naUser;
    }
}
