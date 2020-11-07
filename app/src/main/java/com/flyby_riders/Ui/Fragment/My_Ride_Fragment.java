package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Sharedpreferences.Prefe;
import com.flyby_riders.Ui.Activity.RideMapView;
import com.flyby_riders.Ui.Adapter.Ride.My_Ride_Adapter;
import com.flyby_riders.Ui.Listener.RemoveBikeRide;
import com.flyby_riders.Ui.Model.My_Ride_Model;

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

import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_ENDED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_NOT_STARTED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_STARTED;

public class My_Ride_Fragment extends Fragment implements RemoveBikeRide {
    private static My_Ride_Fragment fragment;
    public RetrofitCallback retrofitCallback;
    ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    RecyclerView MyRide_ListRecyclerView;
    private AlertDialog alertDialog_loader = null;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout emptyView_Li;
    RelativeLayout shimmerView;
    private My_Ride_Adapter my_ride_adapter;

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
        TextView Create_Ride_tv = (TextView) v.findViewById(R.id.Create_Ride_tv);
        MyRide_ListRecyclerView = (RecyclerView) v.findViewById(R.id.MyRide_List);
        MyRide_ListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        Create_Ride_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager pm = (PowerManager) Objects.requireNonNull(getActivity()).getSystemService(Context.POWER_SERVICE);
                if (pm.isIgnoringBatteryOptimizations("com.flyby_riders")) {
                    startActivity(new Intent(getActivity(), RideMapView.class));
                }else {
                    Constant.openBatteryOptmized(getActivity());
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                            Collections.reverse(MyRide_List);
                            HashSet<My_Ride_Model> listToSet = new HashSet<My_Ride_Model>(MyRide_List);
                            ArrayList<My_Ride_Model> listWithoutDuplicates = new ArrayList<My_Ride_Model>(listToSet);
                            setRideAdapter(listWithoutDuplicates);
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

    @Override
    public void removeMyRide(String rideID) {
        removeRideList(rideID);
    }

    private void removeRideList(String rideID) {

    }
}