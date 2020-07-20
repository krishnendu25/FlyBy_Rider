package com.flyby_riders.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyby_riders.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoard_4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoard_4 extends Fragment {

    public OnBoard_4() {
        // Required empty public constructor
    }


    public static OnBoard_4 newInstance() {
        OnBoard_4 fragment = new OnBoard_4();

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
        return inflater.inflate(R.layout.fragment_on_board_4, container, false);
    }
}
