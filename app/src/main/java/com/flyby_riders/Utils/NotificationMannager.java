package com.flyby_riders.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.DashBoard;
import com.flyby_riders.Ui.Listener.NotificationCallback;

public class NotificationMannager implements NotificationCallback {
    private final NotificationManager notificationmanager;
    private Activity activity;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private int RIDER_NOTIFICATION = 85;
    private PendingIntent pIntent;
    String channel="";
    public NotificationMannager(Activity activity) {
        this.activity = activity;
        notificationmanager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    @Override
    public void updateNotificationView(String rideStatus, String distance, String Time) {
        remoteViews.setTextViewText(R.id.Duration_noti,"Duration : " +Time);
        remoteViews.setTextViewText(R.id.Total_distance_tv,"Distance : " +distance+" KM");
        remoteViews.setTextViewText(R.id.notisatust,rideStatus);
        NotificationManagerCompat.from(activity).notify(RIDER_NOTIFICATION, builder.build());
    }

    @Override
    public void controllerNotification(boolean isCreate, boolean isDestroyed) {
        if (isCreate) {
            buildNotification();
        } else if (isDestroyed) {
            notificationmanager.cancel(RIDER_NOTIFICATION);
        }


    }


    private void buildNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else
            channel = "";
        remoteViews = new RemoteViews(activity.getPackageName(), R.layout.notification_view_ride);
        Intent intent = new Intent(activity, DashBoard.class);
        pIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(activity, channel)
                .setContentTitle(activity.getString(R.string.app_name))
                .setContentText(activity.getString(R.string.app_name))
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setCustomBigContentView(remoteViews)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pIntent)
                .setSmallIcon(R.mipmap.ic_appiconrider)
                .setContent(remoteViews);
        notificationmanager.notify(RIDER_NOTIFICATION, builder.build());
    }


    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (notificationmanager != null) {
            notificationmanager.createNotificationChannel(mChannel);
        }
        return "snap map channel";
    }

}
