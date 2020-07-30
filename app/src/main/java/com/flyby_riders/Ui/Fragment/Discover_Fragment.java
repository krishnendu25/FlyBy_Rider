package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;

public class Discover_Fragment extends Fragment {
    RecyclerView Show_Catagory;
    public Discover_Fragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static Discover_Fragment newInstance() {
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
        View V= inflater.inflate(R.layout.fragment_discover, container, false);


        return V;
    }
}