package com.flyby_riders;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;

import java.io.IOException;


public class GlobalApplication extends Application {

    public static RetrofitCallback retrofitCallback;
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        int pid = android.os.Process.myPid();
        String whiteList = "logcat -P '" + pid + "'";
        try {
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (IOException e) {
        } catch (Exception e) { }

    }
    public static GlobalApplication getInstance() {
        return instance;
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