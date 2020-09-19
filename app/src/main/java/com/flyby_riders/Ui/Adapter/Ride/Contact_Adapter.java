package com.flyby_riders.Ui.Adapter.Ride;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Model.Contact_Model;
import com.google.android.datatransport.runtime.BuildConfig;

import java.util.List;

/**
 * Created by KRISHNENDU MANNA on 09,July,2020
 */

public class Contact_Adapter extends BaseAdapter {

    List<Contact_Model> data;
    Context context;
    private String Short_Name;

    public Contact_Adapter(Context context, List<Contact_Model> data) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int id) {
        return (long) id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.contactmember_view, parent, false);
            holder = new ViewHolder();

            holder.CLICK_CARD = (LinearLayout) convertView.findViewById(R.id.CLICK_CARD);
            holder.member_name_shot_tv = (TextView) convertView.findViewById(R.id.member_name_shot_tv);
            holder.Member_name_tv = (TextView) convertView.findViewById(R.id.Member_name_tv);
            holder.option_menu = (ImageView) convertView.findViewById(R.id.option_menu);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.Member_name_tv.setText(data.get(position).getContact_Name());
        if (data.get(position).getContact_Name().length() > 2) {
            Short_Name = data.get(position).getContact_Name().substring(0, 2);
        } else if (data.get(position).getContact_Name().length() > 1) {
            Short_Name = data.get(position).getContact_Name();
        } else {
            Short_Name = "--";
        }
        holder.member_name_shot_tv.setText(Short_Name);
        holder.option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FlyBy");
                    String shareMessage= "\nCheck out FlyBy,I use it to mannage my ride.Get it for free at www.flyby.com\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception d) {
                }
            }
        });


        return convertView;
    }

    public void setFilter(List<Contact_Model> filter) {
        this.data = filter;
        notifyDataSetChanged();

    }

    private static class ViewHolder {
        TextView member_name_shot_tv;
        TextView Member_name_tv;
        ImageView option_menu;
        LinearLayout CLICK_CARD;
    }
}
