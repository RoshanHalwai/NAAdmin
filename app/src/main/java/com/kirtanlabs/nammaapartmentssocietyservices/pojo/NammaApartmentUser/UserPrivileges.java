package com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser;


public class UserPrivileges {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private boolean admin;
    private boolean grantedAccess;
    private boolean verified;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public UserPrivileges() {
    }

    public UserPrivileges(boolean admin, boolean grantedAccess, boolean verified) {
        this.admin = admin;
        this.grantedAccess = grantedAccess;
        this.verified = verified;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public boolean isAdmin() {
        return admin;
    }

    public boolean isGrantedAccess() {
        return grantedAccess;
    }

    public boolean isVerified() {
        return verified;
    }

}
