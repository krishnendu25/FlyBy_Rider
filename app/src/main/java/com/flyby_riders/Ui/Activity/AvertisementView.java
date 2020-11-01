package com.flyby_riders.Ui.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Garage.SliderAdapterExample;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.flyby_riders.Utils.ExpandableTextView;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.flyby_riders.Ui.Fragment.My_Garage_Fragment.garage_ads_list;

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
    @BindView(R.id.byeProductBTN)
    TextView byeProductBTN;
    Context mContext;
    Activity mActivity;
    ArrayList<Garage_Advertisement> addList = new ArrayList<>();
    int pos = 0;
    @BindView(R.id.addDescriptionTV)
    ExpandableTextView addDescriptionTV;
    ArrayList<String> Images = new ArrayList<>();
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
            addList = garage_ads_list;
            advertisementView(addList, pos);
        } catch (Exception e) {
            Log.e("@Flyby",e.getMessage());
        }

        mContext = getApplicationContext();
        mActivity = AvertisementView.this;


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
            priceTAGTv.setText(getString(R.string.rupee)+" "+ga.getAdvertising_costPrice());

        try{
                Picasso.get().load("https://flybyapp.com/flybyapp/images/"+ga.getGarageOwnerDetails().get(0).getProfilePicture()).placeholder(R.drawable.images).into(AlbumCoverPicture);
        }catch (Exception e)
        {
            AlbumCoverPicture.setImageDrawable(getResources().getDrawable(R.drawable.images));
        }

        Images = splitByComma(ga.getAdvertising_Images(), ga.getADIMAGEPATH());
        Images.add(ga.Advertising_CoverPic);
        imageSlider.setSliderAdapter(new SliderAdapterExample(AvertisementView.this,Images,mActivity));

    }

    @OnClick({R.id.Back_Btn, R.id.byeProductBTN,R.id.garageNameView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.byeProductBTN:
                break;
            case R.id.garageNameView:
                Intent intent = new Intent(this, GarageDetailsView.class);
                intent.putExtra("Grage_Owner_ID", addList.get(pos).garageOwnerDetails.get(0).getID());
                startActivity(intent);
                break;
        }
    }

    public static ArrayList<String> splitByComma(String allIds, String imagepath) {
        ArrayList<String> images = new ArrayList<>();
        String[] allIdsArray = TextUtils.split(allIds, ",");
        ArrayList<String> idsList = new ArrayList<String>(Arrays.asList(allIdsArray));
        for (String element : idsList) {
            String url = imagepath + element;
            if (!url.trim().equalsIgnoreCase(imagepath))
            {
                images.add(url);
            }
        }
        return images;
    }
}