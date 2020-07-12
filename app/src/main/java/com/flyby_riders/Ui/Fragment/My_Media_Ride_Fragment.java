package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Media_Ride_Fragment extends Fragment {
    private AlertDialog alertDialog_loader = null;
    private RetrofitCallback retrofitCallback;
    RecyclerView my_uploaded_list;
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
        View view = inflater.inflate(R.layout.fragment_my__media__ride_, container, false);
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        my_uploaded_list= view.findViewById(R.id.my_uploaded_list);
        my_uploaded_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return view;
    }


}