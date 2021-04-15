package com.flyby_riders.Ui.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Activity.LocationPermissionsAllTime;
import com.flyby_riders.Ui.Activity.LocationPermissionsWindow;
import com.flyby_riders.Ui.Activity.UpgradeAccountPlan;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Activity.RideMapView;
import com.flyby_riders.Ui.Adapter.Ride.My_Ride_Adapter;
import com.flyby_riders.Ui.Listener.RemoveBikeRide;
import com.flyby_riders.Ui.Model.My_Ride_Model;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.POWER_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.flyby_riders.Constants.StringUtils.PREMIUM;
import static com.flyby_riders.Constants.StringUtils.RIDE_ENDED;
import static com.flyby_riders.Constants.StringUtils.RIDE_NOT_STARTED;
import static com.flyby_riders.Constants.StringUtils.RIDE_STARTED;

public class My_Ride_Fragment extends Fragment  {
    private static My_Ride_Fragment fragment;
    public RetrofitCallback retrofitCallback;
    ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    RecyclerView MyRide_ListRecyclerView;
    private AlertDialog alertDialog_loader = null;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout emptyView_Li;
    RelativeLayout shimmerView;
    private My_Ride_Adapter my_ride_adapter;
    private SwipeRefreshLayout refreshPull;
    public My_Ride_Fragment() {
    }

    public static My_Ride_Fragment newInstance(String param1, String param2) {
         fragment = new My_Ride_Fragment();

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
        shimmer_view_container = v.findViewById(R.id.shimmer_view_container);
        emptyView_Li = v.findViewById(R.id.emptyView_Li);
        shimmerView = v.findViewById(R.id.shimmerView);
        refreshPull = v.findViewById(R.id.refreshPull);
        TextView Create_Ride_tv = (TextView) v.findViewById(R.id.Create_Ride_tv);
        MyRide_ListRecyclerView = (RecyclerView) v.findViewById(R.id.MyRide_List);
        MyRide_ListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Create_Ride_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PowerManager pm = (PowerManager) Objects.requireNonNull(getActivity()).getSystemService(Context.POWER_SERVICE);
                        if (pm != null && !pm.isIgnoringBatteryOptimizations(getContext().getPackageName())) {
                            Constant.openBatteryOptmized(getActivity());
                        }else {
                            if (new Prefe(getActivity()).getAccountPlanStatus().equalsIgnoreCase(PREMIUM)){
                                startActivity(new Intent(getActivity(), RideMapView.class));
                            }else {
                                if (MyRide_List.size() > 2) {
                                    try {
                                        hit_notRide_Bottomsheet();
                                    } catch (Exception e) {
                                    }
                                }else{
                                    startActivity(new Intent(getActivity(), RideMapView.class));
                                }
                            }
                        }
                    }else {
                        if (new Prefe(getActivity()).getAccountPlanStatus().equalsIgnoreCase(PREMIUM)){
                            startActivity(new Intent(getActivity(), RideMapView.class));
                        }else {
                            if (MyRide_List.size() > 2) {
                                try {
                                    hit_notRide_Bottomsheet();
                                } catch (Exception e) {
                                }
                            }else{
                                startActivity(new Intent(getActivity(), RideMapView.class));
                            }
                        }
                    }



                } catch (Exception e) {
                    startActivity(new Intent(getActivity(), RideMapView.class));
                }

            }
        });

        refreshPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hit_my_ride(new Prefe(getContext()).getUserID());
                refreshPull.setRefreshing(false);
            }
        });
        return v;
    }



    private void hit_notRide_Bottomsheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.can_add_ride, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(dialogView);
        TextView view_plan_details = bottomSheetDialog.findViewById(R.id.view_plan_details);
        view_plan_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UpgradeAccountPlan.class);
                startActivity(i);
                bottomSheetDialog.hide();
            }
        });
        bottomSheetDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION)== PackageManager.PERMISSION_GRANTED){
                }else{
                    Constant.Show_Tos(getActivity(),"Allow all the time - Permission For Ride");
                    Intent intent = new Intent(getActivity(), LocationPermissionsAllTime.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
        }


        hit_my_ride(new Prefe(getContext()).getUserID());
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
                                if (JS.getString("TRACKSTATUS").equalsIgnoreCase("NOT STARTED"))
                                { myRideModel.setTRACKSTATUS(RIDE_NOT_STARTED);
                                }else if (JS.getString("TRACKSTATUS").equalsIgnoreCase("STARTED"))
                                {myRideModel.setTRACKSTATUS(RIDE_STARTED);
                                }else if (JS.getString("TRACKSTATUS").equalsIgnoreCase("END"))
                                {myRideModel.setTRACKSTATUS(RIDE_ENDED);
                                }
                                myRideModel.setSTARTLAT(JS.getString("STARTLAT"));
                                myRideModel.setSTARTLANG(JS.getString("STARTLANG"));
                                myRideModel.setENDLAT(JS.getString("ENDLAT"));
                                myRideModel.setENDLANG(JS.getString("ENDLANG"));
                                myRideModel.setTOTMEMBER(JS.getString("TOTMEMBER"));
                                myRideModel.setSTARTTIME(JS.getString("STARTTIME"));
                                
                                if (!JS.getString("RIDENAME").equalsIgnoreCase("null"))
                                MyRide_List.add(myRideModel);
                            }
                            setRideAdapter(MyRide_List);
                        } else {
                            setRideAdapter(new ArrayList<>());
                            Constant.Show_Tos(getContext(), "No Ride Found");
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        setRideAdapter(new ArrayList<>());
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                setRideAdapter(new ArrayList<>());
                Constant.Show_Tos_Error(getActivity(), true, false);
            }
        });


    }
    public void show_ProgressDialog() {
        shimmer_view_container.startShimmer();
        shimmerView.setVisibility(View.VISIBLE);
    }
    public void hide_ProgressDialog() {
        shimmer_view_container.stopShimmer();
        shimmerView.setVisibility(View.GONE);
    }


    private void setRideAdapter( ArrayList<My_Ride_Model> list){
        if (list.size()>0){
            Collections.reverse(list);
            emptyView_Li.setVisibility(View.GONE);
            my_ride_adapter = new My_Ride_Adapter(getActivity(),list,getActivity(),fragment);
            MyRide_ListRecyclerView.setAdapter(my_ride_adapter);
        }else{
            emptyView_Li.setVisibility(View.VISIBLE);
            if (my_ride_adapter!=null){
                my_ride_adapter.notifyDataSetChanged();
            }
        }
    }


}