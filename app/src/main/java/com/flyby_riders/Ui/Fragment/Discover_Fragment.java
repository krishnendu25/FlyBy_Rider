package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Model.Category_Model;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Discover_Fragment extends Fragment implements View.OnClickListener {
    RelativeLayout bike_servicing_view;
    RelativeLayout gearsaccessories_view;
    RelativeLayout bike_parts_view;
    RelativeLayout bike_wash_view;
    RelativeLayout tyres_view;
    RelativeLayout body_work_view;
    RelativeLayout towing_view;
    RelativeLayout bike_nshowroom_view;
    RelativeLayout race_track_view;
    RelativeLayout racing_nacadmey_view;
    public RetrofitCallback retrofitCallback;
    private AlertDialog alertDialog_loader = null;
    ArrayList<Garage_Owner_Model> Garage_List = new ArrayList<>();
    public Discover_Fragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static Discover_Fragment newInstance() {
        Discover_Fragment fragment = new Discover_Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_discover, container, false);
        Instantiation(V);

        return V;
    }

    private void Instantiation(View v) {
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        bike_servicing_view = (RelativeLayout) v.findViewById(R.id.bike_servicing_view);
        gearsaccessories_view = (RelativeLayout) v.findViewById(R.id.gearsaccessories_view);
        bike_parts_view = (RelativeLayout) v.findViewById(R.id.bike_parts_view);
        bike_wash_view = (RelativeLayout) v.findViewById(R.id.bike_wash_view);
        tyres_view = (RelativeLayout) v.findViewById(R.id.tyres_view);
        body_work_view = (RelativeLayout) v.findViewById(R.id.body_work_view);
        towing_view = (RelativeLayout) v.findViewById(R.id.towing_view);
        bike_nshowroom_view = (RelativeLayout) v.findViewById(R.id.bike_nshowroom_view);
        race_track_view = (RelativeLayout) v.findViewById(R.id.race_track_view);
        racing_nacadmey_view = (RelativeLayout) v.findViewById(R.id.racing_nacadmey_view);

        bike_servicing_view.setOnClickListener(this);
        gearsaccessories_view.setOnClickListener(this);
        bike_parts_view.setOnClickListener(this);
        bike_wash_view.setOnClickListener(this);
        tyres_view.setOnClickListener(this);
        body_work_view.setOnClickListener(this);
        towing_view.setOnClickListener(this);
        bike_nshowroom_view.setOnClickListener(this);
        race_track_view.setOnClickListener(this);
        racing_nacadmey_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bike_servicing_view:
                break;
            case R.id.gearsaccessories_view:
                break;
            case R.id.bike_parts_view:
                break;
            case R.id.bike_wash_view:
                break;
            case R.id.tyres_view:
                break;
            case R.id.body_work_view:
                break;
            case R.id.towing_view:
                break;
            case R.id.bike_nshowroom_view:
                break;
            case R.id.race_track_view:
                break;
            case R.id.racing_nacadmey_view:
                break;
        }
    }

    private void Hit_Get_FlyBy_User() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Get_all_garageowner();

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
                        Garage_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                         JSONArray GARAGEOWNERDETAILS = jsonObject.getJSONArray("GARAGEOWNERDETAILS");
                            for (int i=0 ; i<GARAGEOWNERDETAILS.length() ; i++)
                            {ArrayList <Category_Model> category_models = new ArrayList<>();

                                JSONObject js = GARAGEOWNERDETAILS.getJSONObject(i);
                                JSONArray CAT = js.getJSONArray("CAT");

                                Garage_Owner_Model go = new Garage_Owner_Model();
                                go.setGARAGEID(js.getString("GARAGEID"));
                                go.setOWNERNAME(js.getString("OWNERNAME"));
                                go.setSTORENAME(js.getString("STORENAME"));
                                go.setPHONE(js.getString("PHONE"));
                                go.setADDRESS(js.getString("ADDRESS"));
                                go.setCITY(js.getString("CITY"));
                                go.setLAT(js.getString("LAT"));
                                go.setLANG(js.getString("LANG"));
                                for (int j=0 ; j<CAT.length() ; j++)
                                { JSONObject jsd = CAT.getJSONObject(i);
                                    Category_Model cf = new Category_Model();
                                    cf.setID(jsd.getString("id"));
                                    cf.setName(jsd.getString("cat_name"));
                                    category_models.add(cf);
                                }
                                go.setCategory_models(category_models);
                                Garage_List.add(go);
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

    @Override
    public void onResume() {
        super.onResume();
        Hit_Get_FlyBy_User();
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