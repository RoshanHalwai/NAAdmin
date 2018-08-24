package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.pojo;

public class NoticeBoardPojo {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String nameOfAdmin;
    private String title;
    private String description;
    private String dateAndTime;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NoticeBoardPojo() {

    }

    public NoticeBoardPojo(String nameOfAdmin, String title, String description, String dateAndTime) {
        this.nameOfAdmin = nameOfAdmin;
        this.title = title;
        this.description = description;
        this.dateAndTime = dateAndTime;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getNameOfAdmin() {
        return nameOfAdmin;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}
