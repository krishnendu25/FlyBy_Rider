package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BIKE_BRAND implements Parcelable {
    String NAME , PIC , ID;
    boolean isSelect=false;

    public BIKE_BRAND() {
    }

    protected BIKE_BRAND(Parcel in) {
        NAME = in.readString();
        PIC = in.readString();
        ID = in.readString();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<BIKE_BRAND> CREATOR = new Creator<BIKE_BRAND>() {
        @Override
        public BIKE_BRAND createFromParcel(Parcel in) {
            return new BIKE_BRAND(in);
        }

        @Override
        public BIKE_BRAND[] newArray(int size) {
            return new BIKE_BRAND[size];
        }
    };

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPIC() {
        return PIC;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(NAME);
        dest.writeString(PIC);
        dest.writeString(ID);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
