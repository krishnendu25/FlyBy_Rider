package com.flyby_riders.Ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Album_Viewer;
import com.flyby_riders.Ui.Activity.Document_Album_Creator;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Album_Content_Model;
import com.flyby_riders.Ui.Model.Media_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 03,July,2020
 */
public class My_Album_View extends RecyclerView.Adapter<My_Album_View.MyViewHolder>  {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Album_Content_Model> Album_Content_list ;
    public My_Album_View(ArrayList<Album_Content_Model> data, Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        Album_Content_list = data;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public My_Album_View.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_album, parent, false);
        return new My_Album_View.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(My_Album_View.MyViewHolder holder,int i) {
        holder.Album_name_tv.setText(Album_Content_list.get(i).getALBUM_NAME());
        holder.Album_images_tv.setText(String.valueOf(Album_Content_list.get(i).getBIKEIDocMAGES().size())+" images");
        try{
            Picasso.get().load(Album_Content_list.get(i).getBIKEIDocMAGES().get(i)).placeholder(R.drawable.images)
                    .into(holder.Album_cover_picture);
        }catch (Exception e)
        {
            holder.Album_cover_picture.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }

        holder.Open_Album_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, Album_Viewer.class);
                in.putExtra("list",Album_Content_list);
                in.putExtra("position",String.valueOf(i));
                context.startActivity(in);

            }
        });


    }
    @Override
    public int getItemCount() {
        return Album_Content_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout Open_Album_card;
        ImageView Album_cover_picture;
        TextView Album_name_tv;
        TextView Album_images_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            Open_Album_card = (RelativeLayout) itemView.findViewById(R.id.Open_Album_card);
            Album_cover_picture = (ImageView) itemView.findViewById(R.id.Album_cover_picture);
            Album_name_tv = (TextView) itemView.findViewById(R.id.Album_name_tv);
            Album_images_tv = (TextView) itemView.findViewById(R.id.Album_images_tv);
        }
    }
}