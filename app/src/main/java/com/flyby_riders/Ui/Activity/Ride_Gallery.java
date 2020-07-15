package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.Bike_Ride_Media_Adapter;
import com.flyby_riders.Ui.Adapter.Doucment_Privew_Adapter;
import com.flyby_riders.Ui.Adapter.Ride_Members_Adapter;
import com.flyby_riders.Ui.Fragment.All_Media_Ride_Fragment;
import com.flyby_riders.Ui.Fragment.My_Media_Ride_Fragment;
import com.flyby_riders.Ui.Model.Media_Model;
import com.flyby_riders.Ui.Model.Ride_Media_Model;
import com.flyby_riders.Ui.Model.Ride_Member_model;
import com.flyby_riders.Ui.PhotoPicker.ImagePickerActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ride_Gallery extends BaseActivity {
    private static final int INTENT_REQUEST_GET_IMAGES = 58;
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.storage_info_tv)
    TextView storageInfoTv;
    @BindView(R.id.tabs)
    TabLayout viewPager_tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.add_media_to_ride)
    FloatingActionButton addMediaToRide;
    public static String My_Ride_ID = "", Admin_User_Id = "";
    Bike_Ride_Media_Adapter bike_ride_media_adapter;
    private int mSelectedTabPosition = 0;
    ArrayList<Media_Model> media_models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride__gallery);
        ButterKnife.bind(this);
        try {
            My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
            Admin_User_Id = getIntent().getStringExtra("Admin_User_Id");
        } catch (Exception e) {
        }
        Instantiation();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedTabPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void Instantiation() {
        bike_ride_media_adapter = new Bike_Ride_Media_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(bike_ride_media_adapter);

        if (bike_ride_media_adapter != null)
            bike_ride_media_adapter.notifyDataSetChanged();
        viewPager_tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        mSelectedTabPosition = 0;


    }

    @OnClick({R.id.Back_Btn, R.id.add_media_to_ride})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.add_media_to_ride:
                getImages();
                break;
        }
    }

    private void getImages() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    235);
        } else {
            Intent intent = new Intent(this, ImagePickerActivity.class);
            intent.putExtra("Class", getLocalClassName());
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            if (media_models.size() == 0) {
                media_models.clear();
            }
            for (int i = 0; i < image_uris.size(); i++) {
                try {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = getResizedBitmap(BitmapFactory.decodeFile(image_uris.get(i).getPath(), bmOptions));
                    Media_Model mm = new Media_Model();
                    mm.setBitmap(bitmap);
                    mm.setFile_Name(Constant.SaveImagetoSDcard(Constant.get_random_String(),
                            Constant.get_random_String(), bitmap, Ride_Gallery.this));
                    media_models.add(mm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hit_upload_media(media_models, My_Ride_ID);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 480;
            height = (int) (width / bitmapRatio);
        } else {
            height = 640;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void hit_upload_media(ArrayList<Media_Model> media_models, String my_ride_id) {
        show_ProgressDialog();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("Ride_id", my_ride_id);
        builder.addFormDataPart("Custom_Object", new Session(getApplicationContext()).get_LOGIN_USER_ID());
        for (int i = 0; i < media_models.size(); i++) {
            File file = new File(media_models.get(i).getFile_Name());
            builder.addFormDataPart("ride_file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        MultipartBody requestBody = builder.build();
        Call<ResponseBody> call = retrofitCallback.updateuploadupload_ride_media_file(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());

                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            Constant.Show_Tos(getApplicationContext(), "Media Upload successfully");
                            if (mSelectedTabPosition==0)
                            {
                                All_Media_Ride_Fragment ab=All_Media_Ride_Fragment.newInstance();
                                ab.Fetch_Media_Ride(My_Ride_ID);
                            }else if(mSelectedTabPosition==1)
                            { My_Media_Ride_Fragment mb= My_Media_Ride_Fragment.newInstance();
                                mb.Fetch_Media_Ride(My_Ride_ID);
                            }


                        } else {
                            Constant.Show_Tos(getApplicationContext(), "Media Upload Failed");
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos(getApplicationContext(), "Media Upload Failed");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}