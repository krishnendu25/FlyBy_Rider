package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KRISHNENDU MANNA on 12,July,2020
 */
public class My_Ride_Model implements Parcelable {




    private String RIDEID,RIDENAME,ADMINUSERID,CREATIONDATE,AVG_SPEED,
            TOP_SPEED,TOTALKM,TOTALTIME,COUNTIMAGELIST,PICMEDIAFILE
            ,PLANNAME,TRACKSTATUS,STARTLAT,STARTLANG,ENDLAT,ENDLANG,TOTMEMBER;

    public My_Ride_Model() {
    }

    protected My_Ride_Model(Parcel in) {
        RIDEID = in.readString();
        RIDENAME = in.readString();
        ADMINUSERID = in.readString();
        CREATIONDATE = in.readString();
        AVG_SPEED = in.readString();
        TOP_SPEED = in.readString();
        TOTALKM = in.readString();
        TOTALTIME = in.readString();
        COUNTIMAGELIST = in.readString();
        PICMEDIAFILE = in.readString();
        PLANNAME = in.readString();
        TRACKSTATUS = in.readString();
        STARTLAT = in.readString();
        STARTLANG = in.readString();
        ENDLAT = in.readString();
        ENDLANG = in.readString();
        TOTMEMBER = in.readString();
    }

    public static final Creator<My_Ride_Model> CREATOR = new Creator<My_Ride_Model>() {
        @Override
        public My_Ride_Model createFromParcel(Parcel in) {
            return new My_Ride_Model(in);
        }

        @Override
        public My_Ride_Model[] newArray(int size) {
            return new My_Ride_Model[size];
        }
    };

    public String getRIDEID() {
        return RIDEID;
    }

    public void setRIDEID(String RIDEID) {
        this.RIDEID = RIDEID;
    }

    public String getRIDENAME() {
        return RIDENAME;
    }

    public void setRIDENAME(String RIDENAME) {
        this.RIDENAME = RIDENAME;
    }

    public String getADMINUSERID() {
        return ADMINUSERID;
    }

    public void setADMINUSERID(String ADMINUSERID) {
        this.ADMINUSERID = ADMINUSERID;
    }

    public String getCREATIONDATE() {
        return CREATIONDATE;
    }

    public void setCREATIONDATE(String CREATIONDATE) {
        this.CREATIONDATE = CREATIONDATE;
    }

    public String getAVG_SPEED() {
        return AVG_SPEED;
    }

    public void setAVG_SPEED(String AVG_SPEED) {
        this.AVG_SPEED = AVG_SPEED;
    }

    public String getTOP_SPEED() {
        return TOP_SPEED;
    }

    public void setTOP_SPEED(String TOP_SPEED) {
        this.TOP_SPEED = TOP_SPEED;
    }

    public String getTOTALKM() {
        return TOTALKM;
    }

    public void setTOTALKM(String TOTALKM) {
        this.TOTALKM = TOTALKM;
    }

    public String getTOTALTIME() {
        return TOTALTIME;
    }

    public void setTOTALTIME(String TOTALTIME) {
        this.TOTALTIME = TOTALTIME;
    }

    public String getCOUNTIMAGELIST() {
        return COUNTIMAGELIST;
    }

    public void setCOUNTIMAGELIST(String COUNTIMAGELIST) {
        this.COUNTIMAGELIST = COUNTIMAGELIST;
    }

    public String getPICMEDIAFILE() {
        return PICMEDIAFILE;
    }

    public void setPICMEDIAFILE(String PICMEDIAFILE) {
        this.PICMEDIAFILE = PICMEDIAFILE;
    }

    public String getPLANNAME() {
        return PLANNAME;
    }

    public void setPLANNAME(String PLANNAME) {
        this.PLANNAME = PLANNAME;
    }

    public String getTRACKSTATUS() {
        return TRACKSTATUS;
    }

    public void setTRACKSTATUS(String TRACKSTATUS) {
        this.TRACKSTATUS = TRACKSTATUS;
    }

    public String getSTARTLAT() {
        return STARTLAT;
    }

    public void setSTARTLAT(String STARTLAT) {
        this.STARTLAT = STARTLAT;
    }

    public String getSTARTLANG() {
        return STARTLANG;
    }

    public void setSTARTLANG(String STARTLANG) {
        this.STARTLANG = STARTLANG;
    }

    public String getENDLAT() {
        return ENDLAT;
    }

    public void setENDLAT(String ENDLAT) {
        this.ENDLAT = ENDLAT;
    }

    public String getENDLANG() {
        return ENDLANG;
    }

    public void setENDLANG(String ENDLANG) {
        this.ENDLANG = ENDLANG;
    }

    public String getTOTMEMBER() {
        return TOTMEMBER;
    }

    public void setTOTMEMBER(String TOTMEMBER) {
        this.TOTMEMBER = TOTMEMBER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RIDEID);
        dest.writeString(RIDENAME);
        dest.writeString(ADMINUSERID);
        dest.writeString(CREATIONDATE);
        dest.writeString(AVG_SPEED);
        dest.writeString(TOP_SPEED);
        dest.writeString(TOTALKM);
        dest.writeString(TOTALTIME);
        dest.writeString(COUNTIMAGELIST);
        dest.writeString(PICMEDIAFILE);
        dest.writeString(PLANNAME);
        dest.writeString(TRACKSTATUS);
        dest.writeString(STARTLAT);
        dest.writeString(STARTLANG);
        dest.writeString(ENDLAT);
        dest.writeString(ENDLANG);
        dest.writeString(TOTMEMBER);
    }
}
