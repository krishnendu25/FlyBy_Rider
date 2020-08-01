package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ADD_MODEL implements Parcelable {


  String  ID,ADVID,TITLE,DESC,ADVIDEO,ADCOVERIMAGE,ADCOSTPRICE,AUDIENCE,BIKEMODEL,BIKEMODELID,BIKEBRAND
          ,BIKEBRANDID,STORELINK,CONTACT,NOACTION,CURENTSTATUS,CREATIONDATE,POSTDATE,POSTTIME,PUBLISHDATE,POSTMONTH,EXPIREDDATE,SEENCOUNT,PURCHASECOUNT;
  String IMAGEADPATH,IMAGECOVERPATH,IMAGEVIDEOPATH;
  String PICTURE_1="",PICTURE_2="",PICTURE_3="",PICTURE_4="",PICTURE_5="",PICTURE_6="",PICTURE_7="",PICTURE_8="",ADIMAGES;

    public ADD_MODEL() {
    }


    protected ADD_MODEL(Parcel in) {
        ID = in.readString();
        ADVID = in.readString();
        TITLE = in.readString();
        DESC = in.readString();
        ADVIDEO = in.readString();
        ADCOVERIMAGE = in.readString();
        ADCOSTPRICE = in.readString();
        AUDIENCE = in.readString();
        BIKEMODEL = in.readString();
        BIKEMODELID = in.readString();
        BIKEBRAND = in.readString();
        BIKEBRANDID = in.readString();
        STORELINK = in.readString();
        CONTACT = in.readString();
        NOACTION = in.readString();
        CURENTSTATUS = in.readString();
        CREATIONDATE = in.readString();
        POSTDATE = in.readString();
        POSTTIME = in.readString();
        PUBLISHDATE = in.readString();
        POSTMONTH = in.readString();
        EXPIREDDATE = in.readString();
        SEENCOUNT = in.readString();
        PURCHASECOUNT = in.readString();
        IMAGEADPATH = in.readString();
        IMAGECOVERPATH = in.readString();
        IMAGEVIDEOPATH = in.readString();
        PICTURE_1 = in.readString();
        PICTURE_2 = in.readString();
        PICTURE_3 = in.readString();
        PICTURE_4 = in.readString();
        PICTURE_5 = in.readString();
        PICTURE_6 = in.readString();
        PICTURE_7 = in.readString();
        PICTURE_8 = in.readString();
        ADIMAGES = in.readString();
    }

    public static final Creator<ADD_MODEL> CREATOR = new Creator<ADD_MODEL>() {
        @Override
        public ADD_MODEL createFromParcel(Parcel in) {
            return new ADD_MODEL(in);
        }

        @Override
        public ADD_MODEL[] newArray(int size) {
            return new ADD_MODEL[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getADVID() {
        return ADVID;
    }

    public void setADVID(String ADVID) {
        this.ADVID = ADVID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getADVIDEO() {
        return ADVIDEO;
    }

    public void setADVIDEO(String ADVIDEO) {
        this.ADVIDEO = ADVIDEO;
    }

    public String getADCOVERIMAGE() {
        return ADCOVERIMAGE;
    }

    public void setADCOVERIMAGE(String ADCOVERIMAGE) {
        this.ADCOVERIMAGE = ADCOVERIMAGE;
    }

    public String getADCOSTPRICE() {
        return ADCOSTPRICE;
    }

    public void setADCOSTPRICE(String ADCOSTPRICE) {
        this.ADCOSTPRICE = ADCOSTPRICE;
    }

    public String getAUDIENCE() {
        return AUDIENCE;
    }

    public void setAUDIENCE(String AUDIENCE) {
        this.AUDIENCE = AUDIENCE;
    }

    public String getBIKEMODEL() {
        return BIKEMODEL;
    }

    public void setBIKEMODEL(String BIKEMODEL) {
        this.BIKEMODEL = BIKEMODEL;
    }

    public String getBIKEMODELID() {
        return BIKEMODELID;
    }

    public void setBIKEMODELID(String BIKEMODELID) {
        this.BIKEMODELID = BIKEMODELID;
    }

    public String getBIKEBRAND() {
        return BIKEBRAND;
    }

    public void setBIKEBRAND(String BIKEBRAND) {
        this.BIKEBRAND = BIKEBRAND;
    }

    public String getBIKEBRANDID() {
        return BIKEBRANDID;
    }

    public void setBIKEBRANDID(String BIKEBRANDID) {
        this.BIKEBRANDID = BIKEBRANDID;
    }

    public String getSTORELINK() {
        return STORELINK;
    }

    public void setSTORELINK(String STORELINK) {
        this.STORELINK = STORELINK;
    }

    public String getCONTACT() {
        return CONTACT;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    public String getNOACTION() {
        return NOACTION;
    }

    public void setNOACTION(String NOACTION) {
        this.NOACTION = NOACTION;
    }

    public String getCURENTSTATUS() {
        return CURENTSTATUS;
    }

    public void setCURENTSTATUS(String CURENTSTATUS) {
        this.CURENTSTATUS = CURENTSTATUS;
    }

    public String getCREATIONDATE() {
        return CREATIONDATE;
    }

    public void setCREATIONDATE(String CREATIONDATE) {
        this.CREATIONDATE = CREATIONDATE;
    }

    public String getPOSTDATE() {
        return POSTDATE;
    }

    public void setPOSTDATE(String POSTDATE) {
        this.POSTDATE = POSTDATE;
    }

    public String getPOSTTIME() {
        return POSTTIME;
    }

    public void setPOSTTIME(String POSTTIME) {
        this.POSTTIME = POSTTIME;
    }

    public String getPUBLISHDATE() {
        return PUBLISHDATE;
    }

    public void setPUBLISHDATE(String PUBLISHDATE) {
        this.PUBLISHDATE = PUBLISHDATE;
    }

    public String getPOSTMONTH() {
        return POSTMONTH;
    }

    public void setPOSTMONTH(String POSTMONTH) {
        this.POSTMONTH = POSTMONTH;
    }

    public String getEXPIREDDATE() {
        return EXPIREDDATE;
    }

    public void setEXPIREDDATE(String EXPIREDDATE) {
        this.EXPIREDDATE = EXPIREDDATE;
    }

    public String getSEENCOUNT() {
        return SEENCOUNT;
    }

    public void setSEENCOUNT(String SEENCOUNT) {
        this.SEENCOUNT = SEENCOUNT;
    }

    public String getPURCHASECOUNT() {
        return PURCHASECOUNT;
    }

    public void setPURCHASECOUNT(String PURCHASECOUNT) {
        this.PURCHASECOUNT = PURCHASECOUNT;
    }

    public String getIMAGEADPATH() {
        return IMAGEADPATH;
    }

    public void setIMAGEADPATH(String IMAGEADPATH) {
        this.IMAGEADPATH = IMAGEADPATH;
    }

    public String getIMAGECOVERPATH() {
        return IMAGECOVERPATH;
    }

    public void setIMAGECOVERPATH(String IMAGECOVERPATH) {
        this.IMAGECOVERPATH = IMAGECOVERPATH;
    }

    public String getIMAGEVIDEOPATH() {
        return IMAGEVIDEOPATH;
    }

    public void setIMAGEVIDEOPATH(String IMAGEVIDEOPATH) {
        this.IMAGEVIDEOPATH = IMAGEVIDEOPATH;
    }

    public String getPICTURE_1() {
        return PICTURE_1;
    }

    public void setPICTURE_1(String PICTURE_1) {
        this.PICTURE_1 = PICTURE_1;
    }

    public String getPICTURE_2() {
        return PICTURE_2;
    }

    public void setPICTURE_2(String PICTURE_2) {
        this.PICTURE_2 = PICTURE_2;
    }

    public String getPICTURE_3() {
        return PICTURE_3;
    }

    public void setPICTURE_3(String PICTURE_3) {
        this.PICTURE_3 = PICTURE_3;
    }

    public String getPICTURE_4() {
        return PICTURE_4;
    }

    public void setPICTURE_4(String PICTURE_4) {
        this.PICTURE_4 = PICTURE_4;
    }

    public String getPICTURE_5() {
        return PICTURE_5;
    }

    public void setPICTURE_5(String PICTURE_5) {
        this.PICTURE_5 = PICTURE_5;
    }

    public String getPICTURE_6() {
        return PICTURE_6;
    }

    public void setPICTURE_6(String PICTURE_6) {
        this.PICTURE_6 = PICTURE_6;
    }

    public String getPICTURE_7() {
        return PICTURE_7;
    }

    public void setPICTURE_7(String PICTURE_7) {
        this.PICTURE_7 = PICTURE_7;
    }

    public String getPICTURE_8() {
        return PICTURE_8;
    }

    public void setPICTURE_8(String PICTURE_8) {
        this.PICTURE_8 = PICTURE_8;
    }

    public String getADIMAGES() {
        return ADIMAGES;
    }

    public void setADIMAGES(String ADIMAGES) {
        this.ADIMAGES = ADIMAGES;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(ADVID);
        dest.writeString(TITLE);
        dest.writeString(DESC);
        dest.writeString(ADVIDEO);
        dest.writeString(ADCOVERIMAGE);
        dest.writeString(ADCOSTPRICE);
        dest.writeString(AUDIENCE);
        dest.writeString(BIKEMODEL);
        dest.writeString(BIKEMODELID);
        dest.writeString(BIKEBRAND);
        dest.writeString(BIKEBRANDID);
        dest.writeString(STORELINK);
        dest.writeString(CONTACT);
        dest.writeString(NOACTION);
        dest.writeString(CURENTSTATUS);
        dest.writeString(CREATIONDATE);
        dest.writeString(POSTDATE);
        dest.writeString(POSTTIME);
        dest.writeString(PUBLISHDATE);
        dest.writeString(POSTMONTH);
        dest.writeString(EXPIREDDATE);
        dest.writeString(SEENCOUNT);
        dest.writeString(PURCHASECOUNT);
        dest.writeString(IMAGEADPATH);
        dest.writeString(IMAGECOVERPATH);
        dest.writeString(IMAGEVIDEOPATH);
        dest.writeString(PICTURE_1);
        dest.writeString(PICTURE_2);
        dest.writeString(PICTURE_3);
        dest.writeString(PICTURE_4);
        dest.writeString(PICTURE_5);
        dest.writeString(PICTURE_6);
        dest.writeString(PICTURE_7);
        dest.writeString(PICTURE_8);
        dest.writeString(ADIMAGES);
    }
}
