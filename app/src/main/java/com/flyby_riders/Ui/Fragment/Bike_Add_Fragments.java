package com.flyby_riders.Ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Bike_Brand_Activity;

public class Bike_Add_Fragments extends Fragment {
    public Bike_Add_Fragments() {
    }
    public static Bike_Add_Fragments newInstance() {
        Bike_Add_Fragments fragment = new Bike_Add_Fragments();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bike__add, container, false);
       Button Bike_Add_btn = view.findViewById(R.id.Bike_Add_btn);
        Bike_Add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Bike_Brand_Activity.class));
            }
        });


        return view;
    }
}
