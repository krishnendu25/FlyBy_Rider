package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Fragment.Bike_Add_Fragments;
import com.flyby_riders.Ui.Fragment.Discover_Fragment;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Fragment.Ride_Add_Fragments;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.ShadowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard extends BaseActivity {
    @BindView(R.id.Account_Btn)
    RelativeLayout AccountBtn;
    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    @BindView(R.id.discover_active)
    TextView discoverActive;
    @BindView(R.id.discover_inactive)
    TextView discoverInactive;
    @BindView(R.id.my_garage_active)
    TextView myGarageActive;
    @BindView(R.id.my_garage_inactive)
    TextView myGarageInactive;
    @BindView(R.id.rides_active)
    TextView ridesActive;
    @BindView(R.id.rides_inactive)
    TextView ridesInactive;
    @BindView(R.id.ShadowLayout_discover)
    ShadowLayout ShadowLayoutDiscover;
    @BindView(R.id.ShadowLayout_my_garage)
    ShadowLayout ShadowLayoutMyGarage;
    @BindView(R.id.ShadowLayout_rides)
    ShadowLayout ShadowLayoutRides;
    public static ArrayList<My_Bike_Model> My_Bike = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Instantiation();


    }
    private void Instantiation() {
        Tab_View_Adjust(ShadowLayoutMyGarage,ShadowLayoutDiscover,ShadowLayoutRides);
    }
    @OnClick({R.id.Account_Btn, R.id.discover_active, R.id.discover_inactive, R.id.my_garage_active, R.id.my_garage_inactive, R.id.rides_active, R.id.rides_inactive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Account_Btn:
                Constant.Show_Tos(this,"NOT IMPLEMENTED");
                break;
            case R.id.discover_active:
                replaceFragment(new Discover_Fragment());
                Tab_View_Adjust(ShadowLayoutDiscover,ShadowLayoutMyGarage,ShadowLayoutRides);
                break;
            case R.id.discover_inactive:
                replaceFragment(new Discover_Fragment());
                Tab_View_Adjust(ShadowLayoutDiscover,ShadowLayoutMyGarage,ShadowLayoutRides);
                break;
            case R.id.my_garage_active:
                Hit_Rider_Details(new Session(this).get_LOGIN_USER_ID());
                Tab_View_Adjust(ShadowLayoutMyGarage,ShadowLayoutRides,ShadowLayoutDiscover);
                break;
            case R.id.my_garage_inactive:
                Hit_Rider_Details(new Session(this).get_LOGIN_USER_ID());
                Tab_View_Adjust(ShadowLayoutMyGarage,ShadowLayoutRides,ShadowLayoutDiscover);
                break;
            case R.id.rides_active:
                replaceFragment(new Ride_Add_Fragments());
                Tab_View_Adjust(ShadowLayoutRides,ShadowLayoutMyGarage,ShadowLayoutDiscover);
                break;
            case R.id.rides_inactive:
                replaceFragment(new Ride_Add_Fragments());
                Tab_View_Adjust(ShadowLayoutRides,ShadowLayoutMyGarage,ShadowLayoutDiscover);
                break;
        }
    }
    private void replaceFragment(Fragment fragment)
    {
        try{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment oldFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
            if (oldFragment != null) {
                fragment = oldFragment;
            }
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }catch (Exception e)
        {}
   }
    private void Tab_View_Adjust(View view,View view1,View view2) {

        if (view.getId()==R.id.ShadowLayout_discover)
        {
            if (view.getVisibility()==View.GONE)
            {
                view.setVisibility(View.VISIBLE);
                discoverInactive.setVisibility(View.GONE);

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                myGarageInactive.setVisibility(View.VISIBLE);
                ridesInactive.setVisibility(View.VISIBLE);
            }
        }else if (view.getId()==R.id.ShadowLayout_my_garage)
        {
            if (view.getVisibility()==View.GONE)
            {
                view.setVisibility(View.VISIBLE);
                myGarageInactive.setVisibility(View.GONE);

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);

                discoverInactive.setVisibility(View.VISIBLE);
                ridesInactive.setVisibility(View.VISIBLE);
            }
        }else if (view.getId()==R.id.ShadowLayout_rides)
        {
            if (view.getVisibility()==View.GONE)
            {
                view.setVisibility(View.VISIBLE);
                ridesInactive.setVisibility(View.GONE);

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);

                discoverInactive.setVisibility(View.VISIBLE);
                myGarageInactive.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Hit_Rider_Details(new Session(this).get_LOGIN_USER_ID());
    }
    private void Hit_Rider_Details(String userid) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.USER_DETAILS(userid);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray BIKEBRANDDETAILS = null;
                            try{
                                BIKEBRANDDETAILS =  jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONArray("BIKEBRANDDETAILS");
                            }catch (Exception E)
                            { }

                            if (BIKEBRANDDETAILS!=null)
                            {
                                if (BIKEBRANDDETAILS.length()>0)
                                {My_Bike.clear();
                                    replaceFragment(new My_Garage_Fragment());
                                    for (int i=0;i<BIKEBRANDDETAILS.length();i++)
                                    {
                                        My_Bike_Model mb = new My_Bike_Model();
                                        mb.setMY_BIKE_ID(BIKEBRANDDETAILS.getJSONObject(i).getString("MY_BIKE_ID"));
                                        mb.setBRANDID(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDID"));
                                        mb.setBRANDNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDNAME"));
                                        mb.setBRANDPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDPIC"));
                                        mb.setMODELID(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELID"));
                                        mb.setMODELNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELNAME"));
                                        mb.setMODELPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELPIC"));
                                        if (i==0)
                                        {
                                            mb.setSelect(true);
                                        }
                                        My_Bike.add(mb);
                                    }
                                }else
                                {
                                    replaceFragment(new Bike_Add_Fragments());
                                    Tab_View_Adjust(ShadowLayoutMyGarage,ShadowLayoutDiscover,ShadowLayoutRides);
                                }
                            }else
                            {
                                replaceFragment(new Bike_Add_Fragments());
                                Tab_View_Adjust(ShadowLayoutMyGarage,ShadowLayoutDiscover,ShadowLayoutRides);
                            }
                        } else {
                            hide_ProgressDialog();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });
    }


}