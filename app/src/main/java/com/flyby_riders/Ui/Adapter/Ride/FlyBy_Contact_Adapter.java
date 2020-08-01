package com.flyby_riders.Ui.Adapter.Ride;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Invite_new_member;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;
import java.util.List;

/**
 * Created by KRISHNENDU MANNA on 09,July,2020
 */

public class FlyBy_Contact_Adapter extends BaseAdapter
{
    List<FlyBy_Contact_Model> data;
    Context context;
    private String Short_Name;
    onClick onclick;
    public FlyBy_Contact_Adapter(Context context, List<FlyBy_Contact_Model> data) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        onclick = (Invite_new_member)context;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.member_view, parent, false);
            holder = new ViewHolder();



            holder. CLICK_CARD =  (LinearLayout) convertView.findViewById(R.id.CLICK_CARD);
            holder. member_name_shot_tv = (TextView) convertView.findViewById(R.id.member_name_shot_tv);
            holder. Member_name_tv = (TextView) convertView.findViewById(R.id.Member_name_tv);
            holder. option_menu = (ImageView) convertView.findViewById(R.id.option_menu);
            convertView.setTag(holder);
        }else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.Member_name_tv.setText(data.get(position).getContact_Name());
        holder.Member_name_tv.setText(data.get(position).getContact_Name());
        holder.option_menu.setVisibility(View.GONE);

        if (data.get(position).getContact_Name().length()>2)
        { Short_Name = data.get(position).getContact_Name().substring(0,2);
        }else if (data.get(position).getContact_Name().length()>1)
        {Short_Name =data.get(position).getContact_Name(); }else
        {Short_Name ="--"; }

        holder.member_name_shot_tv.setText(Short_Name);
        holder.CLICK_CARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onClick(String.valueOf(position));
            }
        });


        return convertView;
    }

    public void setFilter(List<FlyBy_Contact_Model> filter)
    {
        this.data =filter;
        notifyDataSetChanged();

    }

    private static class ViewHolder {
        TextView member_name_shot_tv;
        TextView Member_name_tv;
        ImageView option_menu;
        LinearLayout CLICK_CARD;
    }
}
