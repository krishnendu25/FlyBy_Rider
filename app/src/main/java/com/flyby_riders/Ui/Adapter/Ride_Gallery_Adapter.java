package com.flyby_riders.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Model.Ride_Media_Model;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Ride_Gallery_Adapter extends RecyclerView.Adapter<Ride_Gallery_Adapter.MyViewHolder>  {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Ride_Media_Model> data ;
    int Measuredwidth=0;
    public Ride_Gallery_Adapter( Context context,ArrayList<Ride_Media_Model> data_d,int Measuredwidth_g) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.Measuredwidth = Measuredwidth_g;
        data = data_d;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public Ride_Gallery_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chid_ride_gallery, parent, false);
        return new Ride_Gallery_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(Ride_Gallery_Adapter.MyViewHolder holder,int i) {
        int Width = Measuredwidth ;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(Width,Width);
        holder.imageview.setLayoutParams(parms);
        try{
            Picasso.get().load(data.get(i).getMEDIAFILE_URL()).placeholder(R.drawable.images)
                    .into(holder.imageview);
        }catch (Exception e)
        {
            holder.imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }
}