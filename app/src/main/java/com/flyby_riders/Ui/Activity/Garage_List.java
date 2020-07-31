package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Utils.ShadowLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Garage_List extends BaseActivity  {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.Back_)
    ShadowLayout Back;
    @BindView(R.id.Sort_Btn)
    RelativeLayout SortBtn;
    @BindView(R.id.garage_list)
    RecyclerView garageList;
    boolean AtoZ_sort=false , Distance_sort=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__list);
        ButterKnife.bind(this);
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
}