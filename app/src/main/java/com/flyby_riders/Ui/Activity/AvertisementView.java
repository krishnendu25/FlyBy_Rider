package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.GlobalApplication;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.Ui.Adapter.Garage.SliderAdapterExample;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Libraries.PhotoSlider.PhotoSlider;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.flyby_riders.Utils.ExpandableTextView;
import com.flyby_riders.Utils.Prefe;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.StringUtils.TASK_CONTACT_CLICK;
import static com.flyby_riders.Constants.StringUtils.TASK_PURCHASE;

public class AvertisementView extends AppCompatActivity {
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.imageSlider)
    SliderView imageSlider;
    @BindView(R.id.Garage_NAME)
    TextView GarageNAME;
    @BindView(R.id.priceTAGTv)
    TextView priceTAGTv;
    @BindView(R.id.priceView)
    LinearLayout priceView;
    @BindView(R.id.Album_cover_picture)
    ImageView AlbumCoverPicture;
    @BindView(R.id.garageNameTV)
    TextView garageNameTV;
    @BindView(R.id.garageCityNameTV)
    TextView garageCityNameTV;
    @BindView(R.id.garageNameView)
    RelativeLayout garageNameView;
    Context mContext;
    Activity mActivity;
    ArrayList<Garage_Advertisement> addList = new ArrayList<>();
    int pos = 0;
    @BindView(R.id.addDescriptionTV)
    ExpandableTextView addDescriptionTV;
    ArrayList<String> Images = new ArrayList<>();
    @BindView(R.id.whatsappBTN)
    RelativeLayout whatsappBTN;
    @BindView(R.id.callBTN)
    RelativeLayout callBTN;
    @BindView(R.id.buyLink_Re)
    LinearLayout buyLinkRe;
    @BindView(R.id.contactView)
    LinearLayout contactView;
    private String adPhoneNo,adWhatsapp,adBuyLink;
    public RetrofitCallback retrofitCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avertisement_view);
        ButterKnife.bind(this);
        iniView();
    }

    private void iniView() {
        try {
            pos = getIntent().getIntExtra("Position", 0);

            try {
                if (getIntent().getStringExtra("classname").equalsIgnoreCase("GarageDetailsView")){
                    try{
                        addList =   GarageDetailsView.garage_ads_list;
                        advertisementView(addList, pos);
                    }catch (Exception e){

                    }
                }
            } catch (Exception e) {
                try{
                    addList = My_Garage_Fragment.garage_ads_list;
                    advertisementView(addList, pos);
                }catch (Exception ee){

                }
            }



        } catch (Exception e) {
            Log.e("@Flyby", e.getMessage());
        }

        mContext = getApplicationContext();
        mActivity = AvertisementView.this;
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);

    }

    private void advertisementView(ArrayList<Garage_Advertisement> addList, int pos) {
        Garage_Advertisement ga = addList.get(pos);
        GarageNAME.setText(ga.getAdvertising_Title());
        garageNameTV.setText(ga.getGarageOwnerDetails().get(0).getGarageName());
        garageCityNameTV.setText(ga.getGarageOwnerDetails().get(0).getCity());
        addDescriptionTV.setText(ga.getAdvertising_Details());
        if (ga.getAdvertising_costPrice().equalsIgnoreCase("NA"))
            priceTAGTv.setText("Not Disclosed");
        else if (ga.getAdvertising_costPrice().equalsIgnoreCase("POR"))
            priceTAGTv.setText("Price On Request");
        else
            priceTAGTv.setText(getString(R.string.rupee) + " " + ga.getAdvertising_costPrice());

        try {
            Picasso.get().load("https://flybyapp.in/flybyapp/images/" + ga.getGarageOwnerDetails().get(0).getProfilePicture()).placeholder(R.drawable.images).into(AlbumCoverPicture);
        } catch (Exception e) {
            AlbumCoverPicture.setImageDrawable(getResources().getDrawable(R.drawable.images));
        }
        Images = splitByComma(ga.getAdvertising_Images(), ga.getADIMAGEPATH());
        Images.add(ga.Advertising_CoverPic);

        PhotoSlider photoSlider = new PhotoSlider(this, Images);
        SliderAdapterExample sliderAdapterExample =  new SliderAdapterExample(photoSlider,AvertisementView.this, Images, mActivity);
        imageSlider.setSliderAdapter(sliderAdapterExample);
        if (Images.size()==1){
            imageSlider.setSliderAdapter(sliderAdapterExample,false);
        }

        if (addList.get(pos).getAdvertising_userActions().get(0).getByeNow().equalsIgnoreCase("0")){
            buyLinkRe.setVisibility(View.GONE);
        }else
        {buyLinkRe.setVisibility(View.VISIBLE);
            contactView.setVisibility(View.GONE);
        adBuyLink = addList.get(pos).getAdvertising_userActions().get(0).getByeNow().trim();}

        if (addList.get(pos).getAdvertising_userActions().get(0).getContactStore().equalsIgnoreCase("0")){
            contactView.setVisibility(View.GONE);
        }else {
            contactView.setVisibility(View.VISIBLE);
            buyLinkRe.setVisibility(View.GONE);
            String rawContact = addList.get(pos).getAdvertising_userActions().get(0).getContactStore();
            if (rawContact.contains("-")) {
                String[] allIdsArray = TextUtils.split(rawContact, "-");
                ArrayList<String> rawContactList = new ArrayList<String>(Arrays.asList(allIdsArray));
                if (rawContactList.size() > 0)
                    adPhoneNo = rawContactList.get(0);
                if (rawContactList.size() > 1)
                    adWhatsapp = rawContactList.get(1);
            }
        }
    }

    private void hitAddClick(String task,String userid, String advID) {
        Call<ResponseBody> requestCall = retrofitCallback.click_advertise(task,userid,advID);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("@ClickOnAdd ",advID);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    @OnClick({R.id.Back_Btn, R.id.buyLink_Re, R.id.garageNameView, R.id.whatsappBTN, R.id.callBTN})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;

            case R.id.buyLink_Re:
                if (GlobalApplication.connectionDetector.isConnectingToInternet()){
                    hitAddClick(TASK_PURCHASE,new Prefe(mContext).getUserID(),addList.get(pos).Advertising_ID);
                }else{
                    Constant.Show_Tos(getApplicationContext(),"No internet connection found");
                }

                try {
                    Uri webpage;
                    if (adBuyLink.contains("http")){
                        webpage = Uri.parse(adBuyLink);
                    }else{ webpage = Uri.parse("http://"+adBuyLink);}
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(browserIntent);
                } catch (Exception e) {
                    try {
                        Uri webpage;
                        if (adBuyLink.contains("http")){
                             webpage = Uri.parse(adBuyLink);
                        }else{ webpage = Uri.parse("http://"+adBuyLink);}
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case R.id.garageNameView:
                Intent intent = new Intent(this, GarageDetailsView.class);
                intent.putExtra("Grage_Owner_ID", addList.get(pos).garageOwnerDetails.get(0).getID());
                startActivity(intent);
                break;
            case R.id.callBTN:
                try {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        ActivityCompat.requestPermissions((Activity) this, PERMISSIONS_STORAGE, 9);
                    } else {
                        try {
                            if (GlobalApplication.connectionDetector.isConnectingToInternet()){
                                hitanalytics(TASK_CONTACT_CLICK,(addList.get(pos).garageOwnerDetails.get(0).getID()));
                            }else{
                                Constant.Show_Tos(getApplicationContext(),"No internet connection found");
                            }

                        } catch (Exception e) {

                        }
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + adPhoneNo));
                        startActivity(callIntent);
                    }
                } catch (Exception E) {
                    Constant.Show_Tos_Error(this, false, true);
                }
                break;
            case R.id.whatsappBTN:
                try {
                    try {
                        if (GlobalApplication.connectionDetector.isConnectingToInternet()){
                            hitanalytics(TASK_CONTACT_CLICK,(addList.get(pos).garageOwnerDetails.get(0).getID()));
                        }else{
                            Constant.Show_Tos(getApplicationContext(),"No internet connection found");
                        }

                    } catch (Exception e) {

                    }
                    Constant.openWhatsApp(adWhatsapp, "", this);
                } catch (Exception E) {
                    Constant.Show_Tos_Error(this, false, true);
                }
                break;
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
            }
        });
    }
}