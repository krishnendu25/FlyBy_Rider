package com.flyby_riders.Ui.Adapter.Garage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Fragment.My_Garage_Fragment;
import com.flyby_riders.Ui.Model.Garage_Advertisement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Garage_Ad_Adapter extends RecyclerView.Adapter<Garage_Ad_Adapter.MyViewHolder> {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Garage_Advertisement> data;
    GarageAddClick garage_addClick;

    public Garage_Ad_Adapter(Context context, ArrayList<Garage_Advertisement> data_d, My_Garage_Fragment fragment) {
        this.context = context;
        data = data_d;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setClickListener(fragment);
    }

    @Override
    public Garage_Ad_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_ad_view, parent, false);
        return new Garage_Ad_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Garage_Ad_Adapter.MyViewHolder holder, int i) {
        holder.Add_title_tv.setSelected(true);
        holder.cityName.setText(data.get(i).getGarageOwnerDetails().get(0).getCity());
        holder.Add_title_tv.setText(Constant.wordFirstCap(data.get(i).getAdvertising_Title().toLowerCase()));
        if (data.get(i).getGarageOwnerDetails().size()>0)
        holder.vendor_name_tv.setText(Constant.wordFirstCap(data.get(i).getGarageOwnerDetails().get(0).getGarageName().toLowerCase()));

        if (data.get(i).getAdvertising_costPrice().equalsIgnoreCase("NA"))
            holder.price_add.setText("Not Disclosed");
        else if (data.get(i).getAdvertising_costPrice().equalsIgnoreCase("POR"))
            holder.price_add.setText("Price On Request");
        else
            holder.price_add.setText(context.getString(R.string.rupee)+" "+data.get(i).getAdvertising_costPrice());

        try{
            Picasso.get().load(data.get(i).getAdvertising_CoverPic()).placeholder(R.drawable.images).into(holder.Cover_pic_tv);
        }catch (Exception e)
        {
            holder.Cover_pic_tv.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }
        try{
            if (data.get(i).getGarageOwnerDetails().size()>0)
            Picasso.get().load("https://flybyapp.com/flybyapp/images/"+data.get(i).getGarageOwnerDetails().get(0).getProfilePicture()).placeholder(R.drawable.images).into(holder.venderPhoto);
        }catch (Exception e)
        {
            holder.venderPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }
        holder.Add_view_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              garage_addClick.setOnClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Add_view_click;
        ImageView Cover_pic_tv,venderPhoto;
        TextView Add_title_tv;
        TextView price_add;
        TextView vendor_name_tv,cityName;

        public MyViewHolder(View itemView) {
            super(itemView);
            venderPhoto= (ImageView) itemView.findViewById(R.id.venderPhoto);
            Add_view_click = (LinearLayout) itemView.findViewById(R.id.Add_view_click);
            Cover_pic_tv = (ImageView) itemView.findViewById(R.id.Cover_pic_tv);
            Add_title_tv = (TextView) itemView.findViewById(R.id.Add_title_tv);
            price_add = (TextView) itemView.findViewById(R.id.price_add);
            vendor_name_tv = (TextView) itemView.findViewById(R.id.vendor_name_tv);
            cityName = (TextView) itemView.findViewById(R.id.cityName);
        }
    }
    void setClickListener(GarageAddClick itemClickListener) {
        this.garage_addClick = itemClickListener;
    }
}