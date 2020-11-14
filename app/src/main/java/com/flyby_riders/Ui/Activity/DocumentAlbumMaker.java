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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Adapter.DocumentLocker.Doucment_Privew_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Album_Content_Model;
import com.flyby_riders.Ui.Model.Media_Model;
import com.flyby_riders.Ui.PhotoPicker.ImagePickerActivity;
import com.flyby_riders.Utils.BaseActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class DocumentAlbumMaker extends BaseActivity implements onClick {
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<Media_Model> media_models = new ArrayList<>();
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.Bike_Document_tv)
    TextView BikeDocumentTv;
    @BindView(R.id.Album_name_ed)
    EditText AlbumNameEd;
    @BindView(R.id.My_Photo_list)
    RecyclerView MyPhotoList;
    @BindView(R.id.save_my_album)
    Button saveMyAlbum;
    Doucment_Privew_Adapter doucment_privew_adapter;
    String Select_My_Bike_ID = "";
    @BindView(R.id.Error_txt)
    TextView ErrorTxt;
    @BindView(R.id.Update_File_Btn)
    TextView UpdateFileBtn;
    String Update_Album_Id="";
    ArrayList<Album_Content_Model> Album_Content_list = new ArrayList<>();
    ArrayList<String> Photo_Link = new ArrayList<>();
    int Position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document__album__creator);
        ButterKnife.bind(this);
        try{
            Position = Integer.valueOf(getIntent().getStringExtra("position"));
            Album_Content_list = getIntent().getParcelableArrayListExtra("list");
            Photo_Link.clear();
            Photo_Link = Album_Content_list.get(Position).getBIKEIDocMAGES();
            if (Photo_Link.size()>0)
            {UpdateFileBtn.setVisibility(View.VISIBLE);}
            Update_Album_Id = Album_Content_list.get(Position).getALBUM_ID();
            AlbumNameEd.setText( Album_Content_list.get(Position).getALBUM_NAME());
            Get_Api_Photo_Process(Photo_Link);

        }catch (Exception e)
        {
            if (media_models.size() < 1) {
                getImages();
            }
            UpdateFileBtn.setVisibility(View.GONE);
        }


        Instantiation();
        AlbumNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AlbumNameEd.setBackground(getResources().getDrawable(R.drawable.document_edittext));
                ErrorTxt.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void Instantiation() {

        ACTIVITYTITEL.setText("ADD DOCUMENT");
        Select_My_Bike_ID = getIntent().getStringExtra("Select_My_Bike_ID");
        MyPhotoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void getImages() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    235);
        } else {

            Intent intent = new Intent(this, ImagePickerActivity.class);
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
                            Constant.get_random_String(), bitmap, DocumentAlbumMaker.this));
                    media_models.add(mm);
                } catch (Exception e) {
                    Constant.Show_Tos_Error(getApplicationContext(),false,true);
                }
            }
            doucment_privew_adapter = new Doucment_Privew_Adapter(media_models, this);
            MyPhotoList.setAdapter(doucment_privew_adapter);


            //do something
        }
    }

    @OnClick({R.id.Back_Btn, R.id.save_my_album,R.id.Update_File_Btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.save_my_album:
                if (AlbumNameEd.getText().toString().isEmpty()) {
                    AlbumNameEd.setBackground(getResources().getDrawable(R.drawable.edittext_bg_red));
                    ErrorTxt.setText("Enter A Album Name");
                } else if (media_models.size() == 0) {
                    Constant.Show_Tos(this, "Select An Image");
                } else {
                    if (Update_Album_Id!=null)
                    {
                        if (Update_Album_Id.equalsIgnoreCase(""))
                        {
                            uploadMultiFile();
                        }else
                        {
                            uploadMultiFile(Update_Album_Id);
                        }
                    }

                }
                break;
            case  R.id.Update_File_Btn:
                getImages();
                break;
        }
    }


    @Override
    public void onClick(String Value) {
        if (doucment_privew_adapter != null) {
            int Pos = Integer.parseInt(Value);
            media_models.remove(Pos);
            doucment_privew_adapter.notifyDataSetChanged();
            if (media_models.size() == 0) {
                Intent intent = new Intent(this, ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        }

    }

    @Override
    public void onLongClick(String Value) {

    }

    private void uploadMultiFile() {
        show_ProgressDialog();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("userid", new Prefe(this).getUserID());
        builder.addFormDataPart("my_bike_id", Select_My_Bike_ID);
        builder.addFormDataPart("album_name", AlbumNameEd.getText().toString().trim());
        for (int i = 0; i < media_models.size(); i++) {
            File file = new File(media_models.get(i).getFile_Name());
            builder.addFormDataPart("album_file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        MultipartBody requestBody = builder.build();
        Call<ResponseBody> call = retrofitCallback.uploadMultiFile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        String output = Html.fromHtml(response.body().string()).toString();
                        output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                        jsonObject = new JSONObject(output);

                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(), "Album Creation Complete");
                            finish();

                        } else {
                            Constant.Show_Tos(getApplicationContext(), "Album Creation Failed");
                        }


                    } catch (IOException | JSONException e) {
                        Constant.Show_Tos_Error(getApplicationContext(),false,true);
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(),true,false);
            }
        });


    }
    private void uploadMultiFile(String Update_Album_Id) {
        show_ProgressDialog();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("userid", new Prefe(this).getUserID());
        builder.addFormDataPart("album_id", Update_Album_Id);
        builder.addFormDataPart("album_name", AlbumNameEd.getText().toString().trim());
        for (int i = 0; i < media_models.size(); i++) {
            File file = new File(media_models.get(i).getFile_Name());
            builder.addFormDataPart("album_file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        MultipartBody requestBody = builder.build();
        Call<ResponseBody> call = retrofitCallback.updateuploadMultiFile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        String output = Html.fromHtml(response.body().string()).toString();
                        output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                        jsonObject = new JSONObject(output);

                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(), "Album updated successfully");
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Constant.Show_Tos(getApplicationContext(), "Album updated Failed");
                        }


                    } catch (IOException | JSONException e) {
                        Constant.Show_Tos_Error(getApplicationContext(),false,true);
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(),true,false);
            }
        });


    }
    private void Get_Api_Photo_Process(ArrayList<String> photo_link) {
        ArrayList<String> file_name = new ArrayList<>();
        ArrayList<Bitmap> file_name_bimap = new ArrayList<>();
        for (int j=0;j<photo_link.size();j++)
        {
            Bitmap v =  getBitmapFromURL(photo_link.get(j));
            file_name_bimap.add(v);
            file_name.add(Constant.SaveImagetoSDcard(Constant.get_random_String(),
                    Constant.get_random_String(),v, DocumentAlbumMaker.this));
        }
        for (int i = 0; i < file_name.size(); i++) {
            try {
                Media_Model mm = new Media_Model();
                mm.setBitmap(file_name_bimap.get(i));
                mm.setFile_Name(file_name.get(i));
                media_models.add(mm);
            } catch (Exception e) {
                Constant.Show_Tos_Error(getApplicationContext(),false,true);
            }
        }
        doucment_privew_adapter = new Doucment_Privew_Adapter(media_models, this);
        MyPhotoList.setAdapter(doucment_privew_adapter);
        if (media_models.size() < 1) {
            getImages();
        }
    }



    public  Bitmap getBitmapFromURL(String src) {
        try {
             Bitmap[] bitmap_ = {null};
            Picasso.get().load(src).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmap_[0] = bitmap;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    bitmap_[0] =null;
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            });

           return bitmap_[0];

        } catch (Exception e) {
            Constant.Show_Tos_Error(getApplicationContext(),false,true);
            return null;
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

}