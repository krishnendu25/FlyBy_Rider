package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Discover.Garage_Owner_Adapter;
import com.flyby_riders.Ui.Adapter.Discover.Garageownerclick;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;
import com.flyby_riders.Utils.ShadowLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class Garage_List extends BaseActivity implements Garageownerclick {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.Back_)
    ShadowLayout Back;
    @BindView(R.id.Sort_Btn)
    RelativeLayout SortBtn;
    @BindView(R.id.garage_list)
    RecyclerView garageList;
    boolean AtoZ_sort = false, Distance_sort = false;
    ArrayList<Garage_Owner_Model> Garage_Owner_List = new ArrayList<>();
    private double longitude=0,latitude=0;
    Garage_Owner_Adapter garage_owner_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__list);
        ButterKnife.bind(this);
        try {
            Garage_Owner_List = getIntent().getParcelableArrayListExtra("List_Garage");
        } catch (Exception E) {
            Garage_Owner_List = new ArrayList<>();
        }
        show_ProgressDialog();
        LocationTracker tracker = new LocationTracker(
                this,
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true)) {
            @Override
            public void onLocationFound(Location location) {
                try {hide_ProgressDialog();
                    Set_View(location.getLatitude(),location.getLongitude());
                }catch (Exception e){}
            }
            @Override
            public void onTimeout() {
                hide_ProgressDialog();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            hide_ProgressDialog();
            return;
        }
        tracker.startListening();



    }

    private void Set_View(double latitude, double longitude) {

        if (Garage_Owner_List.size()>0)
        {
            for (int i=0; i<Garage_Owner_List.size() ; i++)
            {
                Location startPoint=new Location("locationA");
                startPoint.setLatitude(latitude);
                startPoint.setLongitude(longitude);
                Location endPoint=new Location("locationA");
                endPoint.setLatitude(Double.valueOf(Garage_Owner_List.get(i).getLAT()));
                endPoint.setLongitude(Double.valueOf(Garage_Owner_List.get(i).getLANG()));
                Garage_Owner_List.get(i).setDISTANCE_FROM_ME(String.valueOf(startPoint.distanceTo(endPoint)));
            }
        }
        try {
            Set_LayoutManager(garageList,false,true);
            garage_owner_adapter = new Garage_Owner_Adapter(Garage_Owner_List,this);
            garageList.setAdapter(garage_owner_adapter);
        }catch (Exception e)
        { }
    }

    @OnClick({R.id.Back_Btn, R.id.Sort_Btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.Sort_Btn:
                opensortingbottomsheet(AtoZ_sort,Distance_sort);
                break;
        }
    }

    private void opensortingbottomsheet(boolean atoZ_sort, boolean distance_sort) {
        View dialogView = getLayoutInflater().inflate(R.layout.garage_sorting, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);
        TextView distance_tv = bottomSheetDialog.findViewById(R.id.distance_tv);
        TextView a_z_tv = bottomSheetDialog.findViewById(R.id.a_z_tv);
        if (Distance_sort)
        {
            distance_tv.setTextColor(getResources().getColor(R.color.light_green));
            a_z_tv.setTextColor(getResources().getColor(R.color.white));
            distance_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sortselected, 0);
            a_z_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else   if (AtoZ_sort)
        {
            distance_tv.setTextColor(getResources().getColor(R.color.white));
            a_z_tv.setTextColor(getResources().getColor(R.color.light_green));
            distance_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            a_z_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sortselected, 0);
        }
        distance_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance_tv.setTextColor(getResources().getColor(R.color.light_green));
                a_z_tv.setTextColor(getResources().getColor(R.color.white));
                distance_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sortselected, 0);
                a_z_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                Distance_sort = true;
                AtoZ_sort = false;
                Sorting_Post(Distance_sort,AtoZ_sort);
                bottomSheetDialog.hide();
            }
        });
        a_z_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distance_tv.setTextColor(getResources().getColor(R.color.white));
                a_z_tv.setTextColor(getResources().getColor(R.color.light_green));
                distance_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                a_z_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sortselected, 0);
                Distance_sort = false;
                AtoZ_sort = true;
                Sorting_Post(Distance_sort,AtoZ_sort);
                bottomSheetDialog.hide();
            }
        });
        bottomSheetDialog.show();
    }

    private void Sorting_Post(boolean distance_sort, boolean atoZ_sort) {



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void SelectOnClick(int Position) {
        Intent intent = new Intent(this,Garage_Information.class);
        intent.putExtra("List_Garage",Garage_Owner_List);
        intent.putExtra("Position",String.valueOf(Position));
        startActivity(intent);
    }
}