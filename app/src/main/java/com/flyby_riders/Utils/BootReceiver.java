package com.flyby_riders.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.flyby_riders.Ui.Service.LocationService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, LocationService.class);
        context.startService(serviceIntent);
    }
}