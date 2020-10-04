package com.flyby_riders.Ui.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Sharedpreferences.Prefe;
import com.flyby_riders.Ui.Activity.AvertisementView;
import com.flyby_riders.Ui.Activity.BikeBrandView;
import com.flyby_riders.Ui.Activity.DocumentLockerView;
import com.flyby_riders.Ui.Activity.MyAccount;
import com.flyby_riders.Ui.Activity.UpgradeAccountPlan;
import com.flyby_riders.Ui.Adapter.Garage.Garage_Ad_Adapter;
import com.flyby_riders.Ui.Adapter.Garage.Garage_add_click;
import com.flyby_riders.Ui.Adapter.Garage.My_Bike_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.ShadowLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import net.kjulio.rxlocation.RxLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.flyby_riders.Ui.Listener.StringUtils.PREMIUM;

public class My_Garage_Fragment extends Fragment implements onClick, Garage_add_click {
    final static int REQUEST_LOCATION = 199;
    private final LocationRequest defaultLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    public RetrofitCallback retrofitCallback;
    TextView bikeBrandName, newADIndicator;
    TextView bikeModelName;
    ShadowLayout AccountBtn;
    ImageButton BikeAddBtn;
    RelativeLayout DocumentLockerBtn;
    RecyclerView AdvetismentList;
    My_Bike_Adapter my_bike_adapter;
    ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    RecyclerView MyBikeList;
    My_Garage_Fragment fragment;
    ImageView MyBikeImage;
    String BIKE_MODEL_ID, BIKE_BRAND_ID;
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    NestedScrollView NestedScrollView_view;
   public static ArrayList<Garage_Advertisement> garage_ads_list = new ArrayList<>();
    Garage_Ad_Adapter garageAdAdapter;
    LinearLayout collapse_view, My_Bike_Image_view;
    ImageView collapse_image_view;
    String toDayDate = "";
    int newADFlag = 0;
    private AlertDialog alertDialog_loader = null;
    private LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private String City_Name = "";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Activity mActivity;
    private Context mContext;

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
                if (new Prefe(mActivity).getAccountPlanStatus().equalsIgnoreCase(PREMIUM)) {
                    startActivity(new Intent(mActivity, BikeBrandView.class));
                } else {
                    if (My_Bike_els.size() == 1) {
                        hit_Payment_Bottomsheet();
                    } else {
                        startActivity(new Intent(mActivity, BikeBrandView.class));
                    }
                }
            }
        });
        DocumentLockerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, DocumentLockerView.class));
            }
        });
        AccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyAccount.class));
            }
        });
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
        return view;
    }

    private void hit_Payment_Bottomsheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.can_add_bike, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(dialogView);
        TextView view_plan_details = bottomSheetDialog.findViewById(R.id.view_plan_details);
        view_plan_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, UpgradeAccountPlan.class);
                startActivity(i);
                bottomSheetDialog.hide();
            }
        });
        bottomSheetDialog.show();
    }

    private void Instantiation(View view) {
        mActivity = getActivity();
        mContext = getActivity();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        NestedScrollView_view = view.findViewById(R.id.NestedScrollView_view);
        collapse_view = view.findViewById(R.id.collapse_view);
        My_Bike_Image_view = view.findViewById(R.id.My_Bike_Image_view);
        collapse_image_view = view.findViewById(R.id.collapse_image_view);
        bikeBrandName = view.findViewById(R.id.bike_brand_name);
        AccountBtn= view.findViewById(R.id.Account_Btn);
        bikeModelName = view.findViewById(R.id.bike_model_name);
        newADIndicator = view.findViewById(R.id.newADIndicator);
        bikeModelName.setSelected(true);
        BikeAddBtn = view.findViewById(R.id.Bike_Add_btn);
        DocumentLockerBtn = view.findViewById(R.id.Document_Locker_btn);
        AdvetismentList = view.findViewById(R.id.Advetisment_list);
        MyBikeList = view.findViewById(R.id.My_Bike_list);
        MyBikeImage = view.findViewById(R.id.My_Bike_Image);
        MyBikeList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        AdvetismentList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        locationManager = (LocationManager) mActivity.getSystemService(Service.LOCATION_SERVICE);
        toDayDate = Constant.Get_back_date(Constant.GET_timeStamp());
        newADIndicator.setText("No New");
    }

    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 199);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                if (location != null) {
                    try {
                        FetchMyAdd(location);
                    } catch (NullPointerException e) {

                    }

                } else if (location1 != null) {
                    try {
                        FetchMyAdd(location1);

                    } catch (NullPointerException e) {

                    }
                } else if (location2 != null) {
                    try {
                        FetchMyAdd(location2);
                    } catch (NullPointerException e) {

                    }
                } else {
                    //Secound try
                    RxLocation.locationUpdates(mContext, defaultLocationRequest)
                            .firstElement()
                            .subscribe(new Consumer<Location>() {
                                @Override
                                public void accept(Location location) throws Exception {
                                    FetchMyAdd(location);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fetchRiderLocation() {

        if (new Prefe(mContext).get_mylocation().equalsIgnoreCase("")) {
            show_ProgressDialog();
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        hide_ProgressDialog();
                        try {
                            FetchMyAdd(location);
                        } catch (Exception e) {
                            Constant.Show_Tos_Error(mActivity, false, true);
                        }
                    } else {
                        getLocation();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new Prefe(mContext).get_mylocation());
                FetchMyAdd(jsonObject.getString("lat"), jsonObject.getString("long"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
            Picasso.get().load(my_bike.get(i).getMODELPIC()).placeholder(R.drawable.ic_emptybike).fit().into(MyBikeImage);
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
            Constant.Show_Tos_Error(mActivity, false, true);
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
                            Constant.Show_Tos_Error(mActivity, false, true);
                        }
                        garage_ads_list.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray ALLADVDETAILS = jsonObject.getJSONArray("ALLADVDETAILS");

                            for (int i = 0; i < ALLADVDETAILS.length(); i++) {
                                JSONObject js = ALLADVDETAILS.getJSONObject(i);
                                garage_ads_list.add(getProcessDataParse(js, jsonObject));
                            }
                            if (garage_ads_list.size() > 0) {
                                Collections.reverse(garage_ads_list);
                                garageAdAdapter = new Garage_Ad_Adapter(mActivity, garage_ads_list, My_Garage_Fragment.this);
                                AdvetismentList.setAdapter(garageAdAdapter);
                                if (newADFlag != 0)
                                    newADIndicator.setText(String.valueOf(newADFlag) + " New");
                                else
                                    newADIndicator.setText("No New");
                            }
                        } else {
                            if (garageAdAdapter != null)
                                garageAdAdapter.notifyDataSetChanged();
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        Constant.Show_Tos_Error(mActivity, false, true);
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(mActivity, true, false);

                hide_ProgressDialog();
            }
        });


    }

    private Garage_Advertisement getProcessDataParse(JSONObject js, JSONObject Rootjs) throws JSONException {
        Garage_Advertisement grg = new Garage_Advertisement();
        grg.setAdvertising_costPrice(js.getString("Advertising_costPrice"));
        grg.setAdvertising_CoverPic(Rootjs.getString("COVERIMAGE") + js.getString("Advertising_CoverPic"));
        grg.setAdvertising_Details(js.getString("Advertising_Details"));
        grg.setAdvertising_ID(js.getString("Advertising_ID"));
        grg.setAdvertising_Images(js.getString("Advertising_Images"));
        grg.setUSERID(js.getString("USERID"));
        grg.setAdvertising_PostDate(js.getString("Advertising_PostDate"));
        grg.setADIMAGEPATH(Rootjs.getString("ADIMAGEPATH"));
        grg.setAdvertising_Title(js.getString("Advertising_Title"));
        grg.setAdvertising_Video(Rootjs.getString("VIDEOIMAGEPATH") + js.getString("Advertising_Video"));
        grg.setAdvertising_userActions(getAdvertising_userActions(js.getJSONObject("Advertising_UserAction")));
        grg.setGarageOwnerDetails(getGarageOwnerDetail(js.getJSONObject("GarageOwnerDetails")));
        try {
            if (js.getString("Advertising_PostDate").equalsIgnoreCase(toDayDate)) {
                newADFlag++;
            }
        } catch (Exception e) {
            newADFlag = 0;
        }
        return grg;
    }

    private ArrayList<Garage_Advertisement.GarageOwnerDetails> getGarageOwnerDetail(JSONObject jsonObject) throws JSONException {
        ArrayList<Garage_Advertisement.GarageOwnerDetails> temp = new ArrayList<>();
        Garage_Advertisement.GarageOwnerDetails gh = new Garage_Advertisement.GarageOwnerDetails();
        gh.setAddress(jsonObject.getString("Address"));
        gh.setCity(jsonObject.getString("city"));
        gh.setGarageName(jsonObject.getString("GarageName"));
        gh.setID(jsonObject.getString("ID"));
        gh.setProfilePicture(jsonObject.getString("ProfilePicture"));
        gh.setUserName(jsonObject.getString("UserName"));
        temp.add(gh);
        return temp;
    }

    private ArrayList<Garage_Advertisement.Advertising_UserAction> getAdvertising_userActions(JSONObject jsonObject) throws JSONException {
        ArrayList<Garage_Advertisement.Advertising_UserAction> temp = new ArrayList<>();
        Garage_Advertisement.Advertising_UserAction us = new Garage_Advertisement.Advertising_UserAction();
        us.setByeNow(jsonObject.getString("ByeNow"));
        us.setContactStore(jsonObject.getString("ContactStore"));
        us.setNoUserAction(jsonObject.getString("NoUserAction"));
        temp.add(us);
        return temp;
    }


    @Override
    public void onResume() {
        super.onResume();
        Hit_Rider_Details(new Prefe(mContext).getUserID());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {

            if (new Prefe(mContext).get_mylocation().equalsIgnoreCase("")) {
                fetchRiderLocation();
            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Prefe(mContext).get_mylocation());
                    FetchMyAdd(jsonObject.getString("lat"), jsonObject.getString("long"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    void FetchMyAdd(Location location) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lat", location.getLatitude());
        jsonObject.put("long", location.getLongitude());
        new Prefe(mContext).set_mylocation(jsonObject.toString());
        City_Name = Constant.getCompletecity(mActivity, location.getLatitude(), location.getLongitude(), true, false, false);
        if (City_Name != null && BIKE_BRAND_ID != null && BIKE_MODEL_ID != null) {
            if (!City_Name.equalsIgnoreCase("") && !BIKE_BRAND_ID.equalsIgnoreCase("") && !BIKE_MODEL_ID.equalsIgnoreCase("")) {
                hit_Fetch_add(BIKE_BRAND_ID, BIKE_MODEL_ID);
            }
        }

    }

    void FetchMyAdd(String Latitude, String Longitude) throws JSONException {
        City_Name = Constant.getCompletecity(mActivity, Double.parseDouble(Latitude), Double.parseDouble(Longitude), true, false, false);
        if (City_Name != null && BIKE_BRAND_ID != null && BIKE_MODEL_ID != null) {
            if (!City_Name.equalsIgnoreCase("") && !BIKE_BRAND_ID.equalsIgnoreCase("") && !BIKE_MODEL_ID.equalsIgnoreCase("")) {
                hit_Fetch_add(BIKE_BRAND_ID, BIKE_MODEL_ID);
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
                fetchRiderLocation();
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
                                        my_bike_adapter = new My_Bike_Adapter(My_Garage_Fragment.this, My_Bike_els, mActivity);
                                        MyBikeList.setAdapter(my_bike_adapter);
                                        Set_View(My_Bike_els, 0);
                                    }
                                } else {
                                    Constant.Show_Tos(mContext, "Someting Error..");
                                }
                            } else {
                                Constant.Show_Tos(mContext, "Someting Error..");
                            }
                        } else {
                            hide_ProgressDialog();
                            Constant.Show_Tos(mContext, "Someting Error..");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Constant.Show_Tos(mContext, "Someting Error..");
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(mActivity, true, false);
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
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
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

        Intent intent = new Intent(getActivity(), AvertisementView.class);
        intent.putExtra("Position",Position);
        startActivity(intent);
    }
}
