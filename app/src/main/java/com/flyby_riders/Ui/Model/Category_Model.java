package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category_Model implements Parcelable {
    String Name,ID;
    boolean isSelect=false;

    public Category_Model() {

    }

    public Category_Model(String name, String ID) {
        Name = name;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Category_Model{" +
                "Name='" + Name + '\'' +
                ", ID='" + ID + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    protected Category_Model(Parcel in) {
        Name = in.readString();
        ID = in.readString();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<Category_Model> CREATOR = new Creator<Category_Model>() {
        @Override
        public Category_Model createFromParcel(Parcel in) {
            return new Category_Model(in);
        }

        @Override
        public Category_Model[] newArray(int size) {
            return new Category_Model[size];
        }
    };

    public String getName() {
        return Name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(ID);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
