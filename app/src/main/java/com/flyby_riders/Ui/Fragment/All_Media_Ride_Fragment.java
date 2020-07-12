package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;


public class All_Media_Ride_Fragment extends Fragment {

    public All_Media_Ride_Fragment() {
    }



    public static All_Media_Ride_Fragment newInstance() {
        All_Media_Ride_Fragment fragment = new All_Media_Ride_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all__media__ride, container, false);
        return  v;
    }
}