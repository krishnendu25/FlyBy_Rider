package com.flyby_riders.Ui.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.DocumentLocker.Album_Image_Adapter;
import com.flyby_riders.Ui.Model.Album_Content_Model;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Album_Viewer extends BaseActivity {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.option_menu)
    ImageView optionMenu;
    @BindView(R.id.My_Album_List)
    RecyclerView MyAlbumList;
    Album_Image_Adapter album_image_adapter;
    ArrayList<Album_Content_Model> Album_Content_list = new ArrayList<>();
    int Position;
    @BindView(R.id.Album_name_ed)
    TextView AlbumNameEd;
    ArrayList<String> Photo_Link = new ArrayList<>();
    int Measuredwidth = 0;
    int Measuredheight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__viewer);
        ButterKnife.bind(this);
        try {
            Position = Integer.valueOf(getIntent().getStringExtra("position"));
            Album_Content_list = getIntent().getParcelableArrayListExtra("list");
            Photo_Link.clear();
            Photo_Link = Album_Content_list.get(Position).getBIKEIDocMAGES();
        } catch (Exception e) {
        }
        Initialization();

    }

    private void Initialization() {
        Point size = new Point();
        WindowManager w = this.getWindowManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        AlbumNameEd.setText(Album_Content_list.get(Position).getALBUM_NAME());
        MyAlbumList.setLayoutManager(new GridLayoutManager(this, 2));
        album_image_adapter = new Album_Image_Adapter(Photo_Link,Album_Viewer.this,Measuredwidth/2);
        MyAlbumList.setAdapter(album_image_adapter);
    }

    @OnClick({R.id.Back_Btn, R.id.option_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.option_menu:
                PopupMenu popup = new PopupMenu(this,optionMenu);
                popup.inflate(R.menu.menu_album);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();

                        if (i == R.id.Edit_ablum) {
                            try{
                                Update_album(Album_Content_list,Position);
                            }catch (Exception e)
                            {  return true; }
                            return true;
                        } else if (i == R.id.Delete_album) {
                            try{
                          popup.dismiss();
                          Delete_Album(Album_Content_list.get(Position).getALBUM_ID());
                            }catch (Exception e)
                            {return true; }
                            return true;
                        } else {
                            return onMenuItemClick(item);
                        }
                    }
                });
                popup.show();

                break;
        }
    }

    private void Update_album(ArrayList<Album_Content_Model> album_content_list, int position)
    {
        Intent intent = new Intent(this,Document_Album_Creator.class);
        intent.putExtra("position",String.valueOf(position));
        intent.putExtra("list",album_content_list);
        startActivityForResult(intent,559);
    }

    private void Delete_Album(String album_id)
    {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Delete_Album(new Session(this).get_LOGIN_USER_ID(),album_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(),"Album Deleted Successfully");
                            finish();
                        } else {
                            Constant.Show_Tos(getApplicationContext(),"No data Exist...");
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 559) {
            if(resultCode == Activity.RESULT_OK){
               finish();
            }
        }
    }
}