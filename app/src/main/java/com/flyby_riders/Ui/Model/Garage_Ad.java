package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Garage_Ad implements Parcelable
{

    String USERID
    ,ADVID
    ,ADTITLE
    ,ADDESC
    ,ADVIDEO
    ,ADCOVERIMAGES
    ,ADCOSTPRICE
    ,ADCREATIONDATE
    ,ADPOSTDATE;

    ArrayList<String> images = new ArrayList<>();


    public Garage_Ad() {
    }

    protected Garage_Ad(Parcel in) {
        USERID = in.readString();
        ADVID = in.readString();
        ADTITLE = in.readString();
        ADDESC = in.readString();
        ADVIDEO = in.readString();
        ADCOVERIMAGES = in.readString();
        ADCOSTPRICE = in.readString();
        ADCREATIONDATE = in.readString();
        ADPOSTDATE = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<Garage_Ad> CREATOR = new Creator<Garage_Ad>() {
        @Override
        public Garage_Ad createFromParcel(Parcel in) {
            return new Garage_Ad(in);
        }

        @Override
        public Garage_Ad[] newArray(int size) {
            return new Garage_Ad[size];
        }
    };

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getADVID() {
        return ADVID;
    }

    public void setADVID(String ADVID) {
        this.ADVID = ADVID;
    }

    public String getADTITLE() {
        return ADTITLE;
    }

    public void setADTITLE(String ADTITLE) {
        this.ADTITLE = ADTITLE;
    }

    public String getADDESC() {
        return ADDESC;
    }

    public void setADDESC(String ADDESC) {
        this.ADDESC = ADDESC;
    }

    public String getADVIDEO() {
        return ADVIDEO;
    }

    public void setADVIDEO(String ADVIDEO) {
        this.ADVIDEO = ADVIDEO;
    }

    public String getADCOVERIMAGES() {
        return ADCOVERIMAGES;
    }

    public void setADCOVERIMAGES(String ADCOVERIMAGES) {
        this.ADCOVERIMAGES = ADCOVERIMAGES;
    }

    public String getADCOSTPRICE() {
        return ADCOSTPRICE;
    }

    public void setADCOSTPRICE(String ADCOSTPRICE) {
        this.ADCOSTPRICE = ADCOSTPRICE;
    }

    public String getADCREATIONDATE() {
        return ADCREATIONDATE;
    }

    public void setADCREATIONDATE(String ADCREATIONDATE) {
        this.ADCREATIONDATE = ADCREATIONDATE;
    }

    public String getADPOSTDATE() {
        return ADPOSTDATE;
    }

    public void setADPOSTDATE(String ADPOSTDATE) {
        this.ADPOSTDATE = ADPOSTDATE;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(USERID);
        dest.writeString(ADVID);
        dest.writeString(ADTITLE);
        dest.writeString(ADDESC);
        dest.writeString(ADVIDEO);
        dest.writeString(ADCOVERIMAGES);
        dest.writeString(ADCOSTPRICE);
        dest.writeString(ADCREATIONDATE);
        dest.writeString(ADPOSTDATE);
        dest.writeStringList(images);
    }
}
