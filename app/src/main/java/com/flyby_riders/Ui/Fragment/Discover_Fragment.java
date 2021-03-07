package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Activity.AllGarageList;
import com.flyby_riders.Ui.Adapter.Discover.Catagoryonclick;
import com.flyby_riders.Ui.Adapter.Discover.Category_Adapter;
import com.flyby_riders.Ui.Model.Category_Model;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Discover_Fragment extends Fragment implements Catagoryonclick {
    private static Discover_Fragment fragment;
    public RetrofitCallback retrofitCallback;
    GridView Services_List;
    ArrayList<Garage_Owner_Model> Garage_List = new ArrayList<>();
    Category_Adapter category_adapter;
    ShimmerFrameLayout shimmer_view_container;
    RelativeLayout shimmerView;
    private AlertDialog alertDialog_loader = null;
    private ArrayList<Category_Model> Category_List = new ArrayList<>();

    public Discover_Fragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static Discover_Fragment newInstance() {
        fragment = new Discover_Fragment();

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
        Services_List = v.findViewById(R.id.Services_List);
        shimmer_view_container = v.findViewById(R.id.shimmer_view_container);
        shimmerView = v.findViewById(R.id.shimmerView);

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

                        }
                        Garage_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            JSONArray GARAGEOWNERDETAILS = jsonObject.getJSONArray("GARAGEOWNERDETAILS");
                            for (int i = 0; i < GARAGEOWNERDETAILS.length(); i++) {
                                ArrayList<Category_Model> category_models = new ArrayList<>();

                                JSONObject js = GARAGEOWNERDETAILS.getJSONObject(i);
                                JSONArray CAT = js.getJSONArray("CAT");
                                Garage_Owner_Model go = new Garage_Owner_Model();
                                go.setGARAGEID(js.getString("GARAGEID"));
                                go.setOWNERNAME(js.getString("OWNERNAME"));
                                go.setSTORENAME(js.getString("STORENAME"));
                                go.setPHONE(js.getString("PHONE"));
                                go.setWHATSAPPNO(js.getString("WhatsappNo"));
                                go.setADDRESS(js.getString("ADDRESS"));
                                go.setCITY(js.getString("CITY"));
                                go.setLAT(js.getString("LAT"));
                                go.setLANG(js.getString("LANG"));
                                try {
                                    go.setDetails_1(js.getString("STORE DETAILS 1"));
                                } catch (Exception e) {

                                }
                                try {
                                    go.setDetails_2(js.getString("STORE DETAILS 2"));
                                } catch (Exception e) {

                                }
                                try {
                                    go.setDetails_3(js.getString("STORE DETAILS 3"));
                                } catch (Exception e) {

                                }
                                go.setPROFILEPIC(jsonObject.getString("IMAGEPATH") + js.getString("PROFILEPIC"));
                                for (int j = 0; j < CAT.length(); j++) {
                                    JSONObject jsd = CAT.getJSONObject(j);
                                    Category_Model cf = new Category_Model();
                                    cf.setID(jsd.getString("id"));
                                    cf.setName(jsd.getString("cat_name"));
                                    category_models.add(cf);
                                }
                                go.setCategory_models(category_models);
                                //Active User
                                if (!js.getString("PLAN_ID").equalsIgnoreCase("0") &&
                                        !js.getString("ACCOUNT_TYPE").equalsIgnoreCase("0")) {
                                    //Basic Promo--Anually-Monthly Active
                                    Garage_List.add(go);
                                } else if (js.getString("ACCOUNT_TYPE").equalsIgnoreCase("1")) {
                                    //For Basic Listing
                                    Garage_List.add(go);
                                }
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
                Constant.Show_Tos_Error(getActivity(), true, false);

            }
        });
    }

    private void hit_service() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.getAllServices();

        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (IOException e) {
                            hide_ProgressDialog();
                        }
                        hide_ProgressDialog();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Hit_Get_FlyBy_User();
                            Category_List.clear();
                            JSONArray jsonArray_AllCategory = jsonObject.getJSONArray("AllCategory");
                            for (int i = 0; i < jsonArray_AllCategory.length(); i++) {
                                Category_Model category_model = new Category_Model(
                                        jsonArray_AllCategory.getJSONObject(i).getString("Name"),
                                        jsonArray_AllCategory.getJSONObject(i).getString("ID"));

                                Category_List.add(category_model);
                            }
                            Collections.reverse(Category_List);
                            category_adapter = new Category_Adapter(getActivity(), Category_List, Discover_Fragment.this);
                            Services_List.setAdapter(category_adapter);
                        } else {
                            hide_ProgressDialog();
                        }
                    } catch (JSONException e) {

                        hide_ProgressDialog();
                    }
                } else
                    hide_ProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getActivity(), true, false);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        hit_service();
    }


    @Override
    public void SetOnClick(int Position) {
        try {

        } catch (Exception e) {

        }
        String Catagory = Category_List.get(Position).getName();
        ArrayList<Garage_Owner_Model> temp_list = new ArrayList<>();
        for (int i = 0; i < Garage_List.size(); i++) {
            for (int j = 0; j < Garage_List.get(i).getCategory_models().size(); j++) {
                if (Garage_List.get(i).getCategory_models().get(j).getName().equalsIgnoreCase(Catagory)) {
                    temp_list.add(Garage_List.get(i));
                }
            }
        }
        HashSet<Garage_Owner_Model> hashSet = new HashSet<Garage_Owner_Model>();
        hashSet.addAll(temp_list);
        temp_list.clear();
        temp_list.addAll(hashSet);

        try {
            Intent intent = new Intent(getActivity(), AllGarageList.class);
            intent.putExtra("List_Garage", temp_list);
            intent.putExtra("categoryTitle", Catagory);
            startActivity(intent);
        } catch (Exception e) {

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