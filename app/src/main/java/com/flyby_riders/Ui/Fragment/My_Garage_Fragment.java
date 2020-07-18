package com.flyby_riders.Ui.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Activity.Bike_Brand_Activity;
import com.flyby_riders.Ui.Activity.Document_Locker;
import com.flyby_riders.Ui.Adapter.Garage.Garage_Ad_Adapter;
import com.flyby_riders.Ui.Adapter.Garage.Garage_add_click;
import com.flyby_riders.Ui.Adapter.My_Bike_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Garage_Ad;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.flyby_riders.Ui.Listener.StringUtils.PREMIUM;

public class My_Garage_Fragment extends Fragment implements onClick, Garage_add_click {
    private final LocationRequest defaultLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    TextView bikeBrandName;
    TextView bikeModelName;
    final static int REQUEST_LOCATION = 199;
    ImageButton BikeAddBtn;
    RelativeLayout DocumentLockerBtn;
    RecyclerView AdvetismentList;
    My_Bike_Adapter my_bike_adapter;
    ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    RecyclerView MyBikeList;
    My_Garage_Fragment fragment;
    public RetrofitCallback retrofitCallback;
    ImageView MyBikeImage;
    String BIKE_MODEL_ID, BIKE_BRAND_ID;
    private AlertDialog alertDialog_loader = null;
    private LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private String City_Name = "";
    NestedScrollView NestedScrollView_view;
    ArrayList<Garage_Ad> garage_ads_list = new ArrayList<>();
    Garage_Ad_Adapter garageAdAdapter;
    LinearLayout collapse_view,My_Bike_Image_view;
    ImageView collapse_image_view;

    public My_Garage_Fragment() {
    }

