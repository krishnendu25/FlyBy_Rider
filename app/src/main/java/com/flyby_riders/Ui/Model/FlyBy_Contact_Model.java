package com.flyby_riders.Ui.Model;

/**
 * Created by KRISHNENDU MANNA on 09,July,2020
 */
public class FlyBy_Contact_Model {
    public String Contact_Name="null";
    public String Contact_Number;
    public String User_id;

    public FlyBy_Contact_Model() {
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public void setContact_Number(String contact_Number) {
        Contact_Number = contact_Number;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }
}
