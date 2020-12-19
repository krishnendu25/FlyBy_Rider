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
import com.flyby_riders.Ui.Libraries.PhotoSlider.PhotoSlider;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 04,October,2020
 */
public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {
    private Context context;
    ArrayList<String> images;
    Activity activity;
    PhotoSlider photoSlider;
    public SliderAdapterExample(PhotoSlider photoSlider,Context context, ArrayList<String> images, Activity activity) {
        this.context = context;
        this.images = images;
        this.activity = activity;
        this.photoSlider=photoSlider;
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
                photoSlider.showSliderPopup(position);
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



}