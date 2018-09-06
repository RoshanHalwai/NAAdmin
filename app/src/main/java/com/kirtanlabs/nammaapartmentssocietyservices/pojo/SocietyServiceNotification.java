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
    private long timestamp;
    private String userUID;
    private String endOTP;
    private String eventDate;
    private String eventTitle;

    /*Variable to indicate if Society Service Person has Accepted or Rejected the Notification*/
    private String societyServiceResponse;
    private NAUser naUser;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public SocietyServiceNotification() {

    }

    public SocietyServiceNotification(String notificationUID, String problem, String societyServiceType, String status, String timeSlot, long timestamp, String userUID, String endOTP) {
        this.notificationUID = notificationUID;
        this.problem = problem;
        this.societyServiceType = societyServiceType;
        this.status = status;
        this.timeSlot = timeSlot;
        this.timestamp = timestamp;
        this.userUID = userUID;
        this.endOTP = endOTP;
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
        return timestamp;
    }

    public String getUserUID() {
        return userUID;
    }

    public NAUser getNaUser() {
        return naUser;
    }

    public String getSocietyServiceResponse() {
        return societyServiceResponse;
    }

    public String getEndOTP() {
        return endOTP;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */
    public void setSocietyServiceResponse(String societyServiceResponse) {
        this.societyServiceResponse = societyServiceResponse;
    }

    public void setNaUser(NAUser naUser) {
        this.naUser = naUser;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
