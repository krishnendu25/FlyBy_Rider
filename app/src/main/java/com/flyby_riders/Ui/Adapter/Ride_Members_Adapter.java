package com.flyby_riders.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Invite_new_member;
import com.flyby_riders.Ui.Activity.Ride_Members_Management;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;
import com.flyby_riders.Ui.Model.Ride_Member_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRISHNENDU MANNA on 11,July,2020
 */

public class Ride_Members_Adapter extends RecyclerView.Adapter<Ride_Members_Adapter.MyViewHolder>  {
    ArrayList<Ride_Member_model> data;
    Context context;
    private static LayoutInflater inflater=null;
    private String Short_Name;
    onClick onclick;
    public Ride_Members_Adapter(Context context,ArrayList<Ride_Member_model> data) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        onclick = (Ride_Members_Management)context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public Ride_Members_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_view, parent, false);
        return new Ride_Members_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(Ride_Members_Adapter.MyViewHolder holder, int position) {

        holder.Member_name_tv.setText(data.get(position).getFULL_NAME());

        holder.verified_invite.setVisibility(View.VISIBLE);

        if (data.get(position).getFULL_NAME().equalsIgnoreCase("ADMIN"))
        {  holder.option_menu.setVisibility(View.GONE); }else
        {holder.option_menu.setVisibility(View.VISIBLE); }

        if (data.get(position).getFULL_NAME().length()>2)
        { Short_Name = data.get(position).getFULL_NAME().substring(0,2);
        }else if (data.get(position).getFULL_NAME().length()>1)
        {Short_Name =data.get(position).getFULL_NAME(); }else
        {Short_Name ="--";}
        holder.member_name_shot_tv.setText(Short_Name);
        holder.option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,holder.option_menu);
                popup.inflate(R.menu.menu_remove_member);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();

                        if (i == R.id.remove_member) {
                            onclick.onClick(String.valueOf(position));
                            return true;
                        }  else {
                            return onMenuItemClick(item);
                        }
                    }
                });
                popup.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView member_name_shot_tv;
        TextView Member_name_tv;
        ImageView option_menu,verified_invite;
        LinearLayout CLICK_CARD;
        public MyViewHolder(View itemView) {
            super(itemView);
            CLICK_CARD =  (LinearLayout) itemView.findViewById(R.id.CLICK_CARD);
            member_name_shot_tv = (TextView) itemView.findViewById(R.id.member_name_shot_tv);
            Member_name_tv = (TextView) itemView.findViewById(R.id.Member_name_tv);
            option_menu = (ImageView) itemView.findViewById(R.id.option_menu);
            verified_invite = (ImageView) itemView.findViewById(R.id.option_menu);
        }
    }
}