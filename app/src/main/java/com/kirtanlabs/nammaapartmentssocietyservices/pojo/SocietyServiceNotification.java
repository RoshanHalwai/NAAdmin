package com.kirtanlabs.nammaapartmentssocietyservices.pojo;

import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/2/2018
 */
public class SocietyServiceNotification {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String notificationUID;
    private String problem;
    private String societyServiceType;
    private String status;
    private String timeSlot;
    private long timeStamp;
    private String userUID;

    /*Variable to indicate if Society Service Person has Accepted or Rejected the Notification*/
    private String societyServiceResponse;
    private NAUser naUser;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public SocietyServiceNotification() {

    }

    public SocietyServiceNotification(String notificationUID, String problem, String societyServiceType, String status, String timeSlot, long timeStamp, String userUID) {
        this.notificationUID = notificationUID;
        this.problem = problem;
        this.societyServiceType = societyServiceType;
        this.status = status;
        this.timeSlot = timeSlot;
        this.timeStamp = timeStamp;
        this.userUID = userUID;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getNotificationUID() {
        return notificationUID;
    }

    public String getProblem() {
        return problem;
    }

    public String getSocietyServiceType() {
        return societyServiceType;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getSocietyServiceResponse() {
        return societyServiceResponse;
    }

    public void setSocietyServiceResponse(String societyServiceResponse) {
        this.societyServiceResponse = societyServiceResponse;
    }

    public void setNaUser(NAUser naUser) {
        this.naUser = naUser;
    }

    public NAUser getNaUser() {
        return naUser;
    }
}
