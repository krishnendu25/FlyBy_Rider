package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 24,June,2020
 */
public class Album_Content_Model implements Parcelable
{
    String ALBUM_NAME;
    String ALBUM_ID;
    ArrayList<String> BIKEIDocMAGES = new ArrayList<>();

    public Album_Content_Model() {
    }

    protected Album_Content_Model(Parcel in) {
        ALBUM_NAME = in.readString();
        ALBUM_ID = in.readString();
        BIKEIDocMAGES = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ALBUM_NAME);
        dest.writeString(ALBUM_ID);
        dest.writeStringList(BIKEIDocMAGES);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album_Content_Model> CREATOR = new Creator<Album_Content_Model>() {
        @Override
        public Album_Content_Model createFromParcel(Parcel in) {
            return new Album_Content_Model(in);
        }

        @Override
        public Album_Content_Model[] newArray(int size) {
            return new Album_Content_Model[size];
        }
    };

    public String getALBUM_NAME() {
        return ALBUM_NAME;
    }

    public void setALBUM_NAME(String ALBUM_NAME) {
        this.ALBUM_NAME = ALBUM_NAME;
    }

    public String getALBUM_ID() {
        return ALBUM_ID;
    }

    public void setALBUM_ID(String ALBUM_ID) {
        this.ALBUM_ID = ALBUM_ID;
    }

    public ArrayList<String> getBIKEIDocMAGES() {
        return BIKEIDocMAGES;
    }

    public void setBIKEIDocMAGES(ArrayList<String> BIKEIDocMAGES) {
        this.BIKEIDocMAGES = BIKEIDocMAGES;
    }
}
