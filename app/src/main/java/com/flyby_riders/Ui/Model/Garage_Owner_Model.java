package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 31,July,2020
 */
public class Garage_Owner_Model implements Parcelable
{
    private String GARAGEID,OWNERNAME,STORENAME,PHONE,ADDRESS,CITY,LAT,LANG,DISTANCE_FROM_ME,PROFILEPIC;

    private ArrayList<Category_Model> category_models = new ArrayList<>();

    public Garage_Owner_Model() {
    }

    protected Garage_Owner_Model(Parcel in) {
        GARAGEID = in.readString();
        OWNERNAME = in.readString();
        STORENAME = in.readString();
        PHONE = in.readString();
        ADDRESS = in.readString();
        CITY = in.readString();
        LAT = in.readString();
        LANG = in.readString();
        DISTANCE_FROM_ME = in.readString();
        PROFILEPIC = in.readString();
        category_models = in.createTypedArrayList(Category_Model.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GARAGEID);
        dest.writeString(OWNERNAME);
        dest.writeString(STORENAME);
        dest.writeString(PHONE);
        dest.writeString(ADDRESS);
        dest.writeString(CITY);
        dest.writeString(LAT);
        dest.writeString(LANG);
        dest.writeString(DISTANCE_FROM_ME);
        dest.writeString(PROFILEPIC);
        dest.writeTypedList(category_models);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Garage_Owner_Model> CREATOR = new Creator<Garage_Owner_Model>() {
        @Override
        public Garage_Owner_Model createFromParcel(Parcel in) {
            return new Garage_Owner_Model(in);
        }

        @Override
        public Garage_Owner_Model[] newArray(int size) {
            return new Garage_Owner_Model[size];
        }
    };

    public String getGARAGEID() {
        return GARAGEID;
    }

    public void setGARAGEID(String GARAGEID) {
        this.GARAGEID = GARAGEID;
    }

    public String getOWNERNAME() {
        return OWNERNAME;
    }

    public void setOWNERNAME(String OWNERNAME) {
        this.OWNERNAME = OWNERNAME;
    }

    public String getSTORENAME() {
        return STORENAME;
    }

    public void setSTORENAME(String STORENAME) {
        this.STORENAME = STORENAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLANG() {
        return LANG;
    }

    public void setLANG(String LANG) {
        this.LANG = LANG;
    }

    public String getDISTANCE_FROM_ME() {
        return DISTANCE_FROM_ME;
    }

    public void setDISTANCE_FROM_ME(String DISTANCE_FROM_ME) {
        this.DISTANCE_FROM_ME = DISTANCE_FROM_ME;
    }

    public String getPROFILEPIC() {
        return PROFILEPIC;
    }

    public void setPROFILEPIC(String PROFILEPIC) {
        this.PROFILEPIC = PROFILEPIC;
    }

    public ArrayList<Category_Model> getCategory_models() {
        return category_models;
    }

    public void setCategory_models(ArrayList<Category_Model> category_models) {
        this.category_models = category_models;
    }
}
