package com.flyby_riders.Ui.Listener;

public interface NotificationCallback {
    public void updateNotificationView(String rideStatus,String distance,String Time);
    public void controllerNotification(boolean isCreate,boolean isDestroyed);
}
