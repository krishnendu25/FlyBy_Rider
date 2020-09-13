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

import com.bumptech.glide.Glide;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Bike_Model_Activity;
import com.flyby_riders.Ui.Model.BIKE_BRAND;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Bike_Brand_Adapter extends BaseAdapter {
    ArrayList<BIKE_BRAND> data;
    Context context;
    private static LayoutInflater inflater=null;
    public Bike_Brand_Adapter(Context context, ArrayList<BIKE_BRAND> data) {
        // TODO Auto-generated constructor stub
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
        ImageView brand_pic,brand_checkbox;
        LinearLayout brand_clickview;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_brand, null);
        holder.brand_name=rowView.findViewById(R.id.brand_name);
        holder.brand_pic=rowView.findViewById(R.id.brand_pic);
        holder.brand_clickview = rowView.findViewById(R.id.brand_clickview);
        holder.brand_name.setSelected(true);
        holder.brand_name.setText(data.get(position).getNAME());
        holder.brand_clickview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Bike_Model_Activity.class);
                intent.putExtra("ID",data.get(position).getID());
                intent.putExtra("NAME",data.get(position).getNAME());
                context.startActivity(intent);
            }
        });
        try{
            Glide.with(context).load(data.get(position).getPIC()).placeholder(R.drawable.images).into(holder.brand_pic);

        }catch (Exception e)
        {}
       return rowView;
    }

    public  ArrayList<BIKE_BRAND> get_selected_item()
    {
        return data;
    }

public void Filter( ArrayList<BIKE_BRAND> filter)
{
    this.data=filter;
    notifyDataSetChanged();
}

}