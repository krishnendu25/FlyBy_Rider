package com.flyby_riders.Ui.Adapter.Garage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;
import ozaydin.serkan.com.image_zoom_view.SaveFileListener;

/**
 * Created by KRISHNENDU MANNA on 04,October,2020
 */
public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {
    private Context context;
    ArrayList<String> images;
    Activity activity;

    public SliderAdapterExample(Context context, ArrayList<String> images, Activity activity) {
        this.context = context;
        this.images = images;
        this.activity = activity;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        viewHolder.video_play_ic.setVisibility(View.GONE);
        try {
            Picasso.get().load(images.get(position)).placeholder(R.drawable.ic_placeholderad).into(viewHolder.roundedImageView);
        } catch (Exception e) {
            viewHolder.roundedImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }

        viewHolder.roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgTools(position);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView roundedImageView, video_play_ic;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.roundedImageView);
            video_play_ic = itemView.findViewById(R.id.video_play_ic);
        }
    }

    private void imgTools(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.image_layout, null);
        dialogBuilder.setView(dialogView);
        ImageViewZoom ImageView_d = dialogView.findViewById(R.id.ImageView_d);
        ImageViewZoomConfig imageViewZoomConfig =new ImageViewZoomConfig.Builder().saveProperty(true).saveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog).build();
        ImageView_d.setConfig(imageViewZoomConfig);
        ImageView Close_Image_view = dialogView.findViewById(R.id.Close_Image_view);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        try{
            Picasso.get().load(images.get(position)).placeholder(R.drawable.images).into(ImageView_d);
        }catch (Exception e)
        { ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }
        Close_Image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.hide();
            }
        });
        ImageView_d.saveImage(activity, "ImageViewZoom", "test", Bitmap.CompressFormat.JPEG, 1, imageViewZoomConfig,new SaveFileListener() {
            @Override
            public void onSuccess(File file) {

            }
            @Override
            public void onFail(Exception excepti) {
                Constant.Show_Tos(context,"Download Success");
            }
        });

        alertDialog.show();
        Constant.Show_Tos(context,"Pinch To Zoom");
    }

}