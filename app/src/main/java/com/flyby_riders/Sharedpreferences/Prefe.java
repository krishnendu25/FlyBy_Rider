package com.flyby_riders.Sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.AppLandingView;
import com.flyby_riders.Ui.Activity.LoginView;
import com.flyby_riders.Ui.Model.Contact_Model;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.flyby_riders.Ui.Listener.StringUtils.BASIC;

public class Prefe {

    private static final String LOGIN_PHONE_NO = "PHONE_NO";
    private static final String LOGIN_USER_ID = "user_id";
    private static final String IsLogin="";
    private static final String Is_onBoarding_visit = "false";
    private static final String GRAGE_PHOTO = "GRAGE_PHOTO";
    private static final String Latlong = "latlong";

    private static final String MEMBER_STATUS = BASIC;


    private static final String TRACK_RIDE_ID = "";
    private static final String RIDE_RECORD = "false";


    private static final String LOCAL_ALL_CONTACT="localallcontact";
    private static final String FLYBY_CONTACT="flybycontact";



    public static SharedPreferences sPrfed_State_Saver;
    SharedPreferences.Editor sPrfed_State_Saver_editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public Prefe(Context context) {
        try {
            this._context = context;
            sPrfed_State_Saver = _context.getSharedPreferences("State_Saver", PRIVATE_MODE);
            sPrfed_State_Saver_editor = sPrfed_State_Saver.edit();
        }catch (Exception e)
        {

        }

    }

    //LogOut
    public static void logOut(Context c) {
        sPrfed_State_Saver.edit().clear().apply();
        Constant.Show_Tos(c,"You Have Successfully Logged Out");
        Activity activity = (Activity) c;
        Intent intent = new Intent(activity, LoginView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    public List<Contact_Model> getAllContact() {
        Gson gson = new Gson();
        String json = sPrfed_State_Saver.getString(LOCAL_ALL_CONTACT, "");
        Type type = new TypeToken<List<Contact_Model>>() {
        }.getType();
        return gson.fromJson(json, type);

    }
    public void setAllContact(List<Contact_Model> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        sPrfed_State_Saver_editor.putString(LOCAL_ALL_CONTACT,json).apply();
    }


    public List<FlyBy_Contact_Model> getFlybyContact() {
        Gson gson = new Gson();
        String json = sPrfed_State_Saver.getString(FLYBY_CONTACT, "");
        Type type = new TypeToken<List<FlyBy_Contact_Model>>() {
        }.getType();
        return gson.fromJson(json, type);

    }
    public void setFlybyContact(List<FlyBy_Contact_Model> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        sPrfed_State_Saver_editor.putString(FLYBY_CONTACT,json).apply();
    }




    public String getUserPhoneNO() {
        return sPrfed_State_Saver.getString(LOGIN_PHONE_NO, "");
    }
    public void setUserPhoneNO(String fd) {
        sPrfed_State_Saver_editor.putString(LOGIN_PHONE_NO, fd);
        sPrfed_State_Saver_editor.commit();
    }

    public String get_onBoarding() {
        return sPrfed_State_Saver.getString(Is_onBoarding_visit, "false");
    }

    public String getUserID() {
        return sPrfed_State_Saver.getString(LOGIN_USER_ID, "");
    }
    public void setUserID(String tm) {
        sPrfed_State_Saver_editor.putString(LOGIN_USER_ID, tm);
        sPrfed_State_Saver_editor.commit(); }
    public void set_onBoarding(String tm) {
        sPrfed_State_Saver_editor.putString(Is_onBoarding_visit, tm);
        sPrfed_State_Saver_editor.commit();
    }
    public void set_IsLogin(String tm) {
        sPrfed_State_Saver_editor.putString(IsLogin, tm);
        sPrfed_State_Saver_editor.commit();
    }
    public String get_IsLogin() {
        return sPrfed_State_Saver.getString(IsLogin, "");
    }



    public void setAccountPlanStatus(String tm) {
        sPrfed_State_Saver_editor.putString(MEMBER_STATUS, tm);
        sPrfed_State_Saver_editor.commit();
    }
    public String getAccountPlanStatus() {
        return sPrfed_State_Saver.getString(MEMBER_STATUS, BASIC);
    }



    public void set_TRACK_RIDE_ID(String RIDE_ID) {
        sPrfed_State_Saver_editor.putString(TRACK_RIDE_ID, RIDE_ID);
        sPrfed_State_Saver_editor.commit();
    }
    public String get_TRACK_RIDE_ID() {
        return sPrfed_State_Saver.getString(TRACK_RIDE_ID, "");
    }

    public void set_RIDE_RECORD(String STATUS) {
        sPrfed_State_Saver_editor.putString(RIDE_RECORD, STATUS);
        sPrfed_State_Saver_editor.commit();
    }
    public String get_RIDE_RECORD() {
        return sPrfed_State_Saver.getString(RIDE_RECORD, "false");
    }



    public void set_mylocation(String latlong) {
        sPrfed_State_Saver_editor.putString(Latlong, latlong);
        sPrfed_State_Saver_editor.commit();
    }
    public String get_mylocation() {
        return sPrfed_State_Saver.getString(Latlong, "");
    }

}
