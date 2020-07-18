package com.flyby_riders.Ui.Adapter.Garage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.DashBoard;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Model.Garage_Ad;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Garage_Ad_Adapter extends RecyclerView.Adapter<Garage_Ad_Adapter.MyViewHolder> {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Garage_Ad> data;
    Garage_add_click garage_add_click;

    public Garage_Ad_Adapter(Context context, ArrayList<Garage_Ad> data_d) {
        // TODO Auto-generated constructor stub
        this.context = context;
        data = data_d;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        garage_add_click = (My_Garage_Fragment)garage_add_click;
    }

    @Override
    public Garage_Ad_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_ad_view, parent, false);
        return new Garage_Ad_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Garage_Ad_Adapter.MyViewHolder holder, int i) {

        holder.Add_title_tv.setSelected(true);
        holder.Add_title_tv.setText(data.get(i).getADTITLE());
        holder.vendor_name_tv.setText(data.get(i).getADVID());
        holder.price_add.setText(context.getString(R.string.rupee)+" "+data.get(i).getADCOSTPRICE());

        try{
            Picasso.get().load(data.get(i).getADCOVERIMAGES()).placeholder(R.drawable.images).into(holder.Cover_pic_tv);
        }catch (Exception e)
        {
            holder.Cover_pic_tv.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }
        holder.Add_view_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // garage_add_click.SetOnClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Add_view_click;
        ImageView Cover_pic_tv;
        TextView Add_title_tv;
        TextView price_add;
        TextView vendor_name_tv;

        public MyViewHolder(View itemView) {
            super(itemView);

            Add_view_click = (LinearLayout) itemView.findViewById(R.id.Add_view_click);
            Cover_pic_tv = (ImageView) itemView.findViewById(R.id.Cover_pic_tv);
            Add_title_tv = (TextView) itemView.findViewById(R.id.Add_title_tv);
            price_add = (TextView) itemView.findViewById(R.id.price_add);
            vendor_name_tv = (TextView) itemView.findViewById(R.id.vendor_name_tv);
        }
    }
}