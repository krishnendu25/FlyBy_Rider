package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Discover.ADDClickListener;
import com.flyby_riders.Ui.Adapter.Discover.Advertisement_Adapter;
import com.flyby_riders.Ui.Adapter.Discover.Image_Silder_Adapter;
import com.flyby_riders.Ui.Adapter.Discover.Media_Slider_Click;
import com.flyby_riders.Ui.Model.ADD_MODEL;
import com.flyby_riders.Ui.Model.Garage_Media_Model;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;
import com.flyby_riders.Utils.BaseActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarageDetailsView extends BaseActivity implements ADDClickListener, Media_Slider_Click {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.Garage_DP)
    CircleImageView GarageDP;
    @BindView(R.id.Garage_NAME)
    TextView GarageNAME;
    @BindView(R.id.Garage_CITY)
    TextView GarageCITY;
    @BindView(R.id.Garage_Call)
    LinearLayout GarageCall;
    @BindView(R.id.Garage_Whatsapp)
    LinearLayout GarageWhatsapp;
    @BindView(R.id.Garage_Image_Slider)
    RecyclerView GarageImageSlider;
    @BindView(R.id.Garage_Descrption)
    TextView GarageDescrption;
    @BindView(R.id.Garage_Map_Dirtions)
    TextView GarageMapDirtions;
    @BindView(R.id.Garage_distance_unit)
    TextView GarageDistanceUnit;
    ArrayList<Garage_Owner_Model> Garage_Owner_List = new ArrayList<>();
    ArrayList<ADD_MODEL> add_fetch = new ArrayList<>();
    ArrayList<Garage_Media_Model> Garage_Media_List = new ArrayList<>();
    @BindView(R.id.Garage_Advetisment_list)
    ListView GarageAdvetismentList;
    int Position;
    Image_Silder_Adapter image_silder_adapter;
    @BindView(R.id.Empty_no_media_file)
    TextView EmptyNoMediaFile;
    @BindView(R.id.Empty_no_advertisement)
    TextView EmptyNoAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__information);
        ButterKnife.bind(this);
        try {
            Garage_Owner_List = getIntent().getParcelableArrayListExtra("List_Garage");
            Position = Integer.valueOf(getIntent().getStringExtra("Position"));
        } catch (Exception E) {
            Garage_Owner_List = new ArrayList<>();
        }
        Instantiation();

    }

    private void Instantiation() {
        Set_LayoutManager(GarageImageSlider, true, false);
        Set_Grage_View();
    }

    private void Set_Grage_View() {
        Hit_Add_Fetch(Garage_Owner_List.get(Position).getGARAGEID());
        GarageNAME.setText(Garage_Owner_List.get(Position).getSTORENAME());
        GarageCITY.setText(Garage_Owner_List.get(Position).getCITY());
        try {
            Picasso.get().load(Garage_Owner_List.get(Position).getPROFILEPIC()).placeholder(R.drawable.images).into(GarageDP);
        } catch (Exception e) {
            GarageDP.setImageDrawable(getResources().getDrawable(R.drawable.images));
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < Garage_Owner_List.get(Position).getCategory_models().size(); j++) {
            if (j == Garage_Owner_List.get(Position).getCategory_models().size() - 1) {
                stringBuilder.append(Garage_Owner_List.get(Position).getCategory_models().get(j).getName());
            } else {
                stringBuilder.append(Garage_Owner_List.get(Position).getCategory_models().get(j).getName() + " | ");
            }
        }
        GarageDescrption.setText(stringBuilder.toString());
        double Dis = Double.valueOf(Garage_Owner_List.get(Position).getDISTANCE_FROM_ME()) * 0.001;
        GarageDistanceUnit.setText(new DecimalFormat("##.##").format(Dis) + "km");
    }

    private void Hit_Add_Fetch(String login_user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.ad_post_fetch(login_user_id);
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
                        } catch (IOException e) {
                            Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Hit_Grage_Owner_Details(Garage_Owner_List.get(Position).getGARAGEID());
                            add_fetch.clear();
                            JSONArray Add_List = jsonObject.getJSONArray("ALLPOST");

                            for (int i = 0; i < Add_List.length(); i++) {
                                JSONObject add_count = Add_List.getJSONObject(i);
                                ADD_MODEL add_model = new ADD_MODEL();
                                if (add_count.has("ID"))
                                    add_model.setID(add_count.getString("ID"));
                                if (add_count.has("ADVID"))
                                    add_model.setADVID(add_count.getString("ADVID"));
                                if (add_count.has("TITLE"))
                                    add_model.setTITLE(add_count.getString("TITLE"));
                                if (add_count.has("DESC"))
                                    add_model.setDESC(add_count.getString("DESC"));
                                if (add_count.has("ADVIDEO"))
                                    add_model.setADVIDEO(add_count.getString("ADVIDEO"));
                                if (add_count.has("ADCOVERIMAGE"))
                                    add_model.setADCOVERIMAGE(add_count.getString("ADCOVERIMAGE"));
                                if (add_count.has("ADCOSTPRICE"))
                                    add_model.setADCOSTPRICE(add_count.getString("ADCOSTPRICE"));
                                if (add_count.has("AUDIENCE"))
                                    add_model.setAUDIENCE(add_count.getString("AUDIENCE"));
                                if (add_count.has("BIKEMODEL"))
                                    add_model.setBIKEMODEL(add_count.getString("BIKEMODEL"));
                                if (add_count.has("BIKEMODELID"))
                                    add_model.setBIKEMODELID(add_count.getString("BIKEMODELID"));
                                if (add_count.has("BIKEBRAND"))
                                    add_model.setBIKEBRAND(add_count.getString("BIKEBRAND"));
                                if (add_count.has("BIKEBRANDID"))
                                    add_model.setBIKEBRANDID(add_count.getString("BIKEBRANDID"));
                                if (add_count.has("STORELINK"))
                                    add_model.setSTORELINK(add_count.getString("STORELINK"));
                                if (add_count.has("CONTACT"))
                                    add_model.setCONTACT(add_count.getString("CONTACT"));
                                if (add_count.has("NOACTION"))
                                    add_model.setNOACTION(add_count.getString("NOACTION"));
                                if (add_count.has("CURENTSTATUS"))
                                    add_model.setCURENTSTATUS(add_count.getString("CURENTSTATUS"));
                                if (add_count.has("CREATIONDATE"))
                                    add_model.setCREATIONDATE(add_count.getString("CREATIONDATE"));
                                if (add_count.has("POSTDATE"))
                                    add_model.setPOSTDATE(add_count.getString("POSTDATE"));
                                if (add_count.has("POSTTIME"))
                                    add_model.setPOSTTIME(add_count.getString("POSTTIME"));
                                if (add_count.has("PUBLISHDATE"))
                                    add_model.setPUBLISHDATE(add_count.getString("PUBLISHDATE"));
                                if (add_count.has("POSTMONTH"))
                                    add_model.setPOSTMONTH(add_count.getString("POSTMONTH"));
                                if (add_count.has("EXPIREDDATE"))
                                    add_model.setEXPIREDDATE(add_count.getString("EXPIREDDATE"));
                                if (add_count.has("SEENCOUNT"))
                                    add_model.setSEENCOUNT(add_count.getString("SEENCOUNT"));
                                if (add_count.has("PURCHASECOUNT"))
                                    add_model.setPURCHASECOUNT(add_count.getString("PURCHASECOUNT"));
                                if (jsonObject.has("IMAGEADPATH"))
                                    add_model.setIMAGEADPATH(jsonObject.getString("IMAGEADPATH"));
                                if (jsonObject.has("IMAGECOVERPATH"))
                                    add_model.setIMAGECOVERPATH(jsonObject.getString("IMAGECOVERPATH"));
                                if (jsonObject.has("IMAGEVIDEOPATH"))
                                    add_model.setIMAGEVIDEOPATH(jsonObject.getString("IMAGEVIDEOPATH"));
                                if (add_count.has("ADIMAGES"))
                                    add_model.setADIMAGES(add_count.getString("ADIMAGES"));
                                String str = add_count.getString("ADIMAGES");
                                List<String> ADIMAGESList = Arrays.asList(str.split(","));
                                for (int k = 0; k < ADIMAGESList.size(); k++) {
                                    if (!ADIMAGESList.get(k).equalsIgnoreCase("")) {
                                        if (k == 0)
                                            add_model.setPICTURE_1(ADIMAGESList.get(k));
                                        if (k == 1)
                                            add_model.setPICTURE_2(ADIMAGESList.get(k));
                                        if (k == 2)
                                            add_model.setPICTURE_3(ADIMAGESList.get(k));
                                        if (k == 3)
                                            add_model.setPICTURE_4(ADIMAGESList.get(k));
                                        if (k == 4)
                                            add_model.setPICTURE_5(ADIMAGESList.get(k));
                                        if (k == 5)
                                            add_model.setPICTURE_6(ADIMAGESList.get(k));
                                        if (k == 6)
                                            add_model.setPICTURE_7(ADIMAGESList.get(k));
                                        if (k == 7)
                                            add_model.setPICTURE_8(ADIMAGESList.get(k));
                                    }
                                }
                                add_fetch.add(add_model);
                            }
                            if (add_fetch.size()>0){EmptyNoAdvertisement.setVisibility(View.GONE);}else{EmptyNoAdvertisement.setVisibility(View.VISIBLE);};
                            Advertisement_Adapter advertisement_adapter = new Advertisement_Adapter(GarageDetailsView.this, add_fetch,
                                    Garage_Owner_List.get(Position).getOWNERNAME());
                            GarageAdvetismentList.setAdapter(advertisement_adapter);
                            advertisement_adapter.notifyDataSetChanged();
                        } else {
                            EmptyNoAdvertisement.setVisibility(View.VISIBLE);
                            Hit_Grage_Owner_Details(Garage_Owner_List.get(Position).getGARAGEID());
                        }
                    } catch (JSONException e) {
                        EmptyNoAdvertisement.setVisibility(View.VISIBLE);
                        Hit_Grage_Owner_Details(Garage_Owner_List.get(Position).getGARAGEID());
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                EmptyNoAdvertisement.setVisibility(View.VISIBLE);
                Constant.Show_Tos_Error(getApplicationContext(), true, false);

            }
        });


    }

    private void Hit_Grage_Owner_Details(String login_user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Getgarageownerdetails(login_user_id);

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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Garage_Media_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            ArrayList<String> Images = new ArrayList<>();
                            String Video_URL = "";
                            JSONArray USERDETAILS_Sub = jsonObject.getJSONArray("USERDETAILS").getJSONArray(0);
                            JSONObject media_list = USERDETAILS_Sub.getJSONObject(2);
                            if (!media_list.getString("STORE_PIC").equalsIgnoreCase("") || !media_list.getString("STORE_PIC").equalsIgnoreCase("null")) {
                                Images = splitByComma(media_list.getString("STORE_PIC"), jsonObject.getString("IMAGEPATH"));
                            }
                            if (!media_list.getString("STORE_VIDEO").equalsIgnoreCase("") || media_list.getString("STORE_VIDEO").equalsIgnoreCase("null")) {
                                Video_URL = jsonObject.getString("VIDEOPATH") + media_list.getString("STORE_VIDEO");
                            }
                            try{
                                if (!Video_URL.equalsIgnoreCase("") && Images.size() != 0) {
                                    EmptyNoMediaFile.setVisibility(View.GONE);
                                    for (int i = 0; i < Images.size(); i++) {
                                        Garage_Media_Model garage_media_model = new Garage_Media_Model();
                                        garage_media_model.setFile_Type("IMAGES");
                                        garage_media_model.setFile_Url(Images.get(i));
                                        Garage_Media_List.add(garage_media_model);
                                    }
                                    Garage_Media_Model garage_media_model = new Garage_Media_Model();
                                    garage_media_model.setFile_Type("VIDEO");
                                    garage_media_model.setFile_Url(Video_URL);
                                    Garage_Media_List.add(garage_media_model);
                                    Set_Media_To_Store(Garage_Media_List);
                                }else{EmptyNoMediaFile.setVisibility(View.VISIBLE);}
                            }catch (Exception e){EmptyNoMediaFile.setVisibility(View.VISIBLE);}

                        } else {
                            EmptyNoMediaFile.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        EmptyNoMediaFile.setVisibility(View.VISIBLE);
                        Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EmptyNoMediaFile.setVisibility(View.VISIBLE);
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
            }
        });
    }

    private void Set_Media_To_Store(ArrayList<Garage_Media_Model> media_list) {
        if (media_list.size() != 0) {
            Collections.reverse(media_list);
            image_silder_adapter = new Image_Silder_Adapter(this, media_list);
            GarageImageSlider.setAdapter(image_silder_adapter);

        }
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


    @OnClick({R.id.Back_Btn, R.id.Garage_Call, R.id.Garage_Whatsapp, R.id.Garage_Map_Dirtions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.Garage_Call:
                try {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        ActivityCompat.requestPermissions((Activity) this, PERMISSIONS_STORAGE, 9);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Garage_Owner_List.get(Position).getPHONE()));
                        startActivity(callIntent);
                    }
                } catch (Exception E) {
                    Constant.Show_Tos_Error(this, false, true);
                }
                break;
            case R.id.Garage_Whatsapp:
                try {
                    Constant.openWhatsApp(Garage_Owner_List.get(Position).getPHONE(), "", this);
                } catch (Exception E) {
                    Constant.Show_Tos_Error(this, false, true);
                }
                break;
            case R.id.Garage_Map_Dirtions:
                try {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Garage_Owner_List.get(Position).getLAT() + "," + Garage_Owner_List.get(0).getLANG());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (Exception e) {
                }
                break;
        }
    }


    @Override
    public void setOnAdapterClick(int position) {

    }

    @Override
    public void MediaItemClick(int position) {
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("mVideo_url", Garage_Media_List.get(position).getFile_Url());
        startActivity(intent);

    }
}