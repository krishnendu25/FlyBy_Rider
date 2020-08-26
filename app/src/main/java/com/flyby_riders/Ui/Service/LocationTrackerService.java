package com.flyby_riders.Ui.Service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.DashBoard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // buildNotification();
        requestLocationUpdates();
    }

    private void buildNotification() {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = createChannel();
        } else {
            channel = "";
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_view_ride);
        Intent intent = new Intent(this, DashBoard.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name))
                .setOngoing(true)
                .setCustomBigContentView(remoteViews)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pIntent)
                .setSmallIcon(R.mipmap.ic_appiconrider)
                .setContent(remoteViews);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };


    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }


    private void requestLocationUpdates() {


        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
             Location location = locationResult.getLastLocation();
                    Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                    intent.putExtra(TIMEING, String.valueOf(location.getElapsedRealtimeNanos()));
                    intent.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
                    intent.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));
                    intent.putExtra(EXTRA_SPEED, String.valueOf(location.getSpeed()));
                    LocalBroadcastManager.getInstance(LocationTrackerService.this).sendBroadcast(intent);
                }
            }, null);
        }
    }

}