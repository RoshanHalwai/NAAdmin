package com.kirtanlabs.nammaapartmentssocietyservices.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NammaApartmentUser implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private UserFlatDetails flatDetails;
    private UserPersonalDetails personalDetails;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentUser() {
    }

    public NammaApartmentUser(String uid, UserPersonalDetails personalDetails, UserFlatDetails flatDetails) {
        this.uid = uid;
        this.personalDetails = personalDetails;
        this.flatDetails = flatDetails;
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

    public String getUID() {
        return uid;
    }

}
