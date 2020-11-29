package com.flyby_riders.Ui.Listener;

import androidx.core.app.NotificationCompat;

public interface NotificationCallback {
    public void updateNotificationView(String rideStatus,String distance,String Time);
    public NotificationCompat.Builder controllerNotification(boolean isCreate, boolean isDestroyed);
}
