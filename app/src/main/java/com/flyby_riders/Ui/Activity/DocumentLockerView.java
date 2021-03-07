package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Adapter.DocumentLocker.My_Album_View;
import com.flyby_riders.Ui.Adapter.Garage.My_Bike_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Album_Content_Model;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.Constant.splitByComma;

public class DocumentLockerView extends BaseActivity implements onClick {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.Empty_View)
    RelativeLayout EmptyView;
    @BindView(R.id.My_Bike_list)
    RecyclerView MyBikeList;
    @BindView(R.id.Bike_Document_tv)
    TextView BikeDocumentTv;
    @BindView(R.id.Add_Document_btn)
    LinearLayout AddDocumentBtn;
    @BindView(R.id.My_Album_List)
    RecyclerView MyAlbumList;
    @BindView(R.id.Document_upload_view)
    LinearLayout DocumentUploadView;
    ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    ArrayList<Album_Content_Model> Album_Content_list = new ArrayList<>();
    String Select_My_Bike_ID = "";
    private My_Album_View mb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_locker);
        ButterKnife.bind(this);
        Instantiation();
        Request_Permission();

    }

    private void Request_Permission() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE},
                    235);
        }
    }

    private void Instantiation() {
        MyBikeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MyAlbumList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ACTIVITYTITEL.setText("DOCUMENT LOCKER");
    }

    private void Set_View(ArrayList<My_Bike_Model> my_bike_els, int i) {
        if (my_bike_els.size()>0)
        {
            try{
                Select_My_Bike_ID = my_bike_els.get(i).getMY_BIKE_ID();
                BikeDocumentTv.setText("Documents for " + my_bike_els.get(i).getMODELNAME());
                hit_get_my_album(my_bike_els.get(i).getMY_BIKE_ID());
            }catch (Exception e)
            {
                Constant.Show_Tos_Error(getApplicationContext(),false,true);

            }
        }
    }

    @OnClick({R.id.Back_Btn, R.id.Add_Document_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.Add_Document_btn:
                Intent i = new Intent(this, DocumentAlbumMaker.class);
                i.putExtra("Select_My_Bike_ID", Select_My_Bike_ID);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onClick(String Value) {
        int Pos = Integer.parseInt(Value);
        Set_View(My_Bike_els, Pos);
    }

    @Override
    public void onLongClick(String Value) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (My_Bike_els.size()<1)
        {Hit_Rider_Details(new Prefe(this).getUserID()); }

        if (Select_My_Bike_ID != null) {
            if (!Select_My_Bike_ID.equalsIgnoreCase("")) {
                hit_get_my_album(Select_My_Bike_ID);
            }
        }

    }

    private void hit_get_my_album(String select_my_bike_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Fetch_Album(new Prefe(this).getUserID(),
                select_my_bike_id);
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
                        } Album_Content_list.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            if (jsonObject.has("ALBUMLIST")) {
                                JSONArray Album_list = jsonObject.getJSONArray("ALBUMLIST");

                                for (int i = 0; i < Album_list.length(); i++) {
                                    JSONObject obj = Album_list.getJSONObject(i);
                                    Album_Content_Model ab = new Album_Content_Model();
                                    Create_List_Album(obj, jsonObject.getString("IMAGEPATH"),ab);
                                }
                            }
                            if (Album_Content_list.size()>0)
                            {
                                Collections.reverse(Album_Content_list);
                                 mb = new My_Album_View(Album_Content_list, DocumentLockerView.this);
                                MyAlbumList.setAdapter(mb);
                            }

                        } else {
                            if (mb!=null){
                            mb.notifyDataSetChanged();}
                            hide_ProgressDialog();
                        }


                    } catch (Exception e) {
                        if (mb!=null){
                            mb.notifyDataSetChanged();}
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mb!=null){
                    mb.notifyDataSetChanged();}
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });

    }

    private void Create_List_Album(JSONObject obj, String imagepath, Album_Content_Model ab) {
        ArrayList<String> images = new ArrayList<>();

        try {
            ab.setALBUM_ID(obj.getString("ALBUMID"));
            ab.setALBUM_NAME(obj.getString("FILENAME"));
            ab.setBIKEIDocMAGES(splitByComma(obj.getString("FILEIMAGES"), imagepath));
        } catch (Exception e) {
        }
        Album_Content_list.add(ab);
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
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
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
                                        My_Bike_Adapter b = new My_Bike_Adapter(My_Bike_els, DocumentLockerView.this);
                                        MyBikeList.setAdapter(b);
                                        Set_View(My_Bike_els, 0); }
                                }else
                                {
                                }
                            }else
                            {
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
                hide_ProgressDialog();Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });
    }




}