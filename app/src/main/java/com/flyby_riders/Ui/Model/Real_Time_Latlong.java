package com.flyby_riders.Ui.Model;

public class Real_Time_Latlong {
    double Latitude_Start = 0, Longitude_Start = 0;
    String Rider_Name,MEMBERID;

    public Real_Time_Latlong() {
    }

    public String getRider_Name() {
        return Rider_Name;
    }

    public String getMEMBERID() {
        return MEMBERID;
    }

    public void setMEMBERID(String MEMBERID) {
        this.MEMBERID = MEMBERID;
    }

    public void setRider_Name(String rider_Name) {
        Rider_Name = rider_Name;
    }

    public double getLatitude_Start() {
        return Latitude_Start;
    }

    public void setLatitude_Start(double latitude_Start) {
        Latitude_Start = latitude_Start;
    }

    public double getLongitude_Start() {
        return Longitude_Start;
    }

    public void setLongitude_Start(double longitude_Start) {
        Longitude_Start = longitude_Start;
    }
}
