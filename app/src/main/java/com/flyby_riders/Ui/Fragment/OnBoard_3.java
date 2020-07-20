package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoard_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoard_3 extends Fragment {

    public OnBoard_3() {
        // Required empty public constructor
    }


    public static OnBoard_3 newInstance() {
        OnBoard_3 fragment = new OnBoard_3();

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
        return inflater.inflate(R.layout.fragment_on_board_3, container, false);
    }
}
