package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.Bike_Model_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.BIKE_BRAND;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bike_Model_Activity extends BaseActivity implements onClick {

    String BIKE_ID, BIKE_BRAND;
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.bike_brand_list)
    GridView bikeBrandList;
    ArrayList<BIKE_BRAND> BIKE_LIST_MODEL = new ArrayList<>();
    Bike_Model_Adapter bike_brand_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike__model_);
        ButterKnife.bind(this);
        Instantiation();


    }

    private void Instantiation() {
        try {
            BIKE_ID = getIntent().getStringExtra("ID");
            BIKE_BRAND = getIntent().getStringExtra("NAME");
        } catch (Exception e) {
        }
        ACTIVITYTITEL.setText(BIKE_BRAND);
        hit_fetch_bike_model(BIKE_ID);

    }

    private void hit_fetch_bike_model(String bike_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Fetchbikemodel(bike_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hide_ProgressDialog();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                        BIKE_LIST_MODEL.clear();
                        String Base_Pic_url = jsonObject.getString("IMAGEPATH");
                        JSONArray jsonArray_ALLMODEL = jsonObject.getJSONArray("ALLMODEL");
                        for (int i = 0; i < jsonArray_ALLMODEL.length(); i++) {
                            BIKE_BRAND bike_BRAND = new BIKE_BRAND();
                            bike_BRAND.setID(jsonArray_ALLMODEL.getJSONObject(i).getString("ID").toString());
                            bike_BRAND.setNAME(jsonArray_ALLMODEL.getJSONObject(i).getString("NAME").toString());
                            bike_BRAND.setPIC(Base_Pic_url + jsonArray_ALLMODEL.getJSONObject(i).getString("PIC").toString());
                            BIKE_LIST_MODEL.add(bike_BRAND);
                        }
                        bike_brand_adapter = new Bike_Model_Adapter(Bike_Model_Activity.this,BIKE_LIST_MODEL);
                        bikeBrandList.setAdapter(bike_brand_adapter);

                    } else {
                        hide_ProgressDialog();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    hide_ProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });

    }
    private void hit_Add_Bike_Profile(String bike_model_id) {

        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.Add_Bike_To_Profile(new Session(this).get_LOGIN_USER_ID(),
                bike_model_id,BIKE_ID);

        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hide_ProgressDialog();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(Bike_Model_Activity.this, DashBoard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Constant.Show_Tos(getApplicationContext(),jsonObject.getString("msg"));
                        hide_ProgressDialog();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    hide_ProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
            }
        });


    }
    @OnClick(R.id.Back_Btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onClick(String Value) {
        hit_Add_Bike_Profile(Value);
    }
    @Override
    public void onLongClick(String Value) {

    }
}
