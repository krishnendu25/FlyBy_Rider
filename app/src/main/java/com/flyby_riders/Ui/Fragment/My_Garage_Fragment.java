package com.flyby_riders.Ui.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Activity.AvertisementView;
import com.flyby_riders.Ui.Activity.BikeBrandView;
import com.flyby_riders.Ui.Activity.DocumentLockerView;
import com.flyby_riders.Ui.Activity.MyAccount;
import com.flyby_riders.Ui.Activity.UpgradeAccountPlan;
import com.flyby_riders.Ui.Adapter.Garage.GarageAddClick;
import com.flyby_riders.Ui.Adapter.Garage.Garage_Ad_Adapter;
import com.flyby_riders.Ui.Adapter.Garage.My_Bike_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.Prefe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

import static com.flyby_riders.Constants.StringUtils.PREMIUM;
import static com.flyby_riders.Constants.StringUtils.TASK_CLICK;
import static com.flyby_riders.Constants.StringUtils.TASK_SEEN;

public class My_Garage_Fragment extends Fragment implements onClick, GarageAddClick {
    final static int REQUEST_LOCATION = 199;
    public static ArrayList<Garage_Advertisement> garage_ads_list = new ArrayList<>();
    private final LocationRequest defaultLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    public RetrofitCallback retrofitCallback;
    private TextView bikeBrandName, newADIndicator, emptyViewList, bikeModelName;
    private LinearLayout collapse_view, My_Bike_Image_view;
    private NeumorphCardView AccountBtn;
    private ImageButton BikeAddBtn;
    private RelativeLayout DocumentLockerBtn;
    private RecyclerView AdvetismentList, MyBikeList;
    private My_Bike_Adapter my_bike_adapter;
    private My_Garage_Fragment fragment;
    private NestedScrollView NestedScrollView_view;
    private Garage_Ad_Adapter garageAdAdapter;
    private ImageView collapse_image_view, MyBikeImage;
    private String toDayDate = "", State_Name = "", BIKE_MODEL_ID = "", BIKE_BRAND_ID = "";
    private int newADFlag = 0;
    private AlertDialog alertDialog_loader = null;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Activity mActivity;
    private ShimmerFrameLayout shimmer_view_container;
    private RelativeLayout shimmerView;
    private SwipeRefreshLayout refreshPull;
    private Context mContext;
    private ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    private LinearLayoutManager add_LinearLayoutManager;

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

        refreshPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (State_Name != null) {
                    if (!State_Name.equalsIgnoreCase("")) {
                        hit_Fetch_add();
                    } else {
                        Hit_Rider_Details(new Prefe(mContext).getUserID());
                    }
                } else {
                    Hit_Rider_Details(new Prefe(mContext).getUserID());
                }
                refreshPull.setRefreshing(false);
            }
        });

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
                startActivity(new Intent(mActivity, MyAccount.class));
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

        AdvetismentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    int FirstID = add_LinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    // int LastID = add_LinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    hitAddClick(TASK_SEEN, new Prefe(mActivity).getUserID(), garage_ads_list.get(FirstID).Advertising_ID);
                    //hitAddClick(TASK_SEEN, new Prefe(mActivity).getUserID(),garage_ads_list.get(LastID).Advertising_ID);
                } catch (Exception e) {
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
        mContext = mActivity;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        shimmerView = view.findViewById(R.id.shimmerView);
        NestedScrollView_view = view.findViewById(R.id.NestedScrollView_view);
        collapse_view = view.findViewById(R.id.collapse_view);
        My_Bike_Image_view = view.findViewById(R.id.My_Bike_Image_view);
        collapse_image_view = view.findViewById(R.id.collapse_image_view);
        bikeBrandName = view.findViewById(R.id.bike_brand_name);
        AccountBtn = view.findViewById(R.id.Account_Btn);
        bikeModelName = view.findViewById(R.id.bike_model_name);
        emptyViewList = view.findViewById(R.id.emptyViewList);
        refreshPull = view.findViewById(R.id.refreshPull);
        newADIndicator = view.findViewById(R.id.newADIndicator);
        bikeModelName.setSelected(true);
        BikeAddBtn = view.findViewById(R.id.Bike_Add_btn);
        DocumentLockerBtn = view.findViewById(R.id.Document_Locker_btn);
        AdvetismentList = view.findViewById(R.id.Advetisment_list);
        MyBikeList = view.findViewById(R.id.My_Bike_list);
        MyBikeImage = view.findViewById(R.id.My_Bike_Image);
        MyBikeList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        add_LinearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        AdvetismentList.setLayoutManager(add_LinearLayoutManager);
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        locationManager = (LocationManager) mActivity.getSystemService(Service.LOCATION_SERVICE);
        toDayDate = Constant.Get_back_date(Constant.GET_timeStamp());
        newADIndicator.setVisibility(View.GONE);
        newADIndicator.setText("No New");
    }


    private void fetchRiderLocation() {

        if (new Prefe(mContext).get_mylocation().equalsIgnoreCase("")) {
            show_ProgressDialog();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {

                            try {
                                FetchMyAdd(location);
                            } catch (Exception e) {
                                Constant.Show_Tos_Error(mActivity, false, true);
                            }
                        } else {
                            Hit_Rider_Details(new Prefe(mContext).getUserID());
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new Prefe(mContext).get_mylocation());
                FetchMyAdd(jsonObject.getString("lat"), jsonObject.getString("long"));
            } catch (JSONException e) {

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
            hit_Fetch_add();
        } catch (Exception e) {
            Constant.Show_Tos_Error(mActivity, false, true);
        }
    }

    private void hit_Fetch_add() {
        Log.e("@@@@@", State_Name + "  " + BIKE_BRAND_ID + "  " + BIKE_MODEL_ID);

        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_all_advertise(State_Name, BIKE_BRAND_ID, BIKE_MODEL_ID);
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
                            Log.e("@@@@@", jsonObject.toString());
                        } catch (Exception e) {
                            Constant.Show_Tos_Error(mActivity, false, true);
                        }
                        ArrayList<Garage_Advertisement> temp = new ArrayList<>();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray ALLADVDETAILS = jsonObject.getJSONArray("ALLADVDETAILS");
                            newADFlag = 0;
                            for (int i = 0; i < ALLADVDETAILS.length(); i++) {
                                JSONObject js = ALLADVDETAILS.getJSONObject(i);
                                temp.add(getProcessDataParse(js, jsonObject));
                            }
                            if (temp.size() > 0) {

                                garage_ads_list.clear();
                                garage_ads_list = sortBYDate(filterByData(temp));
                                garageAdAdapter = new Garage_Ad_Adapter(mActivity, garage_ads_list, My_Garage_Fragment.this);
                                AdvetismentList.setAdapter(garageAdAdapter);
                                if (newADFlag != 0) {
                                    newADIndicator.setText(newADFlag + " New");
                                    newADIndicator.setVisibility(View.VISIBLE);
                                } else {
                                    newADFlag = 0;
                                    newADIndicator.setText("No New");
                                    newADIndicator.setVisibility(View.GONE);
                                }
                                if (garage_ads_list.size() > 0) {
                                    emptyViewList.setVisibility(View.GONE);
                                } else {
                                    emptyViewList.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            if (garageAdAdapter != null)
                                newADFlag = 0;
                            garageAdAdapter.notifyDataSetChanged();
                            if (garage_ads_list.size() > 0) {
                                emptyViewList.setVisibility(View.GONE);
                            } else {
                                emptyViewList.setVisibility(View.VISIBLE);
                            }
                            hide_ProgressDialog();

                        }
                    } catch (Exception e) {
                        newADFlag = 0;
                        Constant.Show_Tos_Error(mActivity, false, true);
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(mActivity, true, false);
                newADFlag = 0;
                hide_ProgressDialog();
            }
        });


    }

    private ArrayList<Garage_Advertisement> sortBYDate(ArrayList<Garage_Advertisement> filterByData) {
        ArrayList<Garage_Advertisement> temp = filterByData;
        try {
            Collections.sort(temp, new Comparator<Garage_Advertisement>() {
                DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");

                @Override
                public int compare(Garage_Advertisement lhs, Garage_Advertisement rhs) {
                    try {
                        return f.parse(lhs.getAdvertising_PostDate()).compareTo(f.parse(rhs.getAdvertising_PostDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
        } catch (Exception e) {
            temp = filterByData;
        }
        Collections.reverse(temp);
        return temp;
    }

    private ArrayList<Garage_Advertisement> filterByData(ArrayList<Garage_Advertisement> list) {
        ArrayList<Garage_Advertisement> temp = new ArrayList<>();
        String Date = Constant.Get_back_date(Constant.GET_timeStamp());
        for (int i = 0; i < list.size(); i++) {
            String Differ = Constant.getCountOfDays(list.get(i).getAdvertising_PostDate(), Date);
            if (Integer.parseInt(Differ.replaceAll("Days", "").trim()) < 4) {
                temp.add(list.get(i));
            }
        }
        return temp;
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

                }

            }

        }
    }

    private void FetchMyAdd(Location location) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lat", location.getLatitude());
        jsonObject.put("long", location.getLongitude());
        new Prefe(mContext).set_mylocation(jsonObject.toString());
        State_Name = Constant.getCompletecity(mActivity, location.getLatitude(), location.getLongitude(), false, false, true);
        if (State_Name != null && BIKE_BRAND_ID != null && BIKE_MODEL_ID != null) {
            if (!State_Name.equalsIgnoreCase("") && !BIKE_BRAND_ID.equalsIgnoreCase("") && !BIKE_MODEL_ID.equalsIgnoreCase("")) {
                hit_Fetch_add();
            }
        }

    }

    private void FetchMyAdd(String Latitude, String Longitude) throws JSONException {
        State_Name = Constant.getCompletecity(mActivity, Double.parseDouble(Latitude), Double.parseDouble(Longitude), false, false, true);
        if (State_Name != null && BIKE_BRAND_ID != null && BIKE_MODEL_ID != null) {
            if (!State_Name.equalsIgnoreCase("") && !BIKE_BRAND_ID.equalsIgnoreCase("") && !BIKE_MODEL_ID.equalsIgnoreCase("")) {
                hit_Fetch_add();
            }
        }

    }

    private void Hit_Rider_Details(String userid) {
        newADFlag = 0;
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.USER_DETAILS(userid);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                try {
                    fetchRiderLocation();
                } catch (Exception e) {
                }

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {

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
                                    if (State_Name != null) {
                                        if (!State_Name.equalsIgnoreCase("")) {
                                            hit_Fetch_add();
                                        }
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
        shimmer_view_container.startShimmer();
        shimmerView.setVisibility(View.VISIBLE);
    }

    public void hide_ProgressDialog() {
        shimmer_view_container.stopShimmer();
        shimmerView.setVisibility(View.GONE);
    }

    private void hitAddClick(String task, String userid, String advID) {
        Call<ResponseBody> requestCall = retrofitCallback.click_advertise(task, userid, advID);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("@ClickOnAdd ", advID);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });
    }

    @Override
    public void setOnClick(int Position) {
        hitAddClick(TASK_SEEN, new Prefe(mActivity).getUserID(), garage_ads_list.get(Position).Advertising_ID);
        hitAddClick(TASK_CLICK, new Prefe(mActivity).getUserID(), garage_ads_list.get(Position).Advertising_ID.trim());
        Intent intent = new Intent(mActivity, AvertisementView.class);
        intent.putExtra("Position", Position);
        startActivity(intent);
    }


}
