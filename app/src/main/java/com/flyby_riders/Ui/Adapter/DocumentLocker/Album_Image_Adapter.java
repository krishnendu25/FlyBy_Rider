package com.flyby_riders.Ui.Adapter.DocumentLocker;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Album_Viewer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 04,July,2020
 */
public class Album_Image_Adapter extends RecyclerView.Adapter<Album_Image_Adapter.MyViewHolder>  {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<String> Album_Content_list ;
    int Measuredwidth=0;
    public Album_Image_Adapter(ArrayList<String> data, Context context,int Measuredwidth_g) {
        this.context=context;
        Album_Content_list = data;
        this.Measuredwidth = Measuredwidth_g;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public Album_Image_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_image_album, parent, false);
        return new Album_Image_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(Album_Image_Adapter.MyViewHolder holder,int i) {
        int Width = Measuredwidth ;
        FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(Width,Width);
        holder.Album_images_tv.setLayoutParams(parms);

        try{
            Picasso.get().load(Album_Content_list.get(i)).placeholder(R.drawable.images).into(holder.Album_images_tv);
        }catch (Exception e)
        {
            holder.Album_images_tv.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }

        holder.Album_images_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater li = LayoutInflater.from(context);
                    View dialogView = li.inflate(R.layout.image_layout, null);
                    dialogBuilder.setView(dialogView);
                    ImageView ImageView_d = dialogView.findViewById(R.id.ImageView_d);
                    ImageView Close_Image_view = dialogView.findViewById(R.id.Close_Image_view);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    try{
                        Picasso.get().load(Album_Content_list.get(i)).placeholder(R.drawable.images).into(ImageView_d);
                    }catch (Exception e)
                    {
                       ImageView_d.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
                    }
                    Close_Image_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.hide();
                        }
                    });
                    alertDialog.show();
                }catch (Exception e)
                {Constant.Show_Tos(context,"Something Wrong."); }

            }
        });
    }
    @Override
    public int getItemCount() {
        return Album_Content_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Album_images_tv;LinearLayout container;
        public MyViewHolder(View itemView) {
            super(itemView);
            Album_images_tv = (ImageView) itemView.findViewById(R.id.Album_images_tv);
        }
    }
}