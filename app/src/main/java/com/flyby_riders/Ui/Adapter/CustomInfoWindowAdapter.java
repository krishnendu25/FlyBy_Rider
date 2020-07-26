package com.flyby_riders.Ui.Adapter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyby_riders.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by KRISHNENDU MANNA  00/00/2020
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.snippet);

        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {


        return null;
    }
}