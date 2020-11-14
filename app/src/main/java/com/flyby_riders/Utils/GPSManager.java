package com.flyby_riders.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.flyby_riders.Ui.Activity.GpsTrunOnWarning;


/**
 * Created by KRISHNENDU MANNA on 13,August,2020
 */

public class GPSManager extends BroadcastReceiver {
    private static  GPSManager gpsManager;
    private LocationManager mlocManager;
    int Count=0;



    public void start(Context context) {
        mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Count=0;
        } else {
            if (Count==0)
            {Count++;
                Intent intent = new Intent(context, GpsTrunOnWarning.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        }








    }

    @Override
    public void onReceive(Context context, Intent intent) {
        start(context);
    }


}