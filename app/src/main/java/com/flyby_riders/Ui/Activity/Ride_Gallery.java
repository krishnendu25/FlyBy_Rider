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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Bike_Ride_Media_Adapter;
import com.flyby_riders.Ui.Adapter.Doucment_Privew_Adapter;
import com.flyby_riders.Ui.Model.Media_Model;
import com.flyby_riders.Ui.PhotoPicker.ImagePickerActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    String My_Ride_ID = "";
    Bike_Ride_Media_Adapter bike_ride_media_adapter;
    private int mSelectedTabPosition=0;
    ArrayList<Media_Model> media_models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride__gallery);
        ButterKnife.bind(this);
        try { My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
        } catch (Exception e) {
        }
        Instantiation();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mSelectedTabPosition=position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void Instantiation() {
        bike_ride_media_adapter = new Bike_Ride_Media_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(bike_ride_media_adapter);
        if (bike_ride_media_adapter!=null)
        bike_ride_media_adapter.notifyDataSetChanged();
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
            intent.putExtra("Class",getLocalClassName());
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            if (media_models.size()==0)
            {media_models.clear();}
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
            hit_upload_media(media_models,My_Ride_ID);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 480 ;
            height = (int) (width / bitmapRatio);
        } else {
            height = 640 ;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void hit_upload_media(ArrayList<Media_Model> media_models, String my_ride_id) {



    }
}