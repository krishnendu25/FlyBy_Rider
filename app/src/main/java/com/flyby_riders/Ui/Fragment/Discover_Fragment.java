package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

import com.airbnb.lottie.LottieAnimationView;
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
    GridView Services_List;
    public RetrofitCallback retrofitCallback;
    private AlertDialog alertDialog_loader = null;
    ArrayList<Garage_Owner_Model> Garage_List = new ArrayList<>();
    private ArrayList<Category_Model> Category_List = new ArrayList<>();
    Category_Adapter category_adapter;
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
                                go.setPROFILEPIC(jsonObject.getString("IMAGEPATH")+js.getString("PROFILEPIC"));
                                for (int j=0 ; j<CAT.length() ; j++)
                                { JSONObject jsd = CAT.getJSONObject(j);
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
                Constant.Show_Tos_Error(getActivity(),true,false);

            }
        });
    }
    private void hit_service() {

        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.getAllServices();

        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Hit_Get_FlyBy_User();
                            Category_List.clear();
                            JSONArray jsonArray_AllCategory = jsonObject.getJSONArray("AllCategory");
                            for (int i = 0; i < jsonArray_AllCategory.length(); i++) {
                                Category_Model category_model = new Category_Model(
                                        jsonArray_AllCategory.getJSONObject(i).getString("Name").toString(),
                                        jsonArray_AllCategory.getJSONObject(i).getString("ID").toString());

                                Category_List.add(category_model);
                            }
                            Collections.reverse(Category_List);
                            category_adapter = new Category_Adapter(getActivity(), Category_List,Discover_Fragment.this);
                            Services_List.setAdapter(category_adapter);
                        } else {
                            hide_ProgressDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();Constant.Show_Tos_Error(getActivity(),true,false);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        hit_service();
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
    public void SetOnClick(int Position) { try {

        }catch (Exception e)
        {

        }
        String Catagory = Category_List.get(Position).getName();
        ArrayList<Garage_Owner_Model> temp_list = new ArrayList<>();
        for (int i = 0 ; i< Garage_List.size(); i++)
        {
            for (int j= 0 ; j< Garage_List.get(i).getCategory_models().size(); j++)
            {
                if (Garage_List.get(i).getCategory_models().get(j).getName().equalsIgnoreCase(Catagory))
                {
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
            intent.putExtra("List_Garage",temp_list);
            intent.putExtra("categoryTitle",Catagory);
            startActivity(intent);
        }catch (Exception e)
        {

        }



    }
}