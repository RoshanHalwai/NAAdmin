package com.kirtanlabs.nammaapartmentssocietyservices.admin.helpdesk.pojo;

public class HelpDeskPojo {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private long timestamp;
    private String userName;
    private String serviceCategory;
    private String serviceType;
    private String problemDescription;
    private String uid;
    private String userUID;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public HelpDeskPojo() {

    }

    public HelpDeskPojo(long timestamp, String userName, String serviceCategory, String serviceType,
                        String problemDescription, String uid, String userUID) {
        this.timestamp = timestamp;
        this.userName = userName;
        this.serviceCategory = serviceCategory;
        this.serviceType = serviceType;
        this.problemDescription = problemDescription;
        this.uid = uid;
        this.userUID = userUID;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getUserName() {
        return userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public String getProblemDescription() { return problemDescription; }

    public String getUid() { return uid; }

    public String getUserUID() {
        return userUID;
    }

    public String getServiceType() {
        return serviceType;
    }

}
