package com.flyby_riders.Ui.Adapter.Discover;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.GarageDetailsView;
import com.flyby_riders.Ui.Libraries.PhotoSlider.PhotoSlider;
import com.flyby_riders.Ui.Model.Garage_Media_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Image_Silder_Adapter extends RecyclerView.Adapter<Image_Silder_Adapter.ViewHolder> {

    Context context;
    private ArrayList<Garage_Media_Model> mData;
    private LayoutInflater mInflater;
    Media_Slider_Click media_slider_click;
    PhotoSlider photoSlider;
    public Image_Silder_Adapter(PhotoSlider photoSlider, Context context, ArrayList<Garage_Media_Model> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.media_slider_click = (GarageDetailsView)context;
        this.photoSlider =photoSlider;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.child_image_slider, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (mData.get(position).getFile_Type().equalsIgnoreCase("VIDEO")) {
            holder.video_play_ic.setVisibility(View.VISIBLE);
            try{
                holder.roundedImageView.setBackground(null);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.isMemoryCacheable();
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(mData.get(position).getFile_Url()).placeholder(R.drawable.ic_placeholderad).into(holder.roundedImageView);
            }catch (Exception e){}
                  } else {
            try {
                holder.roundedImageView.setBackground(null);
                Picasso.get().load(mData.get(position).getFile_Url()).placeholder(R.drawable.ic_placeholderad).into(holder.roundedImageView);
            } catch (Exception e) {
                holder.roundedImageView.setBackground(null);
                Picasso.get().load(R.drawable.images).into(holder.roundedImageView);
            }
            holder.video_play_ic.setVisibility(View.GONE);
        }
        holder.video_layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in));

        holder.roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {photoSlider.showSliderPopup(position);}
        });
        holder.video_play_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_slider_click.MediaItemClick(position);
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    Garage_Media_Model getItem(int id) {
        return mData.get(id);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView roundedImageView, video_play_ic;
        RelativeLayout video_layout;
        ViewHolder(View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.roundedImageView);
            video_play_ic = itemView.findViewById(R.id.video_play_ic);
            video_layout= itemView.findViewById(R.id.video_layout);
        }


    }
}