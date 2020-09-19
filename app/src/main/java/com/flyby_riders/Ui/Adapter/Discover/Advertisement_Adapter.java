package com.flyby_riders.Ui.Adapter.Discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Garage_Information;
import com.flyby_riders.Ui.Model.ADD_MODEL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Advertisement_Adapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<ADD_MODEL> data;
    Context mcontext;
    ADDClickListener itemClickListener;
    String ownername;
    public Advertisement_Adapter(Context context, ArrayList<ADD_MODEL> data, String ownername) {
        this.data = data;
        this.mcontext = context;
        this.ownername = ownername;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemClickListener = (Garage_Information) context;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_ad_view, null);
        holder.Add_view_click = (LinearLayout) rowView.findViewById(R.id.Add_view_click);
        holder.Cover_pic_tv = (ImageView) rowView.findViewById(R.id.Cover_pic_tv);
        holder.Add_title_tv = (TextView) rowView.findViewById(R.id.Add_title_tv);
        holder.price_add = (TextView) rowView.findViewById(R.id.price_add);
        holder.vendor_name_tv = (TextView) rowView.findViewById(R.id.vendor_name_tv);

        holder.Add_title_tv.setText(data.get(i).getTITLE());
        holder.vendor_name_tv.setText(ownername);
        holder.price_add.setText(mcontext.getString(R.string.rupee)+" "+data.get(i).getADCOSTPRICE());

        try{
            Picasso.get().load(data.get(0).getIMAGECOVERPATH()+data.get(0).getADCOVERIMAGE()).placeholder(R.drawable.images).into(holder.Cover_pic_tv);
        }catch (Exception e)
        {  }
        holder.Add_view_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.setOnAdapterClick(0);
            }
        });
        return rowView;
    }

    public class Holder {
        LinearLayout Add_view_click;
        ImageView Cover_pic_tv;
        TextView Add_title_tv;
        TextView price_add;
        TextView vendor_name_tv;
    }

}