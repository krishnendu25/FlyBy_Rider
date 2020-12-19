/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flyby_riders.Ui.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flyby_riders.GlobalApplication;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.SQLite_DatabaseHelper.TestAdapter;
import com.flyby_riders.Ui.Activity.RideMapView;
import com.flyby_riders.Utils.NotificationMannager;
import com.flyby_riders.Utils.Prefe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.Constant.GET_timeStamp;
import static com.flyby_riders.Constants.StringUtils.RIDE_IS_BACKGROUND;
import static com.flyby_riders.Constants.StringUtils.RIDE_STARTED;

public class LocationUpdatesServiceV2 extends Service {
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String TIMEING = "TIMEING";
    private int RIDER_NOTIFICATION = 85;
    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";
    private TestAdapter testAdapter;
    private static final String TAG = LocationUpdatesServiceV2.class.getSimpleName();

    private static final String CHANNEL_ID = "channel_01";

    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final int NOTIFICATION_ID = 12345678;

    private boolean mChangingConfiguration = false;

    private NotificationMannager notificationMannager;

    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    private Location mLocation;

    public LocationUpdatesServiceV2() {
    }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(GlobalApplication.getInstance());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        notificationMannager = new NotificationMannager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try{
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(RIDER_NOTIFICATION);
        }catch (Exception e){ e.printStackTrace(); }
        if (!mChangingConfiguration && new Prefe(GlobalApplication.getInstance()).requestingLocationUpdates()) {
            startForeground(NOTIFICATION_ID,getNotification() );
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }
    private Notification getNotification() {
        if (notificationMannager!=null)
        return notificationMannager.controllerNotification(true,false).build();
        else
            return null;
    }
    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        try{new Prefe(GlobalApplication.getInstance()).setRequestingLocationUpdates(true);}catch (Exception E){}

        startService(new Intent(GlobalApplication.getInstance(), LocationUpdatesServiceV2.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            new Prefe(GlobalApplication.getInstance()).setRequestingLocationUpdates(false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            new Prefe(GlobalApplication.getInstance()).setRequestingLocationUpdates(false);
            stopSelf();
        } catch (SecurityException unlikely) {
            new Prefe(GlobalApplication.getInstance()).setRequestingLocationUpdates(true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }
    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }
    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);
        mLocation = location;
        inBackGroundTrack(location);
        //shearMyLocationToOther(location);
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(TIMEING, String.valueOf(location.getElapsedRealtimeNanos()));
        intent.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
        intent.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));
        intent.putExtra(EXTRA_SPEED, String.valueOf(location.getSpeed()));
        updateNotificationValues();
        LocalBroadcastManager.getInstance(GlobalApplication.getInstance()).sendBroadcast(intent);
    }

    private void updateNotificationValues() {
        try {



         /*   if (notificationMannager!=null)
                notificationMannager.updateNotificationView(new Prefe(GlobalApplication.getInstance()).getRideTrackStatus(), String.valueOf(location.getSpeed()*3.6), "time");
 */       } catch (Exception e) {
        }
    }

    private void shearMyLocationToOther(Location location) {
        if (new Prefe(GlobalApplication.getInstance()).getRideTrackStatus().equalsIgnoreCase(RIDE_STARTED) &&
                new Prefe(GlobalApplication.getInstance()).getisLocationVisibleToOther()) {
            if (new Prefe(GlobalApplication.getInstance()).getRideID() != null) {
                if (!new Prefe(GlobalApplication.getInstance()).getRideID().equalsIgnoreCase("")) {
                    hit_update_My_Location(location.getLatitude(), location.getLongitude());
                }
            }
        }
    }

    private void hit_update_My_Location(double mlattitude, double mlongitude) {
        RetrofitCallback retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        Call<ResponseBody> requestCall = retrofitCallback.location_tracker(String.valueOf(mlattitude), String.valueOf(mlongitude), new Prefe(GlobalApplication.getInstance()).getRideID(), new Prefe(this).getUserID(), GET_timeStamp());
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */

    public class LocalBinder extends Binder {
        public LocationUpdatesServiceV2 getService() {
            return LocationUpdatesServiceV2.this;
        }
    }


    private void inBackGroundTrack(Location location) {

        if (testAdapter==null){
            testAdapter = new TestAdapter(GlobalApplication.getInstance());
            testAdapter.createDatabase();
            testAdapter.open();
        }
        String userID = new Prefe(GlobalApplication.getInstance()).getUserID();
        if (new Prefe(GlobalApplication.getInstance()).getRideTrackStatus().equalsIgnoreCase(RIDE_STARTED) &&
                new Prefe(GlobalApplication.getInstance()).getRideRecordStatus()){
            testAdapter.INSERT_REALTIMELOCATION(new Prefe(GlobalApplication.getInstance()).getRideID(),userID,String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),GET_timeStamp());
            double Real_Time_Speed = Double.parseDouble(String.valueOf(location.getSpeed()));//meters/second
            double Real_Time_Speed_kmph = Real_Time_Speed * 3.6;
            testAdapter.INSERT_RIDE_DATA(new Prefe(GlobalApplication.getInstance()).getRideID(), new Prefe(GlobalApplication.getInstance()).getUserID(),String.valueOf(Real_Time_Speed_kmph), String.valueOf(Real_Time_Speed_kmph), GET_timeStamp(), RIDE_STARTED);
            if (notificationMannager != null)
                notificationMannager.controllerNotification(true, false);
        }
        Log.e("@@@@@@","BackGround-->"+" "+location.getLatitude());
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try{
            Log.e("@@@@@@","service terminate-->"+" ");
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(RIDER_NOTIFICATION);
        }catch (Exception e){}
    }
}
