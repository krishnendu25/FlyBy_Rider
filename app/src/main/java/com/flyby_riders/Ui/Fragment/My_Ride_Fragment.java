package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Activity.Create_Group_Ride;
import com.flyby_riders.Ui.Activity.Ride_Members_Management;
import com.flyby_riders.Ui.Adapter.My_Ride_Adapter;
import com.flyby_riders.Ui.Adapter.Ride_Members_Adapter;
import com.flyby_riders.Ui.Model.My_Ride_Model;
import com.flyby_riders.Ui.Model.Ride_Member_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Ride_Fragment extends Fragment {
    public RetrofitCallback retrofitCallback;
    ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    private AlertDialog alertDialog_loader = null;
    RecyclerView MyRide_ListRecyclerView;
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
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        TextView Create_Ride_tv = (TextView) v.findViewById(R.id.Create_Ride_tv);
         MyRide_ListRecyclerView = (RecyclerView) v.findViewById(R.id.MyRide_List);
        MyRide_ListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        Create_Ride_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Create_Group_Ride.class));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        hit_my_ride(new Session(getContext()).get_LOGIN_USER_ID());
    }

    private void hit_my_ride(String user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_fetch_my_ride(user_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MyRide_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            JSONArray RIDEDETAILS_LIST = jsonObject.getJSONArray("RIDEDETAILS");
                            for (int i = 0; i < RIDEDETAILS_LIST.length(); i++) {
                                My_Ride_Model RD = new My_Ride_Model();

                                RD.setRide_Name("Ride-"+String.valueOf(Constant.generateRandomNumber()));
                                RD.setRide_Total_Member(String.valueOf(Constant.generateRandomNumber()));
                                RD.setRide_Start_Date(RIDEDETAILS_LIST.getJSONObject(i).getString("RIDECREATION"));
                                RD.setRide_Total_Distance("21KMs");
                                RD.setRide_Cover_pic("https://firebasestorage.googleapis.com/v0/b/flyby-riders.appspot.com/o/images.jpg?alt=media&token=97270133-971a-429b-86e4-135ae75677ab");
                                RD.setRide_Status("ACTIVE");
                                RD.setRide_Admin_Id(RIDEDETAILS_LIST.getJSONObject(i).getString("ADMINID"));
                                RD.setRide_ID(RIDEDETAILS_LIST.getJSONObject(i).getString("RIDEID"));
                                RD.setTotal_media("23");
                                MyRide_List.add(RD);
                            }
                            Collections.reverse(MyRide_List);

                            My_Ride_Adapter my_ride_adapter = new My_Ride_Adapter(getActivity(),MyRide_List);
                            MyRide_ListRecyclerView.setAdapter(my_ride_adapter);

                        } else {
                            Constant.Show_Tos(getContext(), "No Ride Found");
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        Constant.Show_Tos(getContext(), "No Ride Found");
                        hide_ProgressDialog();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos(getContext(), "Something Wrong");
            }
        });


    }


    public void show_ProgressDialog() {
        try {
            try {
                if (alertDialog_loader != null) {
                    alertDialog_loader.show();
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = (this).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.loading_page, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);
                    LottieAnimationView LottieAnimationView = dialogView.findViewById(R.id.LottieAnimationView);
                    alertDialog_loader = dialogBuilder.create();
                    alertDialog_loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    LottieAnimationView.setAnimation("bike_loader.json");
                    LottieAnimationView.playAnimation();
                    alertDialog_loader.show();
                }
            } catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        } catch (Exception e) {

        }


    }

    public void hide_ProgressDialog() {
        try {
            try {
                if (alertDialog_loader != null) {
                    alertDialog_loader.hide();
                }
            } catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        } catch (Exception e) {

        }
    }
}