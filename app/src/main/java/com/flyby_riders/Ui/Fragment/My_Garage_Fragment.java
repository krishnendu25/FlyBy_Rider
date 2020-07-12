package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.flyby_riders.Ui.Activity.DashBoard;
import com.flyby_riders.Ui.Activity.Document_Locker;
import com.flyby_riders.Ui.Adapter.My_Bike_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Garage_Fragment extends Fragment implements onClick {

    TextView bikeBrandName;
    TextView bikeModelName;
    ImageButton BikeAddBtn;
    RelativeLayout DocumentLockerBtn;
    ListView AdvetismentList;
    My_Bike_Adapter my_bike_adapter;
    ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    RecyclerView MyBikeList;
    My_Garage_Fragment fragment;
    public RetrofitCallback retrofitCallback;
    ImageView MyBikeImage;
    String BIKE_MODEL_ID, BIKE_BRAND_ID;
    private AlertDialog alertDialog_loader=null;

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
        bikeBrandName= view.findViewById(R.id.bike_brand_name);
        bikeModelName= view.findViewById(R.id.bike_model_name);
        BikeAddBtn=view.findViewById(R.id.Bike_Add_btn);
        DocumentLockerBtn=view.findViewById(R.id.Document_Locker_btn);
        AdvetismentList=view.findViewById(R.id.Advetisment_list);
        MyBikeList=view.findViewById(R.id.My_Bike_list);
        MyBikeImage=view.findViewById(R.id.My_Bike_Image);
        MyBikeList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        BikeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Bike_Brand_Activity.class));
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



    @Override
    public void onClick(String Value) {
        if (My_Bike_els.size()>0)
        {Set_View(My_Bike_els, Integer.valueOf(Value));}

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
        try{
            bikeModelName.setText(my_bike.get(i).getMODELNAME());
            bikeBrandName.setText(my_bike.get(i).getBRANDNAME());
            BIKE_BRAND_ID = my_bike.get(i).getBRANDID();
            BIKE_MODEL_ID = my_bike.get(i).getMODELID();
            hit_Fetch_add(BIKE_BRAND_ID,BIKE_MODEL_ID);
        }catch (Exception e)
        {}

    }

    private void hit_Fetch_add(String bike_brand_id, String bike_model_id)
    {

    }

    @Override
    public void onResume() {
        super.onResume();
        Hit_Rider_Details(new Session(getContext()).get_LOGIN_USER_ID());
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
                                {My_Bike_els.clear();

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
                                        My_Bike_els.add(mb);
                                    }
                                    if (My_Bike_els.size()>0)
                                    {
                                        my_bike_adapter = new My_Bike_Adapter(My_Garage_Fragment.this, My_Bike_els, getActivity());
                                        MyBikeList.setAdapter(my_bike_adapter);
                                        Set_View(My_Bike_els, 0);
                                    }
                                }else
                                { Constant.Show_Tos(getContext(),"Someting Error..");}
                            }else
                            {Constant.Show_Tos(getContext(),"Someting Error.."); }
                        } else {
                            hide_ProgressDialog();
                            Constant.Show_Tos(getContext(),"Someting Error..");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Constant.Show_Tos(getContext(),"Someting Error..");
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

    public  void show_ProgressDialog()
    {
        try{
            try {
                if (alertDialog_loader !=null)
                {
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
        }catch (Exception e)
        {

        }



    }
    public void hide_ProgressDialog()
    {
        try{
            try {
                if (alertDialog_loader!=null )
                {
                    alertDialog_loader.hide();
                }
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        }catch (Exception e)
        {

        }
    }
}
