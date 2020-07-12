package com.flyby_riders.Ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Create_Group_Ride;

public class My_Ride_Fragment extends Fragment {


    public My_Ride_Fragment() {
    }

    public static My_Ride_Fragment newInstance(String param1, String param2) {
        My_Ride_Fragment fragment = new My_Ride_Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_my__ride, container, false);

        TextView Create_Ride_tv = (TextView) v.findViewById(R.id.Create_Ride_tv);
        RecyclerView MyRide_List = (RecyclerView) v.findViewById(R.id.MyRide_List);

    Create_Ride_tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         startActivity(new Intent(getActivity(), Create_Group_Ride.class));
        }
    });

        return v;
    }
}