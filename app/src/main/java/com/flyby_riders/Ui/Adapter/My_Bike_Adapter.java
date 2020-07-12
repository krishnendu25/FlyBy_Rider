package com.flyby_riders.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Document_Locker;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 30,May,2020
 */
public class My_Bike_Adapter extends RecyclerView.Adapter<My_Bike_Adapter.MyViewHolder>  {
    ArrayList<My_Bike_Model> data;
    Context context;
   onClick onClick;
    private static LayoutInflater inflater=null;
    public My_Bike_Adapter(My_Garage_Fragment contextf, ArrayList<My_Bike_Model> data,Context context) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        onClick = (My_Garage_Fragment)contextf;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public My_Bike_Adapter(ArrayList<My_Bike_Model> data,Context context) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        onClick = (Document_Locker)context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_my_bike_view, parent, false);
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

        if (data.get(position).isSelect())
        {
            holder.background_select.setBackground(null);
            holder.background_select.setBackground(context.getResources().getDrawable(R.drawable.bike_selected));
            holder.select_stamp.setVisibility(View.VISIBLE);
        }else
        {
            holder.background_select.setBackground(null);
            holder.background_select.setBackground(context.getResources().getDrawable(R.drawable.bike_unselected));
            holder.select_stamp.setVisibility(View.GONE);
        }

        holder.bike_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0 ;i<data.size();i++)
                {
                    data.get(i).setSelect(false);
                }
                data.get(position).setSelect(true);
                onClick.onClick(String.valueOf(position));
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    //View Holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout bike_view;
        ImageView background_select,bike_ic,select_stamp;


        public MyViewHolder(View itemView) {
            super(itemView);
            bike_view = itemView.findViewById(R.id.bike_view);
            background_select = itemView.findViewById(R.id.background_select);
            bike_ic = itemView.findViewById(R.id.bike_ic);
            select_stamp = itemView.findViewById(R.id.select_stamp);

        }
    }



}