package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Garage_Information extends BaseActivity {

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
    @BindView(R.id.Garage_Advetisment_list)
    RecyclerView GarageAdvetismentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__information);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.Back_Btn, R.id.Garage_Call, R.id.Garage_Whatsapp, R.id.Garage_distance_unit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                break;
            case R.id.Garage_Call:
                break;
            case R.id.Garage_Whatsapp:
                break;
            case R.id.Garage_distance_unit:
                break;
        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}