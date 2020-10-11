package com.flyby_riders;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import java.io.IOException;


public class GlobalApplication extends Application {


    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        int pid = android.os.Process.myPid();
        String whiteList = "logcat -P '" + pid + "'";
        try {
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (IOException e) {
        } catch (Exception e) { }

    }



    private void initApplication()
    {
        instance = this;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}