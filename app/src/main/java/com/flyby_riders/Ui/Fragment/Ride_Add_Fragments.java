package com.flyby_riders.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.RideMapView;

import java.util.Objects;

public class Ride_Add_Fragments extends Fragment {

Button Add_my_ride;
    public Ride_Add_Fragments() {
    }

    public static Ride_Add_Fragments newInstance() {
        Ride_Add_Fragments fragment = new Ride_Add_Fragments();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_ride_add_fragments, container, false);

        Add_my_ride = view.findViewById(R.id.Add_my_ride);
        Add_my_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager pm = (PowerManager) Objects.requireNonNull(getActivity()).getSystemService(Context.POWER_SERVICE);
                if (pm.isIgnoringBatteryOptimizations("com.flyby_riders")) {
                    startActivity(new Intent(getActivity(), RideMapView.class));
                }else {
                    Constant.openBatteryOptmized(getActivity());
                }
            }
        });

        return view;
    }
}