    public My_Garage_Fragment newInstance() {
        if (fragment != null) {
            return fragment;
        } else {
            fragment = new My_Garage_Fragment();
            return fragment;
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my__garage_, container, false);
        Instantiation(view);

        BikeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Session(getActivity()).get_MEMBER_STATUS().equalsIgnoreCase(PREMIUM)) {
                    startActivity(new Intent(getActivity(), Bike_Brand_Activity.class));
                } else {
                    if (My_Bike_els.size() == 1) {
                        Constant.Show_Tos(getActivity(), "Basic User Add Only 1 Bike");
                    } else {
                        startActivity(new Intent(getActivity(), Bike_Brand_Activity.class));
                    }
                }
            }
        });
        DocumentLockerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Document_Locker.class));
            }
        });

        return view;
    }

    private void Instantiation(View view) {
        NestedScrollView_view = view.findViewById(R.id.NestedScrollView_view);
        collapse_view = view.findViewById(R.id.collapse_view);
        My_Bike_Image_view = view.findViewById(R.id.My_Bike_Image_view);
        collapse_image_view = view.findViewById(R.id.collapse_image_view);
        bikeBrandName = view.findViewById(R.id.bike_brand_name);
        bikeModelName = view.findViewById(R.id.bike_model_name);
        BikeAddBtn = view.findViewById(R.id.Bike_Add_btn);
        DocumentLockerBtn = view.findViewById(R.id.Document_Locker_btn);
        AdvetismentList = view.findViewById(R.id.Advetisment_list);
        MyBikeList = view.findViewById(R.id.My_Bike_list);
        MyBikeImage = view.findViewById(R.id.My_Bike_Image);
        MyBikeList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        AdvetismentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        //Fetch Location
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PERMISSION_GRANTED) {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Fetch Location And Set Marker
                Fetch_Location_With_RX();
            }

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        NestedScrollView_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {

                    Transition transition = new Fade();
                    transition.setDuration(100);
                    transition.addTarget(R.id.collapse_view);

                    TransitionManager.beginDelayedTransition(collapse_view, transition);

                    collapse_view.setVisibility(View.VISIBLE);
                    MyBikeImage.setVisibility(View.GONE);
                }

                if (scrollY == 0) {

                    Transition x = new Fade();
                    x.setDuration(600);
                    x.addTarget(R.id.My_Bike_Image);

                    TransitionManager.beginDelayedTransition(My_Bike_Image_view, x);
                    collapse_view.setVisibility(View.GONE);
                    MyBikeImage.setVisibility(View.VISIBLE);
                }


            }
        });


    }

    private void Fetch_Location_With_RX() {
        LocationTracker tracker = new LocationTracker(
                getActivity(),
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true)

        ) {

            @Override
            public void onLocationFound(Location location) {
                try {
                    City_Name = Constant.getCompletecity(getActivity(), location.getLatitude(), location.getLongitude(), true, false, false);
                    if (City_Name != null && BIKE_BRAND_ID != null && BIKE_MODEL_ID != null) {
                        if (!City_Name.equalsIgnoreCase("") && !BIKE_BRAND_ID.equalsIgnoreCase("") && !BIKE_MODEL_ID.equalsIgnoreCase("")) {
                            hit_Fetch_add(BIKE_BRAND_ID, BIKE_MODEL_ID);
                        }
                    }
                }catch (Exception e){}

            }

            @Override
            public void onTimeout() {
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        tracker.startListening();

    }


    @Override
    public void onClick(String Value) {
        if (My_Bike_els.size() > 0) {
            Set_View(My_Bike_els, Integer.valueOf(Value));
        }

    }

    @Override
    public void onLongClick(String Value) {

    }

    private void Set_View(ArrayList<My_Bike_Model> my_bike, int i) {

        try {
            Picasso.get().load(my_bike.get(i).getMODELPIC()).placeholder(R.drawable.ic_emptybike).into(MyBikeImage);
        } catch (Exception e) {
            MyBikeImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_emptybike));
        }
        try {
            Picasso.get().load(my_bike.get(i).getMODELPIC()).placeholder(R.drawable.ic_emptybike).into(collapse_image_view);
        } catch (Exception e) {
            collapse_image_view.setImageDrawable(getResources().getDrawable(R.drawable.ic_emptybike));
        }
        try {
            bikeModelName.setText(my_bike.get(i).getMODELNAME());
            bikeBrandName.setText(my_bike.get(i).getBRANDNAME());
            BIKE_BRAND_ID = my_bike.get(i).getBRANDID();
            BIKE_MODEL_ID = my_bike.get(i).getMODELID();
            if (City_Name != null) {
                if (!City_Name.equalsIgnoreCase("")) {
                    hit_Fetch_add(BIKE_BRAND_ID, BIKE_MODEL_ID);
                }
            }

        } catch (Exception e) {
        }

    }

    private void hit_Fetch_add(String bike_brand_id, String bike_model_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_all_advertise(bike_model_id, bike_brand_id, City_Name);
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
                        garage_ads_list.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray ALLADVDETAILS = jsonObject.getJSONArray("ALLADVDETAILS");

                            for (int i = 0; i < ALLADVDETAILS.length(); i++) {
                                JSONObject js = ALLADVDETAILS.getJSONObject(i);

                                Garage_Ad garage_ad = new Garage_Ad();
                                garage_ad.setUSERID(js.getString("USERID"));
                                garage_ad.setADVID(js.getString("ADVID"));
                                garage_ad.setADTITLE(js.getString("ADTITLE"));
                                garage_ad.setADDESC(js.getString("ADDESC"));
                                garage_ad.setADVIDEO(jsonObject.getString("VIDEOIMAGEPATH") + js.getString("ADVIDEO"));
                                garage_ad.setADCOVERIMAGES(jsonObject.getString("COVERIMAGE") + js.getString("ADCOVERIMAGES"));
                                garage_ad.setADCOSTPRICE(js.getString("ADCOSTPRICE"));
                                garage_ad.setADCREATIONDATE(js.getString("ADCREATIONDATE"));
                                garage_ad.setADPOSTDATE(js.getString("ADPOSTDATE"));
                                garage_ad.setImages(Constant.splitByComma(js.getString("ADIMAGES"), jsonObject.getString("ADIMAGEPATH")));
                                garage_ads_list.add(garage_ad);
                            }
                            if (garage_ads_list.size() > 0) {
                                garageAdAdapter = new Garage_Ad_Adapter(getActivity(), garage_ads_list);
                                AdvetismentList.setAdapter(garageAdAdapter);
                            }

                        } else {
                            hide_ProgressDialog();
                        }


                    } catch (Exception e) {
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


    @Override
    public void onResume() {
        super.onResume();
        Hit_Rider_Details(new Session(getContext()).get_LOGIN_USER_ID());
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {

            Fetch_Location_With_RX();
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
                            e.printStackTrace();
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray BIKEBRANDDETAILS = null;
                            try {
                                BIKEBRANDDETAILS = jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONArray("BIKEBRANDDETAILS");
                            } catch (Exception E) {
                            }

                            if (BIKEBRANDDETAILS != null) {
                                if (BIKEBRANDDETAILS.length() > 0) {
                                    My_Bike_els.clear();

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
                                        My_Bike_els.add(mb);
                                    }
                                    if (My_Bike_els.size() > 0) {
                                        my_bike_adapter = new My_Bike_Adapter(My_Garage_Fragment.this, My_Bike_els, getActivity());
                                        MyBikeList.setAdapter(my_bike_adapter);
                                        Set_View(My_Bike_els, 0);
                                    }
                                } else {
                                    Constant.Show_Tos(getContext(), "Someting Error..");
                                }
                            } else {
                                Constant.Show_Tos(getContext(), "Someting Error..");
                            }
                        } else {
                            hide_ProgressDialog();
                            Constant.Show_Tos(getContext(), "Someting Error..");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Constant.Show_Tos(getContext(), "Someting Error..");
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

    @Override
    public void SetOnClick(int Position) {

    }
}
