package com.flyby_riders.Ui.Model;

/**
 * Created by KRISHNENDU MANNA on 11,July,2020
 */
public class Ride_Member_model
{
    String USERID,PHONE,FULL_NAME;
    boolean Admin=false;


    public Ride_Member_model() {
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }
}
