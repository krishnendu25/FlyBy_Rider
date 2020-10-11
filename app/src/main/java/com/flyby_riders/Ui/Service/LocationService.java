package com.flyby_riders.Ui.Service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationService extends Service {
    private static final String PREFIX = LocationService.class.getName() + ".";
    public static final String ACTION_START_COLLECTING = PREFIX + "ACTION_START_COLLECTING";
    public static final String ACTION_STOP_COLLECTING = PREFIX + "ACTION_STOP_COLLECTING";
    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String TIMEING = "TIMEING";
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(ACTION_LOCATION_BROADCAST);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.i("*****", "Location changed");
         //   if (isBetterLocation(loc, previousBestLocation)) {
            //   }
            intent.putExtra(TIMEING, String.valueOf(loc.getElapsedRealtimeNanos()));
            intent.putExtra(EXTRA_LATITUDE, String.valueOf(loc.getLatitude()));
            intent.putExtra(EXTRA_LONGITUDE, String.valueOf(loc.getLongitude()));
            intent.putExtra(EXTRA_SPEED, String.valueOf(loc.getSpeed()));
            LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
        }


        public void onProviderEnabled(String provider) {
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY_COMPATIBILITY;
    }


}