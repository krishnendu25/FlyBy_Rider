package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Activity.Ride_Gallery;
import com.flyby_riders.Ui.Adapter.Ride_Gallery_Adapter;
import com.flyby_riders.Ui.Model.Ride_Media_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Media_Ride_Fragment extends Fragment {
    private AlertDialog alertDialog_loader = null;
    private RetrofitCallback retrofitCallback;
    RecyclerView my_uploaded_list;
    static My_Media_Ride_Fragment fragment;
    String My_Ride_ID = "", Admin_User_Id = "";
    Ride_Gallery_Adapter ride_gallery_adapter;
    ArrayList<Ride_Media_Model> Ride_Media_List = new ArrayList<>();
    int Measuredwidth = 0;
    int Measuredheight = 0;
    public My_Media_Ride_Fragment() {
    }

    public static My_Media_Ride_Fragment newInstance() {
        if (fragment==null)
        { fragment = new My_Media_Ride_Fragment();return fragment; }
        else
        {return fragment; }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my__media__ride_, container, false);
        Instantiation(view);
        Fetch_Media_Ride(My_Ride_ID);
        Point size = new Point();
        WindowManager w = getActivity().getWindowManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        return view;
    }
    private void Instantiation(View view) {
        My_Ride_ID= Ride_Gallery.My_Ride_ID;
        Admin_User_Id=Ride_Gallery.Admin_User_Id;
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        my_uploaded_list= view.findViewById(R.id.my_uploaded_list);
        my_uploaded_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void onResume() {
        super.onResume();

    }




    public void Fetch_Media_Ride(String my_ride_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_ride_album(my_ride_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }Ride_Media_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            String IMAGEPATH = jsonObject.getString("IMAGEPATH");

                            JSONArray ALBUMLIST = jsonObject.getJSONArray("ALBUMLIST");

                            for (int i = 0; i < ALBUMLIST.length(); i++) {

                                JSONObject obj = ALBUMLIST.getJSONObject(i);
                                ArrayList<String> Images = splitByComma(obj.getString("MEDIAFILE"),IMAGEPATH);

                                for (int j=0;j<Images.size();j++) {
                                    Ride_Media_Model rm = new Ride_Media_Model();
                                    rm.setRIDEID(obj.getString("RIDEID"));
                                    rm.setMEDIAFILE_URL(Images.get(j));
                                    rm.setUPLOADER_ID(obj.getString("CUSTOMOBJECT"));
                                    rm.setTOTALFILESIZE(obj.getString("TOTALFILESIZE"));
                                    Ride_Media_List.add(rm);
                                }
                            }

                            Filter_List(Ride_Media_List,new Session(getActivity()).get_LOGIN_USER_ID());

                        } else {
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        hide_ProgressDialog();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog(); }
        });
    }

    private void Filter_List(ArrayList<Ride_Media_Model> ride_media_list, String User_Id) {

        for (int i=0 ; i<ride_media_list.size() ; i++)
        { if (!ride_media_list.get(i).getUPLOADER_ID().equalsIgnoreCase(User_Id))
            {ride_media_list.remove(i);}
        }

        ride_gallery_adapter = new Ride_Gallery_Adapter(getActivity(),Ride_Media_List,Measuredwidth/2);
        my_uploaded_list.setAdapter(ride_gallery_adapter);

    }


    public static ArrayList<String> splitByComma(String allIds, String imagepath) {
        ArrayList<String> images = new ArrayList<>();
        String[] allIdsArray = TextUtils.split(allIds, ",");
        ArrayList<String> idsList = new ArrayList<String>(Arrays.asList(allIdsArray));
        for (String element : idsList) {
            images.add(imagepath + element);
        }
        return images;
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