package com.flyby_riders.Ui.Adapter.Garage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Bike_Model_Activity;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.BIKE_BRAND;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KRISHNENDU MANNA on 29,May,2020
 */

public class Bike_Model_Adapter extends BaseAdapter {
    ArrayList<BIKE_BRAND> data;
    Context context;
    onClick onClick;
    private static LayoutInflater inflater=null;
    public Bike_Model_Adapter(Context context, ArrayList<BIKE_BRAND> data) {
        // TODO Auto-generated constructor stub
        onClick = (Bike_Model_Activity) context;
        this.data=data;
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView brand_name;
        ImageView brand_pic;
        LinearLayout brand_clickview;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_bike_model, null);
        holder.brand_pic=rowView.findViewById(R.id.brand_pic);
        holder.brand_name=rowView.findViewById(R.id.brand_name);
        holder.brand_clickview=rowView.findViewById(R.id.brand_clickview);
        holder.brand_name.setSelected(true);

        holder.brand_name.setText(data.get(position).getNAME());

        holder.brand_clickview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(data.get(position).getID());
            }
        });
        try{
            Picasso.get()
                    .load(data.get(position).getPIC()).placeholder(R.drawable.ic_emptybike)
                    .into(holder.brand_pic);
        }catch (Exception e)
        {holder.brand_pic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_emptybike));
        }
        return rowView;
    }

    public  ArrayList<BIKE_BRAND> get_selected_item()
    {
        return data;
    }



}