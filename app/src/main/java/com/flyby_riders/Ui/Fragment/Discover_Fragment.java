package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;

public class Discover_Fragment extends Fragment {

    public Discover_Fragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static Discover_Fragment newInstance(String param1, String param2) {
        Discover_Fragment fragment = new Discover_Fragment();

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
        View V= inflater.inflate(R.layout.fragment_discover, container, false);
        return V;
    }
}