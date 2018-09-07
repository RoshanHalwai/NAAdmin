package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.pojo;

public class HelpDeskPojo {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dateAndTime;
    private String userName;
    private String serviceCategory;
    private String description;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public HelpDeskPojo() {

    }

    public HelpDeskPojo(String dateAndTime, String userName, String serviceCategory, String description) {
        this.dateAndTime = dateAndTime;
        this.userName = userName;
        this.serviceCategory = serviceCategory;
        this.description = description;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getUserName() {
        return userName;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public String getDescription() {
        return description;
    }


}
