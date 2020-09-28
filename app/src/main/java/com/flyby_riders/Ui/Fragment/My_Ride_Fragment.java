package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
import com.flyby_riders.Ui.Activity.RideMapView;
import com.flyby_riders.Ui.Adapter.Ride.My_Ride_Adapter;
import com.flyby_riders.Ui.Model.My_Ride_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Ride_Fragment extends Fragment {
    public RetrofitCallback retrofitCallback;
    ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    RecyclerView MyRide_ListRecyclerView;
    private AlertDialog alertDialog_loader = null;

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
                startActivity(new Intent(getActivity(), RideMapView.class));
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
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MyRide_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            JSONArray RIDEDETAILS_LIST = jsonObject.getJSONArray("RIDEDETAILS");
                            for (int i = 0; i < RIDEDETAILS_LIST.length(); i++) {
                                My_Ride_Model myRideModel = new My_Ride_Model();
                                JSONObject JS = RIDEDETAILS_LIST.getJSONObject(i);
                                myRideModel.setRIDEID(JS.getString("RIDEID"));
                                myRideModel.setRIDENAME(JS.getString("RIDENAME"));
                                myRideModel.setADMINUSERID(JS.getString("ADMINUSERID"));
                                myRideModel.setCREATIONDATE(JS.getString("CREATIONDATE"));
                                myRideModel.setAVG_SPEED(JS.getString("AVG_SPEED"));
                                myRideModel.setTOP_SPEED(JS.getString("TOP_SPEED"));
                                myRideModel.setTOTALKM(JS.getString("TOTALKM"));
                                myRideModel.setTOTALTIME(JS.getString("TOTALTIME"));
                                myRideModel.setCOUNTIMAGELIST(JS.getString("COUNTIMAGELIST"));
                                myRideModel.setPICMEDIAFILE(JS.getString("PICMEDIAFILE"));
                                myRideModel.setPLANNAME(JS.getString("PLANNAME"));
                                myRideModel.setTRACKSTATUS(JS.getString("TRACKSTATUS"));
                                myRideModel.setSTARTLAT(JS.getString("STARTLAT"));
                                myRideModel.setSTARTLANG(JS.getString("STARTLANG"));
                                myRideModel.setENDLAT(JS.getString("ENDLAT"));
                                myRideModel.setENDLANG(JS.getString("ENDLANG"));
                                myRideModel.setTOTMEMBER(JS.getString("TOTMEMBER"));
                                myRideModel.setSTARTTIME(JS.getString("STARTTIME"));
                                MyRide_List.add(myRideModel);
                            }
                            Collections.reverse(MyRide_List);

                            HashSet<My_Ride_Model> listToSet = new HashSet<My_Ride_Model>(MyRide_List);
                            ArrayList<My_Ride_Model> listWithoutDuplicates = new ArrayList<My_Ride_Model>(listToSet);

                            My_Ride_Adapter my_ride_adapter = new My_Ride_Adapter(getActivity(), listWithoutDuplicates);
                            MyRide_ListRecyclerView.setAdapter(my_ride_adapter);

                        } else {
                            Constant.Show_Tos(getContext(), "No Ride Found");
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        hide_ProgressDialog();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getActivity(), true, false);
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