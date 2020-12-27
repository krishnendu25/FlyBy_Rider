package com.flyby_riders.Ui.Libraries.PhotoSlider;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.OnBoarding;
import com.flyby_riders.Utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoSlider {
    public Context context;
    public ArrayList<String> mData;
    int TotalCount=0;
    int position=0 ;
    public PhotoSlider(Context context, ArrayList<String> data) {
        this.context = context;
        this.mData = data;
    }


    public void showSliderPopup(int i) {
        position = i;
        TotalCount=mData.size()-1;
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater li = LayoutInflater.from(context);
            View dialogView = li.inflate(R.layout.image_layout, null);
            dialogBuilder.setView(dialogView);
            ImageView ImageView_d = dialogView.findViewById(R.id.ImageView_d);
            TextView viewPager  = dialogView.findViewById(R.id.viewPager);
            ImageView Close_Image_view = dialogView.findViewById(R.id.Close_Image_view);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            try {
                Picasso.get().load(mData.get(i).toString()).placeholder(R.drawable.images).into(ImageView_d);
            } catch (Exception e) {
                ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
            }
            Close_Image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.hide();
                }
            });
            viewPager.setText(String.valueOf(position+1)+" / "+String.valueOf(TotalCount+1));
            ImageView_d.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeTop() {

                }

                public void onSwipeRight() {
                    if (position!=0){
                        position--;
                        try {
                            Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position+1)+" / "+String.valueOf(TotalCount+1));
                    }else if (position==0){
                        try {
                            Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position+1)+" / "+String.valueOf(TotalCount+1));
                    }

                }

                public void onSwipeLeft() {
                    if (position<TotalCount){
                        position++;
                        try {
                            Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position)+" / "+String.valueOf(TotalCount+1));
                    }else if (position==TotalCount){
                        try {
                            Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position)+" / "+String.valueOf(TotalCount+1));
                    }

                }

                public void onSwipeBottom() {
                }

            });



            alertDialog.show();


        } catch (Exception e) {

        }
    }


}