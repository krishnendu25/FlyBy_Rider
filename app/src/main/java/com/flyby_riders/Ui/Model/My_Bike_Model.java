package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KRISHNENDU MANNA on 30,May,2020
 */
public class My_Bike_Model  implements Parcelable
{
   String
           MY_BIKE_ID,
           BRANDID ,
    MODELID,
    BRANDNAME ,
    MODELNAME,
    BRANDPIC,
    MODELPIC;
boolean IsSelect=false;

    public My_Bike_Model() {
    }

    protected My_Bike_Model(Parcel in) {
        MY_BIKE_ID = in.readString();
        BRANDID = in.readString();
        MODELID = in.readString();
        BRANDNAME = in.readString();
        MODELNAME = in.readString();
        BRANDPIC = in.readString();
        MODELPIC = in.readString();
        IsSelect = in.readByte() != 0;
    }

    public static final Creator<My_Bike_Model> CREATOR = new Creator<My_Bike_Model>() {
        @Override
        public My_Bike_Model createFromParcel(Parcel in) {
            return new My_Bike_Model(in);
        }

        @Override
        public My_Bike_Model[] newArray(int size) {
            return new My_Bike_Model[size];
        }
    };

    public String getMY_BIKE_ID() {
        return MY_BIKE_ID;
    }

    public void setMY_BIKE_ID(String MY_BIKE_ID) {
        this.MY_BIKE_ID = MY_BIKE_ID;
    }

    public String getBRANDID() {
        return BRANDID;
    }

    public void setBRANDID(String BRANDID) {
        this.BRANDID = BRANDID;
    }

    public String getMODELID() {
        return MODELID;
    }

    public void setMODELID(String MODELID) {
        this.MODELID = MODELID;
    }

    public String getBRANDNAME() {
        return BRANDNAME;
    }

    public void setBRANDNAME(String BRANDNAME) {
        this.BRANDNAME = BRANDNAME;
    }

    public String getMODELNAME() {
        return MODELNAME;
    }

    public void setMODELNAME(String MODELNAME) {
        this.MODELNAME = MODELNAME;
    }

    public String getBRANDPIC() {
        return BRANDPIC;
    }

    public void setBRANDPIC(String BRANDPIC) {
        this.BRANDPIC = BRANDPIC;
    }

    public String getMODELPIC() {
        return MODELPIC;
    }

    public void setMODELPIC(String MODELPIC) {
        this.MODELPIC = MODELPIC;
    }

    public boolean isSelect() {
        return IsSelect;
    }

    public void setSelect(boolean select) {
        IsSelect = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MY_BIKE_ID);
        dest.writeString(BRANDID);
        dest.writeString(MODELID);
        dest.writeString(BRANDNAME);
        dest.writeString(MODELNAME);
        dest.writeString(BRANDPIC);
        dest.writeString(MODELPIC);
        dest.writeByte((byte) (IsSelect ? 1 : 0));
    }
}
