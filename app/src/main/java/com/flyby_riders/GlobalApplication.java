package com.flyby_riders;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;


public class GlobalApplication extends Application {

    public static RetrofitCallback retrofitCallback;
    private static GlobalApplication instance;
    public static  String device_Token="";
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

       /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("@@@", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        device_Token = task.getResult();
                        Log.w("@@@", device_Token);
                    }
                });*/



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