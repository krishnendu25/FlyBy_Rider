package com.flyby_riders.Ui.Service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flyby_riders.SQLite_DatabaseHelper.TestAdapter;
import com.flyby_riders.Utils.Prefe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static com.flyby_riders.Constants.Constant.GET_timeStamp;
import static com.flyby_riders.Constants.StringUtils.RIDE_IS_BACKGROUND;
import static com.flyby_riders.Constants.StringUtils.RIDE_STARTED;

public class LocationService extends Service {
    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_SPEED = "EXTRA_SPEED";
    public static final String TIMEING = "TIMEING";
    public Intent intent;
    public LocationRequest request;
    public FusedLocationProviderClient client;
    private Prefe mPrefe;
    private int RIDER_NOTIFICATION = 85;
    private TestAdapter testAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
        mPrefe = new Prefe(this);

        restartLocationService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        Log.e("@@@@@@","service onDestroy-->"+" ");
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Log.e("@@@@@@","requestLocationUpdates-->"+" "+location.getLatitude());
                if (mPrefe.getRideStatus().equalsIgnoreCase(RIDE_IS_BACKGROUND)){
                    inBackGroundTrack(location);
                }
                Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                intent.putExtra(TIMEING, String.valueOf(location.getElapsedRealtimeNanos()));
                intent.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
                intent.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));
                intent.putExtra(EXTRA_SPEED, String.valueOf(location.getSpeed()));
                LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
            }
        }, null);





    }

    private void inBackGroundTrack(Location location) {
       if (mPrefe==null){
           mPrefe = new Prefe(this);
       }
       if (testAdapter==null){
           testAdapter = new TestAdapter(this);
           testAdapter.createDatabase();
           testAdapter.open();
       }
       String userID = mPrefe.getUserID();
       if (mPrefe.getRideTrackStatus().equalsIgnoreCase(RIDE_STARTED)){
           testAdapter.INSERT_REALTIMELOCATION(mPrefe.getRideID(),userID,String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),GET_timeStamp());
           double Real_Time_Speed = Double.parseDouble(String.valueOf(location.getSpeed()));//meters/second
           double Real_Time_Speed_kmph = Real_Time_Speed * 3.6;
           testAdapter.INSERT_RIDE_DATA(mPrefe.getRideID(), new Prefe(getApplicationContext()).getUserID(),String.valueOf(Real_Time_Speed_kmph), String.valueOf(Real_Time_Speed_kmph), GET_timeStamp(), RIDE_STARTED);
       }

       Log.e("@@@@@@","BackGround-->"+" "+location.getLatitude());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        hitDoTaskInterval(pendingIntent);
      //  return START_STICKY_COMPATIBILITY;
        return START_STICKY;
    }

    private void hitDoTaskInterval(PendingIntent pendingIntent) {
        restartLocationService();
        trunOnAlerm(getApplicationContext());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try{
            Log.e("@@@@@@","service terminate-->"+" ");
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(RIDER_NOTIFICATION);
        }catch (Exception e){}

    }
    public void restartLocationService(){
        testAdapter = new TestAdapter(this);
        testAdapter.createDatabase();
        testAdapter.open();
        intent = new Intent(ACTION_LOCATION_BROADCAST);
        request = new LocationRequest();
        request.setInterval(3000);
        request.setFastestInterval(1500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(LocationService.this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        }
    }

    private void trunOnAlerm(Context context){
        long repeatPeriod = 3000;
        Intent intent = new Intent(context, LocationService.class);
        long startMillis = System.currentTimeMillis() + repeatPeriod;
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("TAG", "alarm set version O ");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Log.e("TAG", "alarm set version setExact method ");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        } else {
            Log.e("TAG", "alarm set version set method ");
            alarmManager.set(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        }

    }
}