package com.flyby_riders.Ui.Adapter.DocumentLocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Activity.Document_Album_Creator;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Media_Model;
import java.util.ArrayList;
/**
 * Created by KRISHNENDU MANNA on 24,June,2020
 */
public class Doucment_Privew_Adapter extends RecyclerView.Adapter<Doucment_Privew_Adapter.MyViewHolder>  {
    ArrayList<Media_Model> data;
    Context context;
    private static LayoutInflater inflater=null;
    onClick onClick;
    public Doucment_Privew_Adapter(ArrayList<Media_Model> data, Context context) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        onClick = (Document_Album_Creator)context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public Doucment_Privew_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_add_document, parent, false);
        return new Doucment_Privew_Adapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(Doucment_Privew_Adapter.MyViewHolder holder,int position) {
      holder.Document_picture_iv.setImageBitmap(data.get(position).getBitmap());
      holder.Remove_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onClick.onClick(String.valueOf(position));
          }
      });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Document_picture_iv;
        ImageButton Remove_btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            Document_picture_iv = itemView.findViewById(R.id.Document_picture_iv);
            Remove_btn = itemView.findViewById(R.id.Remove_btn);
        }
    }
}