package com.flyby_riders.Ui.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String TIMEING = "TIMEING";
    public Intent intent;
    public LocationRequest request;
    public FusedLocationProviderClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(ACTION_LOCATION_BROADCAST);
        request = new LocationRequest();
        request.setInterval(2000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(LocationService.this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
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



    private void requestLocationUpdates() {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                    intent.putExtra(TIMEING, String.valueOf(location.getElapsedRealtimeNanos()));
                    intent.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
                    intent.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));
                    intent.putExtra(EXTRA_SPEED, String.valueOf(location.getSpeed()));
                    LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
                }
            }, null);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY_COMPATIBILITY;
    }
}