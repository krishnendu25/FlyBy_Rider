package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Fragment.Bike_Add_Fragments;
import com.flyby_riders.Ui.Fragment.Discover_Fragment;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Fragment.My_Ride_Fragment;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.flyby_riders.Constants.StringUtils.BASIC;
import static com.flyby_riders.Constants.StringUtils.PREMIUM;

public class DashBoard extends BaseActivity {
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

    public static ArrayList<My_Bike_Model> My_Bike = new ArrayList<>();
    @BindView(R.id.ShadowLayout_discover)
    NeumorphCardView ShadowLayoutDiscover;
    @BindView(R.id.ShadowLayout_my_garage)
    NeumorphCardView ShadowLayoutMyGarage;
    @BindView(R.id.ShadowLayout_rides)
    NeumorphCardView ShadowLayoutRides;
    private String My_Ride_Attached;
    ShimmerFrameLayout shimmer_view_container;
    RelativeLayout shimmerView;
    String Current_Fagment_Name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Instantiation();
        Hit_Rider_Details(new Prefe(this).getUserID());


    }

    private void Instantiation() {
        new Prefe(this).set_mylocation("");
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        shimmerView = findViewById(R.id.shimmerView);
        Tab_View_Adjust(ShadowLayoutMyGarage, ShadowLayoutDiscover, ShadowLayoutRides);
    }

    @OnClick({R.id.discover_active, R.id.discover_inactive, R.id.my_garage_active, R.id.my_garage_inactive, R.id.rides_active, R.id.rides_inactive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.discover_active:
                replaceFragment(new Discover_Fragment());
                Tab_View_Adjust(ShadowLayoutDiscover, ShadowLayoutMyGarage, ShadowLayoutRides);
                break;
            case R.id.discover_inactive:
                replaceFragment(new Discover_Fragment());
                Tab_View_Adjust(ShadowLayoutDiscover, ShadowLayoutMyGarage, ShadowLayoutRides);
                break;
            case R.id.my_garage_active:
                Hit_Rider_Details(new Prefe(this).getUserID());
                Tab_View_Adjust(ShadowLayoutMyGarage, ShadowLayoutRides, ShadowLayoutDiscover);
                break;
            case R.id.my_garage_inactive:
                Hit_Rider_Details(new Prefe(this).getUserID());
                Tab_View_Adjust(ShadowLayoutMyGarage, ShadowLayoutRides, ShadowLayoutDiscover);
                break;
            case R.id.rides_active:
                hit_my_ride(new Prefe(this).getUserID());
                if (My_Ride_Attached != null) {
                    if (My_Ride_Attached.equalsIgnoreCase("0")) {
                        replaceFragment(new My_Ride_Fragment());
                    } else {
                        replaceFragment(new My_Ride_Fragment());
                    }
                } else {
                    replaceFragment(new My_Ride_Fragment());
                }
                Tab_View_Adjust(ShadowLayoutRides, ShadowLayoutMyGarage, ShadowLayoutDiscover);
                break;
            case R.id.rides_inactive:
                if (My_Ride_Attached != null) {
                    if (My_Ride_Attached.equalsIgnoreCase("0")) {
                        replaceFragment(new My_Ride_Fragment());
                    } else {
                        replaceFragment(new My_Ride_Fragment());
                    }
                } else {
                    replaceFragment(new My_Ride_Fragment());
                }
                Tab_View_Adjust(ShadowLayoutRides, ShadowLayoutMyGarage, ShadowLayoutDiscover);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        try {
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment oldFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
                if (oldFragment != null) {
                    fragment = oldFragment;
                }
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
                Current_Fagment_Name = fragment.getClass().getName();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            } catch (IllegalStateException e) {
                Constant.Show_Tos_Error(getApplicationContext(), false, true);
            }
        } catch (Exception e) {
            Constant.Show_Tos_Error(getApplicationContext(), false, true);
        }


    }


    private void Tab_View_Adjust(View view, View view1, View view2) {

        if (view.getId() == R.id.ShadowLayout_discover) {

            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
                discoverInactive.setVisibility(View.GONE);

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                myGarageInactive.setVisibility(View.VISIBLE);
                ridesInactive.setVisibility(View.VISIBLE);
            }
        } else if (view.getId() == R.id.ShadowLayout_my_garage) {
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
                myGarageInactive.setVisibility(View.GONE);

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);

                discoverInactive.setVisibility(View.VISIBLE);
                ridesInactive.setVisibility(View.VISIBLE);
            }
        } else if (view.getId() == R.id.ShadowLayout_rides) {
            if (view.getVisibility() == View.GONE) {
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
        hit_my_ride(new Prefe(this).getUserID());
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            {
                Intent intent = new Intent(this, GpsTrunOnWarning.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
        if (ContextCompat.checkSelfPermission(DashBoard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
        } else {
            Intent i = new Intent(getApplicationContext(), LocationPermissionsWindow.class);
            startActivityForResult(i, 365);
        }


        if (My_Ride_Attached != null) {
            if (!My_Ride_Attached.equalsIgnoreCase("0")) {
                if (Current_Fagment_Name.contains("My_Ride_Fragment"))
                    replaceFragment(new My_Ride_Fragment());
            }
        }


    }

    private void Hit_Rider_Details(String userid) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.USER_DETAILS(userid);
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
                            Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray BIKEBRANDDETAILS = null;
                            try {
                                BIKEBRANDDETAILS = jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONArray("BIKEBRANDDETAILS");
                                Check_Payment(jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONObject("USERDETAILS"));
                            } catch (Exception E) {
                                Constant.Show_Tos_Error(getApplicationContext(), false, true);
                            }

                            if (BIKEBRANDDETAILS != null) {
                                if (BIKEBRANDDETAILS.length() > 0) {
                                    My_Bike.clear();
                                    replaceFragment(new My_Garage_Fragment());
                                    for (int i = 0; i < BIKEBRANDDETAILS.length(); i++) {
                                        My_Bike_Model mb = new My_Bike_Model();
                                        mb.setMY_BIKE_ID(BIKEBRANDDETAILS.getJSONObject(i).getString("MY_BIKE_ID"));
                                        mb.setBRANDID(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDID"));
                                        mb.setBRANDNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDNAME"));
                                        mb.setBRANDPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDPIC"));
                                        mb.setMODELID(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELID"));
                                        mb.setMODELNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELNAME"));
                                        mb.setMODELPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELPIC"));
                                        if (i == 0) {
                                            mb.setSelect(true);
                                        }
                                        My_Bike.add(mb);
                                    }
                                } else {
                                    replaceFragment(new Bike_Add_Fragments());
                                    Tab_View_Adjust(ShadowLayoutMyGarage, ShadowLayoutDiscover, ShadowLayoutRides);
                                }
                            } else {
                                replaceFragment(new Bike_Add_Fragments());
                                Tab_View_Adjust(ShadowLayoutMyGarage, ShadowLayoutDiscover, ShadowLayoutRides);
                            }
                        } else {
                            hide_ProgressDialog();
                        }


                    } catch (Exception e) {
                        Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });
    }

    private void Check_Payment(JSONObject jsonObject) {

        if (jsonObject.has("PLANNAME")) {
            try {
                if (jsonObject.getString("PLANNAME") != null) {
                    if (jsonObject.getString("PLANNAME").equalsIgnoreCase("")) {
                        new Prefe(getApplicationContext()).setAccountPlanStatus(BASIC);
                    } else if (jsonObject.getString("PLANNAME").equalsIgnoreCase("null")) {
                        new Prefe(getApplicationContext()).setAccountPlanStatus(BASIC);
                    } else {
                        if (jsonObject.getString("PLANNAME").equalsIgnoreCase(BASIC)) {
                            new Prefe(getApplicationContext()).setAccountPlanStatus(BASIC);
                        } else {
                            new Prefe(getApplicationContext()).setAccountPlanStatus(PREMIUM);
                        }
                    }
                } else {
                    new Prefe(getApplicationContext()).setAccountPlanStatus(BASIC);
                }
            } catch (Exception e) {
                Constant.Show_Tos_Error(getApplicationContext(), false, true);
            }
        }


    }

    private void hit_my_ride(String user_id) {

        Call<ResponseBody> requestCall = retrofitCallback.fetch_fetch_my_ride(user_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            JSONArray RIDEDETAILS = jsonObject.getJSONArray("RIDEDETAILS");
                            if (RIDEDETAILS.length() > 0) {
                                My_Ride_Attached = String.valueOf(RIDEDETAILS.length());
                            } else if (RIDEDETAILS.length() == 0) {
                                My_Ride_Attached = String.valueOf(RIDEDETAILS.length());
                            } else {
                                My_Ride_Attached = String.valueOf(0);
                            }

                        } else {
                            My_Ride_Attached = String.valueOf(0);
                        }
                    } catch (Exception e) {
                        My_Ride_Attached = String.valueOf(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                My_Ride_Attached = String.valueOf(0);
            }
        });

        if (My_Ride_Attached != null) {
            if (!My_Ride_Attached.equalsIgnoreCase("0")) {
                if (Current_Fagment_Name.contains("My_Ride_Fragment"))
                    replaceFragment(new My_Ride_Fragment());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == 365) {
            Hit_Rider_Details(new Prefe(this).getUserID());
            hit_my_ride(new Prefe(this).getUserID());
        }
    }

    public void show_ProgressDialog() {
        shimmer_view_container.startShimmer();
        shimmerView.setVisibility(View.VISIBLE);
    }

    public void hide_ProgressDialog() {
        shimmer_view_container.stopShimmer();
        shimmerView.setVisibility(View.GONE);
    }


}
