package com.flyby_riders.Ui.Libraries.PhotoSlider;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyby_riders.Constants.Constant;
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
            ImageView downloadView = dialogView.findViewById(R.id.downloadView);
            ImageView Close_Image_view = dialogView.findViewById(R.id.Close_Image_view);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            try {
                Glide.with(context).load(mData.get(i).toString()).placeholder(R.drawable.images).into(ImageView_d);
               // Picasso.get().load(mData.get(i).toString()).placeholder(R.drawable.images).into(ImageView_d);
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
                            Glide.with(context).load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                         //   Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position+1)+" / "+String.valueOf(TotalCount+1));
                    }else if (position==0){
                        try {
                            Glide.with(context).load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                       //     Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
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
                            Glide.with(context).load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                          //  Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position)+" / "+String.valueOf(TotalCount+1));
                    }else if (position==TotalCount){
                        try {
                            Glide.with(context).load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                         //   Picasso.get().load(mData.get(position).toString()).placeholder(R.drawable.images).into(ImageView_d);
                        } catch (Exception e) {
                            ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                        }
                        viewPager.setText(String.valueOf(position)+" / "+String.valueOf(TotalCount+1));
                    }

                }

                public void onSwipeBottom() {
                }

            });


            downloadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadViewImages(position);
                }
            });




            alertDialog.show();


        } catch (Exception e) {

        }
    }

    private void downloadViewImages(int position) {
        String Url = mData.get(position).toString();
        try {
            DownloadBooks(Url, String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void DownloadBooks(String url,String title){
        try {
            Constant.Show_Tos(context,title+".jpeg download completed.");
            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
            String tempTitle=title;
            request.setTitle(tempTitle);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,tempTitle+".jpeg");
            DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            request.setMimeType("image/jpeg");
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}