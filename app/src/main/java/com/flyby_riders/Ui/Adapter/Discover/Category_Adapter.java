package com.flyby_riders.Ui.Adapter.Discover;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Fragment.Discover_Fragment;
import com.flyby_riders.Ui.Model.Category_Model;

import java.util.ArrayList;

public class Category_Adapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<Category_Model> data;
    Context context;
    Catagoryonclick onclick;
    public Category_Adapter(Context context, ArrayList<Category_Model> data, Discover_Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setClickListener(fragment);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_discover, null);
        holder.discover_iv = rowView.findViewById(R.id.discover_iv);
        holder.discover_tv = rowView.findViewById(R.id.discover_tv);
        holder.child_discover_view = rowView.findViewById(R.id.child_discover_view);

        //Dynamic Width
        int iDisplayWidth = context.getResources().getDisplayMetrics().widthPixels ;
        int iImageWidth = iDisplayWidth/2;



        holder.discover_tv.getLayoutParams().width = iImageWidth;

        if (data.get(position).getName().equalsIgnoreCase("Servicing & Repair")) {
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_servicing));
            holder.discover_tv.setText("Servicing &\nRepair");
            holder.discover_tv.setTextColor(Color.parseColor("#BD8F71"));
        } else if (data.get(position).getName().equalsIgnoreCase("Gears & Accessories")) {
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gears));
            holder.discover_tv.setText("Gears &\nAccessories");
            holder.discover_tv.setTextColor(Color.parseColor("#59C6F1"));
        } else if (data.get(position).getName().equalsIgnoreCase("Bike Parts & Accessories")) {
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bikeparts));
            holder.discover_tv.setText("Bike Parts &\nAccessories");
            holder.discover_tv.setTextColor(Color.parseColor("#7DB474"));
        } else if (data.get(position).getName().equalsIgnoreCase("Bike Wash")) {
            holder.discover_tv.setText("Bike Wash");
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bikewash));
            holder.discover_tv.setTextColor(Color.parseColor("#B14848"));
        } else if (data.get(position).getName().equalsIgnoreCase("Tyres")) {
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tyres));
            holder.discover_tv.setText("Tyres");
            holder.discover_tv.setTextColor(Color.parseColor("#CD9A0C"));
        } else if (data.get(position).getName().equalsIgnoreCase("Body Work & Modifications")) {
            holder.discover_tv.setText("Body Work &\nModifications");
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_modifications));
            holder.discover_tv.setTextColor(Color.parseColor("#917FD1"));
        } else if (data.get(position).getName().equalsIgnoreCase("Towing")) {
            holder.discover_tv.setText("Towing");
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_towing));
            holder.discover_tv.setTextColor(Color.parseColor("#E8E6EC"));
        } else if (data.get(position).getName().equalsIgnoreCase("Bike Showroom")) {
            holder.discover_tv.setText("Bike\nShowroom");
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_showroom));
            holder.discover_tv.setTextColor(Color.parseColor("#61B89E"));
        } else if (data.get(position).getName().equalsIgnoreCase("Race Track")) {
            holder.discover_tv.setText("Race Track");
            holder.discover_tv.setTextColor(Color.parseColor("#5AABCB"));
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_racetrack));
        } else if (data.get(position).getName().equalsIgnoreCase("Racing Acadmey")) {
            holder.discover_tv.setText("Racing Acadmey");
            holder.discover_tv.setTextColor(Color.parseColor("#AD8D31"));
            holder.discover_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_raceacadmey));
        }

        holder.child_discover_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.SetOnClick(position);
            }
        });


        return rowView;
    }


    public class Holder {
        RelativeLayout child_discover_view;
        ImageView discover_iv;
        TextView discover_tv;
    }
    void setClickListener(Catagoryonclick onclick) {
        this.onclick = onclick;
    }

}