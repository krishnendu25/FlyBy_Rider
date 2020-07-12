package com.flyby_riders.Sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Choose_Way_Screen;

public class Session {

    private static final String LOGIN_PHONE_NO = "PHONE_NO";
    private static final String LOGIN_USER_ID = "user_id";
    private static final String IsLogin="";
    private static final String Is_onBoarding_visit = "false";
    private static final String GRAGE_PHOTO = "GRAGE_PHOTO";


    public static SharedPreferences sPrfed_State_Saver;
    SharedPreferences.Editor sPrfed_State_Saver_editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public Session(Context context) {
        this._context = context;
        sPrfed_State_Saver = _context.getSharedPreferences("State_Saver", PRIVATE_MODE);
        sPrfed_State_Saver_editor = sPrfed_State_Saver.edit();
    }

    //LogOut
    public static void Direct_Logout(final Context c) {
        sPrfed_State_Saver.edit().clear().commit();
        Activity activity = (Activity) c;
        Intent intent = new Intent(activity, Choose_Way_Screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    public String get_LOGIN_PHONE_NO() {
        return sPrfed_State_Saver.getString(LOGIN_PHONE_NO, "");
    }
    public void set_LOGIN_PHONE_NO(String fd) {
        sPrfed_State_Saver_editor.putString(LOGIN_PHONE_NO, fd);
        sPrfed_State_Saver_editor.commit();
    }

    public String get_onBoarding() {
        return sPrfed_State_Saver.getString(Is_onBoarding_visit, "false");
    }
    public String get_GRAGE_PHOTO() {
        return sPrfed_State_Saver.getString(GRAGE_PHOTO, "");
    }

    public String get_LOGIN_USER_ID() {
        return sPrfed_State_Saver.getString(LOGIN_USER_ID, "");
    }

    public void set_LOGIN_USER_ID(String tm) {
        sPrfed_State_Saver_editor.putString(LOGIN_USER_ID, tm);
        sPrfed_State_Saver_editor.commit();
    }
    public void set_GRAGE_PHOTO(String tm) {
        sPrfed_State_Saver_editor.putString(GRAGE_PHOTO, tm);
        sPrfed_State_Saver_editor.commit();
    }
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

}