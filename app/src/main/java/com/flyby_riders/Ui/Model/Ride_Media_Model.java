package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ride_Media_Model implements Parcelable
{
    String RIDEID,MEDIAFILE_URL,UPLOADER_ID,TOTALFILESIZE;

    public Ride_Media_Model() {
    }

    protected Ride_Media_Model(Parcel in) {
        RIDEID = in.readString();
        MEDIAFILE_URL = in.readString();
        UPLOADER_ID = in.readString();
        TOTALFILESIZE = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RIDEID);
        dest.writeString(MEDIAFILE_URL);
        dest.writeString(UPLOADER_ID);
        dest.writeString(TOTALFILESIZE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ride_Media_Model> CREATOR = new Creator<Ride_Media_Model>() {
        @Override
        public Ride_Media_Model createFromParcel(Parcel in) {
            return new Ride_Media_Model(in);
        }

        @Override
        public Ride_Media_Model[] newArray(int size) {
            return new Ride_Media_Model[size];
        }
    };

    public String getRIDEID() {
        return RIDEID;
    }

    public void setRIDEID(String RIDEID) {
        this.RIDEID = RIDEID;
    }

    public String getMEDIAFILE_URL() {
        return MEDIAFILE_URL;
    }

    public void setMEDIAFILE_URL(String MEDIAFILE_URL) {
        this.MEDIAFILE_URL = MEDIAFILE_URL;
    }

    public String getUPLOADER_ID() {
        return UPLOADER_ID;
    }

    public void setUPLOADER_ID(String UPLOADER_ID) {
        this.UPLOADER_ID = UPLOADER_ID;
    }

    public String getTOTALFILESIZE() {
        return TOTALFILESIZE;
    }

    public void setTOTALFILESIZE(String TOTALFILESIZE) {
        this.TOTALFILESIZE = TOTALFILESIZE;
    }
}
