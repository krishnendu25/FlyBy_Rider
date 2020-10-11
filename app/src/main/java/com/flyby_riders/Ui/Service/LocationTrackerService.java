package com.flyby_riders.Ui.Service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by KRISHNENDU MANNA on 30,June,2020
 */


public class LocationTrackerService extends Service {

    private static final String TAG = LocationTrackerService.class.getSimpleName();
    public static final String ACTION_LOCATION_BROADCAST = LocationTrackerService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String TIMEING = "TIMEING";
    private LocationListener locationListener;
    private GpsStatus.Listener gpsListener;
    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("@LocationTrackerService",location.toString());
                Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                intent.putExtra(TIMEING, String.valueOf(location.getElapsedRealtimeNanos()));
                intent.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
                intent.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));
                intent.putExtra(EXTRA_SPEED, String.valueOf(location.getSpeed()));
                LocalBroadcastManager.getInstance(LocationTrackerService.this).sendBroadcast(intent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle bundle) {
                Log.e("@LocationTrackerService",provider.toString());
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("@LocationTrackerService",provider.toString());

            }

            @Override
            public void onProviderDisabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    LocationTrackerService.this.stopSelf();
                }
            }
        };

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LocationManager loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LocationManager loc = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        loc.removeUpdates(locationListener);
        loc.removeGpsStatusListener(gpsListener);
        locationListener = null;
        gpsListener = null;
        Log.e("@onDestroy","hj");
        super.onDestroy();
    }

    private TrackingBinder binder;

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null)
            binder = new TrackingBinder();
        return this.binder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }

    public class TrackingBinder extends Binder {

        public void requestUpdates(LocationTrackerService callback){
            if (callback == null)
                throw new NullPointerException("[callback] can't be null!");
           callback = callback;
        }
    }
}