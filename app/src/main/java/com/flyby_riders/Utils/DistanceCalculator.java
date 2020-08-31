package com.flyby_riders.Utils;

import android.location.Location;

import java.text.DecimalFormat;

/**
 * Created by KRISHNENDU MANNA on 30,August,2020
 */
public class DistanceCalculator
{


    public static String  distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return String.valueOf(0);
        }
        else {
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(lat1);
            startPoint.setLongitude(lon1);
            Location endPoint = new Location("locationb");
            endPoint.setLatitude(lat2);
            endPoint.setLongitude(lon2);
            double Dis = Double.valueOf(startPoint.distanceTo(endPoint)) * 0.001;
            return String.valueOf(new DecimalFormat("##.##").format(Dis));
        }
    }
}