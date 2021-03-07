package com.flyby_riders.Ui.Listener;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 30,August,2020
 */
public interface DirectionPointListener {
    public void onPath(ArrayList<LatLng> arrayList);
}