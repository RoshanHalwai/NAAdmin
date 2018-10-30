package com.kirtanlabs.nammaapartmentssocietyservices.admin.scrapcollections.pojo;

import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;

public class ScrapCollectionPojo {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String userUID;
    private String societyServiceType;
    private String notificationUID;
    private String status;
    private long timestamp;
    private String quantity;
    private String scrapType;
    private NAUser naUser;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public ScrapCollectionPojo() {

    }

    public ScrapCollectionPojo(String userUID, String societyServiceType, String notificationUID, String status, Long timestamp, String quantity, String scrapType) {
        this.userUID = userUID;
        this.societyServiceType = societyServiceType;
        this.notificationUID = notificationUID;
        this.status = status;
        this.timestamp = timestamp;
        this.status = status;
        this.quantity = quantity;
        this.scrapType = scrapType;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getUserUID() {
        return userUID;
    }

    public String getSocietyServiceType() {
        return societyServiceType;
    }

    public String getNotificationUID() {
        return notificationUID;
    }

    public String getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getScrapType() {
        return scrapType;
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
