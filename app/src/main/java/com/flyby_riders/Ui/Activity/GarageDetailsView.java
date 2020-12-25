package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
import com.flyby_riders.Ui.Libraries.PhotoSlider.PhotoSlider;
import com.flyby_riders.Ui.Model.ADD_MODEL;
import com.flyby_riders.Ui.Model.Category_Model;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.flyby_riders.Ui.Model.Garage_Media_Model;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;
import com.flyby_riders.Utils.BaseActivity;
import com.flyby_riders.Utils.Helper;
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

import static com.flyby_riders.Constants.StringUtils.TASK_CONTACT_CLICK;
import static com.flyby_riders.Constants.StringUtils.TASK_VISIT;

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
    String Grage_Owner_ID = "";
    @BindView(R.id.details_1)
    TextView details1;
    @BindView(R.id.details_2)
    TextView details2;
    @BindView(R.id.details_3)
    TextView details3;
    @BindView(R.id.detailsReasonView)
    LinearLayout detailsReasonView;
    boolean isPremium = false;
    public static ArrayList<Garage_Advertisement> garage_ads_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__information);
        ButterKnife.bind(this);
        Instantiation();
        try {
            Grage_Owner_ID = getIntent().getStringExtra("Grage_Owner_ID");
            Hit_Grage_Owner_Details(Grage_Owner_ID);
        } catch (Exception E) {
        }


    }


    private void Instantiation() {
        Set_LayoutManager(GarageImageSlider, true, false);

    }

    private void Set_Grage_View() {


        if (Garage_Owner_List.get(Position).getWHATSAPPNO().trim().equalsIgnoreCase("")) {
            GarageWhatsapp.setEnabled(false);
            GarageWhatsapp.setAlpha(0.3f);
        }
        if (Garage_Owner_List.get(Position).getPHONE().trim().equalsIgnoreCase("")) {
            GarageCall.setEnabled(false);
            GarageCall.setAlpha(0.3f);
        }
        hitanalytics(TASK_VISIT,(Garage_Owner_List.get(Position).getGARAGEID()));
        Hit_Add_Fetch(Garage_Owner_List.get(Position).getGARAGEID());
        GarageNAME.setText(Garage_Owner_List.get(Position).getSTORENAME());
        GarageCITY.setText(Garage_Owner_List.get(Position).getCITY());
        details1.setText(Garage_Owner_List.get(Position).getDetails_1());
        details2.setText(Garage_Owner_List.get(Position).getDetails_2());
        details3.setText(Garage_Owner_List.get(Position).getDetails_3());

        if (Garage_Owner_List.get(Position).getDetails_1().trim().equalsIgnoreCase("")&&
                Garage_Owner_List.get(Position).getDetails_2().trim().equalsIgnoreCase("")&&
                Garage_Owner_List.get(Position).getDetails_3().trim().equalsIgnoreCase("")){
            detailsReasonView.setVisibility(View.GONE);
        }else{
            detailsReasonView.setVisibility(View.VISIBLE);
        }

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
                            if (add_fetch.size() > 0) {
                                EmptyNoAdvertisement.setVisibility(View.GONE);
                            } else {
                                EmptyNoAdvertisement.setVisibility(View.VISIBLE);
                            }
                            ArrayList<ADD_MODEL> temp = filterByData(add_fetch);
                            Collections.reverse(temp);
                            Advertisement_Adapter advertisement_adapter = new Advertisement_Adapter(GarageDetailsView.this, temp,
                                    Garage_Owner_List.get(Position).getOWNERNAME());
                            GarageAdvetismentList.setAdapter(advertisement_adapter);
                            advertisement_adapter.notifyDataSetChanged();
                            try {
                                Helper.getListViewSize(GarageAdvetismentList);
                            } catch (Exception e) {
                            }
                        } else {
                            EmptyNoAdvertisement.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        EmptyNoAdvertisement.setVisibility(View.VISIBLE);
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

    private ArrayList<ADD_MODEL> filterByData(ArrayList<ADD_MODEL> add_fetch) {
        ArrayList<ADD_MODEL> temp = new ArrayList<>();
        String Date = Constant.Get_back_date(Constant.GET_timeStamp());
        int day=0;
        if(isPremium){
            day=90;
        }else{day=7;}


        for (int i=0 ; i<add_fetch.size(); i++){
            String Differ = Constant.getCountOfDays(add_fetch.get(i).getPOSTDATE(),Date);
            if (Integer.parseInt(Differ.replaceAll("Days","").trim())<day){
                temp.add(add_fetch.get(i)) ;
            }
        }
        return temp;
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
                            try {
                                isPremium = getDetailsPlan(USERDETAILS_Sub.getJSONObject(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (!media_list.getString("STORE_PIC").equalsIgnoreCase("") || !media_list.getString("STORE_PIC").equalsIgnoreCase("null")) {
                                Images = splitByComma(media_list.getString("STORE_PIC"), jsonObject.getString("IMAGEPATH"));
                            }
                            if (!media_list.getString("STORE_VIDEO").equalsIgnoreCase("") || media_list.getString("STORE_VIDEO").equalsIgnoreCase("null")) {
                                Video_URL = jsonObject.getString("VIDEOPATH") + media_list.getString("STORE_VIDEO");
                            }
                            try {
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
                                } else {
                                    EmptyNoMediaFile.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                EmptyNoMediaFile.setVisibility(View.GONE);
                            }
                            if (Garage_Owner_List.size() == 0) {
                                ArrayList<Category_Model> category_models = new ArrayList<>();
                                JSONObject grageOwner = USERDETAILS_Sub.getJSONObject(0);
                                JSONArray CAT = grageOwner.getJSONArray("CAT");
                                Garage_Owner_Model go = new Garage_Owner_Model();
                                go.setGARAGEID(grageOwner.getString("ID"));
                                go.setOWNERNAME(grageOwner.getString("FIRSTNAME"));
                                go.setSTORENAME(grageOwner.getString("STORE"));
                                go.setPHONE(grageOwner.getString("PHONE"));
                                go.setWHATSAPPNO(grageOwner.getString("WHATSAPPNO"));
                                go.setADDRESS(grageOwner.getString("ADDRESS"));
                                go.setCITY(grageOwner.getString("CITY"));
                                go.setLAT(grageOwner.getString("GLAT"));
                                go.setLANG(grageOwner.getString("GLANG"));
                                go.setDetails_1(grageOwner.getString("STORE DETAILS 1"));
                                go.setDetails_2(grageOwner.getString("STORE DETAILS 2"));
                                go.setDetails_3(grageOwner.getString("STORE DETAILS 3"));
                                go.setPROFILEPIC(jsonObject.getString("IMAGEPATH") + grageOwner.getString("PIC"));
                                for (int j = 0; j < CAT.length(); j++) {
                                    JSONObject jsd = CAT.getJSONObject(j);
                                    Category_Model cf = new Category_Model();
                                    cf.setID(jsd.getString("id"));
                                    cf.setName(jsd.getString("cat_name"));
                                    category_models.add(cf);
                                }
                                go.setCategory_models(category_models);
                                Garage_Owner_List.add(go);

                                Set_Grage_View();
                            }
                        }
                    } catch (Exception e) {
                        Constant.Show_Tos_Error(getApplicationContext(), false, true);
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
            }
        });
    }

    private void Set_Media_To_Store(ArrayList<Garage_Media_Model> media_list) {
        if (media_list.size() != 0) {
            Collections.reverse(media_list);
            ArrayList<String> temp = new ArrayList<>();
            for (int i=0 ; i<media_list.size();i++){
                temp.add(media_list.get(i).getFile_Url());
            }
            PhotoSlider photoSlider = new PhotoSlider(this, temp);
            image_silder_adapter = new Image_Silder_Adapter(photoSlider,this, media_list);
            GarageImageSlider.setAdapter(image_silder_adapter);

        }
    }

    private boolean getDetailsPlan(JSONObject jsonObject) throws JSONException {
        String Current_Plan_Id=jsonObject.getString("Current_Plan_Id");

        if (Current_Plan_Id.equalsIgnoreCase("3")){
            return true;
        }else if (Current_Plan_Id.equalsIgnoreCase("4")){
            return true;
        }else{
            return false;
        }
    }

    public static ArrayList<String> splitByComma(String allIds, String imagepath) {
        ArrayList<String> images = new ArrayList<>();
        String[] allIdsArray = TextUtils.split(allIds, ",");
        ArrayList<String> idsList = new ArrayList<String>(Arrays.asList(allIdsArray));
        for (String element : idsList) {
            String url = imagepath + element;
            if (!url.trim().equalsIgnoreCase(imagepath)) {
                images.add(url);
            }
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
                        hitanalytics(TASK_CONTACT_CLICK,(Garage_Owner_List.get(Position).getGARAGEID()));
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
                    hitanalytics(TASK_CONTACT_CLICK,(Garage_Owner_List.get(Position).getGARAGEID()));
                    Constant.openWhatsApp(Garage_Owner_List.get(Position).getPHONE(), "", this);
                } catch (Exception E) {
                    Constant.Show_Tos_Error(this, false, true);
                }
                break;
            case R.id.Garage_Map_Dirtions:
                try {
                  /*  Uri gmmIntentUri = Uri.parse("google.navigation:q=" );
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);*/


                    String strUri = "http://maps.google.com/maps?q=loc:" + Garage_Owner_List.get(Position).getLAT() + "," + Garage_Owner_List.get(0).getLANG() + " (" + "" + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } catch (Exception e) {
                }
                break;
        }
    }



    @Override
    public void MediaItemClick(int position) {
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("mVideo_url", Garage_Media_List.get(position).getFile_Url());
        startActivity(intent);

    }

    private void hitanalytics(String task, String userid) {
        Call<ResponseBody> requestCall = retrofitCallback.clickEventOnGarageOwner(task, userid);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });
    }

    @Override
    public void setOnAdapterClick(int position, String addID) {
        ArrayList<ADD_MODEL> temp = new ArrayList<>();
        garage_ads_list.clear();
        if (add_fetch.size()>0){
            for (int i=0 ; i<add_fetch.size();i++){
                if (add_fetch.get(i).getADVID().equalsIgnoreCase(addID)){
                    garage_ads_list = setGarage_Advertisement(add_fetch.get(i));
                    break;
                }
            }
        }
        Intent intent = new Intent(this, AvertisementView.class);
        intent.putExtra("Position", 0);
        intent.putExtra("classname", "GarageDetailsView");
        startActivity(intent);


    }

    private  ArrayList<Garage_Advertisement> setGarage_Advertisement(ADD_MODEL temp) {
        ArrayList<Garage_Advertisement> addList = new ArrayList<>();
        Garage_Advertisement grg = new Garage_Advertisement();
        grg.setAdvertising_ID(temp.getID());
        grg.setAdvertising_Title(temp.getTITLE());
        grg.setAdvertising_Details(temp.getDESC());
        grg.setAdvertising_Video(temp.getIMAGEVIDEOPATH()+temp.getADVIDEO());
       //Set Garage OwnerOwnerDetailsDetails
        ArrayList<Garage_Advertisement.GarageOwnerDetails> temp2 = new    ArrayList<Garage_Advertisement.GarageOwnerDetails>();
        Garage_Advertisement.GarageOwnerDetails garageOwnerDetails = new Garage_Advertisement.GarageOwnerDetails();
        garageOwnerDetails.setAddress(Garage_Owner_List.get(Position).getADDRESS());
        garageOwnerDetails.setCity(Garage_Owner_List.get(Position).getCITY());
        garageOwnerDetails.setGarageName(Garage_Owner_List.get(Position).getSTORENAME());
        garageOwnerDetails.setID(Garage_Owner_List.get(Position).getGARAGEID());
        garageOwnerDetails.setUserName(Garage_Owner_List.get(Position).getOWNERNAME());
        garageOwnerDetails.setProfilePicture(Garage_Owner_List.get(Position).getPROFILEPIC());
        temp2.add(garageOwnerDetails);
        grg.setGarageOwnerDetails(temp2);
        grg.setAdvertising_PostDate(temp.getPUBLISHDATE());
        grg.setAdvertising_costPrice(temp.getADCOSTPRICE());
        grg.setADIMAGEPATH(temp.getIMAGEADPATH());
        grg.setAdvertising_CoverPic(temp.getIMAGECOVERPATH()+temp.getADCOVERIMAGE());
        grg.setAdvertising_Images(temp.getADIMAGES());
        ArrayList<Garage_Advertisement.Advertising_UserAction> temp3 = new ArrayList<>();
        Garage_Advertisement.Advertising_UserAction userAction = new Garage_Advertisement.Advertising_UserAction();
        userAction.setByeNow(temp.getSTORELINK());
        userAction.setContactStore(temp.getCONTACT());
        userAction.setNoUserAction(temp.getNOACTION());
        temp3.add(userAction);
        grg.setAdvertising_userActions(temp3);
        addList.add(grg);
        return addList;
    }
}