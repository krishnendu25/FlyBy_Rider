package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;

public class My_Media_Ride_Fragment extends Fragment {


    public My_Media_Ride_Fragment() {

    }


    public static My_Media_Ride_Fragment newInstance() {
        My_Media_Ride_Fragment fragment = new My_Media_Ride_Fragment();

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
        View view = inflater.inflate(R.layout.fragment_my__media__ride_, container, false);

        return view;
    }
}