package com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser;

import java.io.Serializable;

public class NAUser implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private UserFlatDetails flatDetails;
    private UserPersonalDetails personalDetails;
    private UserPrivileges privileges;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NAUser() {
    }

    public NAUser(String uid, UserPersonalDetails personalDetails, UserFlatDetails flatDetails, UserPrivileges privileges) {
        this.uid = uid;
        this.personalDetails = personalDetails;
        this.flatDetails = flatDetails;
        this.privileges = privileges;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public UserFlatDetails getFlatDetails() {
        return flatDetails;
    }

    public UserPersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public String getUID() {
        return uid;
    }

}
