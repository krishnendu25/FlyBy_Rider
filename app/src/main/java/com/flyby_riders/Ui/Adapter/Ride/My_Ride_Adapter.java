package com.flyby_riders.Ui.Adapter.Ride;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Create_Group_Ride;
import com.flyby_riders.Ui.Model.My_Ride_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_ENDED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_NOT_STARTED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_STARTED;


/**
 * Created by KRISHNENDU MANNA on 12,July,2020
 */
public class My_Ride_Adapter extends RecyclerView.Adapter<My_Ride_Adapter.MyViewHolder>  {
    ArrayList<My_Ride_Model> data;
    Context context;
    private static LayoutInflater inflater=null;

    public My_Ride_Adapter(Context contecxt,ArrayList<My_Ride_Model> MyRide_List ) {
        // TODO Auto-generated constructor stub
        this.context=contecxt;
        this.data = MyRide_List;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public My_Ride_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_my_ride_view, parent, false);
        return new My_Ride_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(My_Ride_Adapter.MyViewHolder holder, int position) {

        holder.Ride_Title_tv.setSelected(true);
        holder.Ride_Start_Date.setSelected(true);
        holder.Ride_status.setSelected(true);
        holder.Total_distance_tv.setSelected(true);
        holder.Total_time_tv.setSelected(true);
        holder.Total_Member_tv.setSelected(true);
        holder.total_media_tv.setSelected(true);



        holder.my_ride_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Create_Group_Ride.class);
                intent.putExtra("My_Ride_Name",data.get(position).getRide_Name());
                intent.putExtra("My_Ride_ID",data.get(position).getRide_ID());
                intent.putExtra("Admin_User_Id",data.get(position).getRide_Admin_Id());

                if (data.get(position).getRide_Status().equalsIgnoreCase("NOT STARTED"))
                { intent.putExtra("TRACKSTATUS",RIDE_NOT_STARTED);
                 }else if (data.get(position).getRide_Status().equalsIgnoreCase("STARTED"))
                { intent.putExtra("TRACKSTATUS",RIDE_STARTED);
                }else if (data.get(position).getRide_Status().equalsIgnoreCase("END"))
                { intent.putExtra("TRACKSTATUS",RIDE_ENDED);
                }
                intent.putExtra("STARTLAT",data.get(position).getSTARTLAT());
                intent.putExtra("STARTLANG",data.get(position).getSTARTLANG());
                intent.putExtra("ENDLAT",data.get(position).getENDLAT());
                intent.putExtra("ENDLANG",data.get(position).getENDLANG());
                context.startActivity(intent);
            }
        });
        try{
            Picasso.get().load(data.get(position).getRide_Cover_pic()).placeholder(R.drawable.images).into(holder.Ride_Image);
        }catch (Exception e)
        {
            holder.Ride_Image.setImageDrawable(context.getResources().getDrawable(R.drawable.images));
        }

        holder.Ride_Title_tv.setText(data.get(position).getRide_Name());
        holder.Ride_Start_Date.setText(Constant.Get_back_date_and_time(data.get(position).getRide_Start_Date()));
        holder.Ride_status.setText(data.get(position).getRide_Status());
        holder.Total_distance_tv.setText(data.get(position).getRide_Total_Distance());
        holder.Total_time_tv.setText(data.get(position).getRide_Total_Time());
        holder.Total_Member_tv.setText(data.get(position).getRide_Total_Member()+" members");
        holder.total_media_tv.setText(data.get(position).getTotal_media()+" media files");



    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout my_ride_view;
        ImageView Ride_Image;
        TextView Ride_Title_tv;
        TextView Ride_Start_Date;
        TextView Ride_status;
        TextView Total_distance_tv;
        TextView Total_time_tv;
        TextView Total_Member_tv;
        TextView total_media_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            my_ride_view = (LinearLayout) itemView.findViewById(R.id.my_ride_view);
            Ride_Image = (ImageView) itemView.findViewById(R.id.Ride_Image);
            Ride_Title_tv = (TextView) itemView.findViewById(R.id.Ride_Title_tv);
            Ride_Start_Date = (TextView) itemView.findViewById(R.id.Ride_Start_Date);
            Ride_status = (TextView) itemView.findViewById(R.id.Ride_status);
            Total_distance_tv = (TextView) itemView.findViewById(R.id.Total_distance_tv);
            Total_time_tv = (TextView) itemView.findViewById(R.id.Total_time_tv);
            Total_Member_tv = (TextView) itemView.findViewById(R.id.Total_Member_tv);
            total_media_tv = (TextView) itemView.findViewById(R.id.total_media_tv);
        }
    }
}