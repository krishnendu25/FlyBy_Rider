package com.flyby_riders.Ui.Adapter.Garage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.DocumentLockerView;
import com.flyby_riders.Ui.Activity.MyAccount;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 30,May,2020
 */
public class MyBikeMannageAdapter extends RecyclerView.Adapter<MyBikeMannageAdapter.MyViewHolder>  {
    ArrayList<My_Bike_Model> data;
    Context context;
    private static LayoutInflater inflater=null;
    DeleteMyBike deleteMyBike;
    public MyBikeMannageAdapter(ArrayList<My_Bike_Model> data, Context context) {
        this.data=data;
        this.context=context;
        deleteMyBike = (MyAccount)context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_mybikemannage, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try{
            Picasso.get().load(data.get(position).getMODELPIC()).placeholder(R.drawable.ic_emptybike).into(holder.bike_ic);
        }catch (Exception e)
        {
            holder.bike_ic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_emptybike));
        }
        holder.bikeName.setSelected(true);
        holder.bikeName.setText(data.get(position).getMODELNAME());

        holder.deleteMyBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyBike.onClickDeleteMyBike(data.get(position).getMY_BIKE_ID(),data.get(position).getMODELNAME());
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout bike_view;
        ImageView bike_ic,deleteMyBike;
        TextView bikeName;
        public MyViewHolder(View itemView) {
            super(itemView);
            bike_view = itemView.findViewById(R.id.bike_view);
            bikeName = itemView.findViewById(R.id.bikeName);
            bike_ic = itemView.findViewById(R.id.bike_ic);
            deleteMyBike = itemView.findViewById(R.id.deleteMyBike);

        }
    }
}