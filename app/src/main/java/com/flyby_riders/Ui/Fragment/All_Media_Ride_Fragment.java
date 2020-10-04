package com.flyby_riders.Ui.Fragment;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Activity.RideGalleryView;
import com.flyby_riders.Ui.Adapter.Ride.Ride_Gallery_Adapter;
import com.flyby_riders.Ui.Model.Ride_Media_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class All_Media_Ride_Fragment extends Fragment {
    private static All_Media_Ride_Fragment fragment;
    private AlertDialog alertDialog_loader = null;
    private RetrofitCallback retrofitCallback;
    public RecyclerView all_uploaded_list;
    ImageView Empty_View;
    String My_Ride_ID = "", Admin_User_Id = "";
    Ride_Gallery_Adapter ride_gallery_adapter;
    ArrayList<Ride_Media_Model> Ride_Media_List = new ArrayList<>();
    int Measuredwidth = 0;
    int Measuredheight = 0;

    public All_Media_Ride_Fragment() {
    }

    public static All_Media_Ride_Fragment newInstance() {
        if (fragment==null)
        {fragment = new All_Media_Ride_Fragment();return fragment; }
        else
        {return fragment;}

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all__media__ride, container, false);
        Instantiation(v);


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

        return  v;
    }

    private void Instantiation(View v) {
        Empty_View = v.findViewById(R.id.Empty_View);
        My_Ride_ID= RideGalleryView.My_Ride_ID;
        Admin_User_Id= RideGalleryView.Admin_User_Id;
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        all_uploaded_list = v.findViewById(R.id.all_uploaded_list);
        all_uploaded_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        Fetch_Media_Ride(My_Ride_ID);
    }

    public  void Fetch_Media_Ride(String my_ride_id) {
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
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }Ride_Media_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            String IMAGEPATH = jsonObject.getString("IMAGEPATH");

                            JSONArray ALBUMLIST = jsonObject.getJSONArray("ALBUMLIST");

                            for (int i = 0; i < ALBUMLIST.length(); i++) {

                                JSONObject obj = ALBUMLIST.getJSONObject(i);
                                ArrayList<String> Images = splitByComma(obj.getString("MEDIAFILE"),IMAGEPATH);

                                for (int j=0;j<Images.size();j++){
                                    Ride_Media_Model rm = new Ride_Media_Model();
                                    rm.setRIDEID(obj.getString("RIDEID"));
                                    rm.setMEDIAFILE_URL(Images.get(j));
                                    rm.setUPLOADER_ID(obj.getString("CUSTOMOBJECT"));
                                    rm.setTOTALFILESIZE(obj.getString("TOTALFILESIZE"));
                                    Ride_Media_List.add(rm);
                                }
                            }
                            ride_gallery_adapter = new Ride_Gallery_Adapter(getActivity(),Ride_Media_List,Measuredwidth/2);
                            all_uploaded_list.setAdapter(ride_gallery_adapter);
                            if (Ride_Media_List.size()>0)
                            { Empty_View.setVisibility(View.GONE); }

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
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getActivity(),true,false);

            }
        });
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