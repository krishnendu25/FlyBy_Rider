package com.flyby_riders.Ui.Adapter.Discover;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Garage_List;
import com.flyby_riders.Ui.Model.Garage_Owner_Model;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KRISHNENDU MANNA on 01,August,2020
 */
public class Garage_Owner_Adapter extends RecyclerView.Adapter<Garage_Owner_Adapter.MyViewHolder>  {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Garage_Owner_Model> Garage_list;
    Garageownerclick garageownerclick;
    public Garage_Owner_Adapter(ArrayList<Garage_Owner_Model> data, Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        Garage_list = data;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        garageownerclick = (Garage_List)context;
    }
    @Override
    public Garage_Owner_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_garage_layout, parent, false);
        return new Garage_Owner_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(Garage_Owner_Adapter.MyViewHolder holder,int i) {


        holder.Garage_NAME.setText(Garage_list.get(i).getOWNERNAME());
        holder.Garage_CITY.setText(Garage_list.get(i).getCITY());

        double Dis = Double.valueOf(Garage_list.get(i).getDISTANCE_FROM_ME())/1000;


        holder.Garage_distance_unit.setText(new DecimalFormat("##.##").format(Dis)+ "km");
        holder.Garage_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 9);
                }else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+Garage_list.get(i).getPHONE()));
                    context.startActivity(callIntent);
                }
            }
        });
        holder.Garage_Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWhatsApp(Garage_list.get(i).getPHONE(),"",context);
            }
        });
        holder.Garage_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                garageownerclick.SelectOnClick(i);
            }
        });
        try{
            Picasso.get().load(Garage_list.get(i).getPROFILEPIC()).placeholder(R.drawable.images).into(holder.Garage_ImageView);
        }catch (Exception e)
        {holder.Garage_ImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.images));}

        StringBuilder stringBuilder = new StringBuilder();
        for (int j=0 ; j<Garage_list.get(i).getCategory_models().size() ; j++)
        {
            if (j==Garage_list.get(i).getCategory_models().size()-1)
            {stringBuilder.append(Garage_list.get(i).getCategory_models().get(j).getName());
            }else
            {stringBuilder.append(Garage_list.get(i).getCategory_models().get(j).getName()+" | ");}
        }
        holder.Garage_Services.setText(stringBuilder.toString());
    }
    @Override
    public int getItemCount() {
        return Garage_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout Garage_View;
        TextView Garage_distance_unit;
        ImageView Garage_Call;
        ImageView Garage_Whatsapp;
        CircleImageView Garage_ImageView;
        TextView Garage_NAME;
        TextView Garage_CITY;
        TextView Garage_Services;
        public MyViewHolder(View itemView) {
            super(itemView);
            Garage_View = (RelativeLayout) itemView.findViewById(R.id.Garage_View);
            Garage_distance_unit = (TextView) itemView.findViewById(R.id.Garage_distance_unit);
            Garage_Call = (ImageView) itemView.findViewById(R.id.Garage_Call);
            Garage_Whatsapp = (ImageView) itemView.findViewById(R.id.Garage_Whatsapp);
            Garage_ImageView = (CircleImageView) itemView.findViewById(R.id.Garage_ImageView);
            Garage_NAME = (TextView) itemView.findViewById(R.id.Garage_NAME);
            Garage_CITY = (TextView) itemView.findViewById(R.id.Garage_CITY);
            Garage_Services = (TextView) itemView.findViewById(R.id.Garage_Services);
        }
    }


}