package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Garage.Bike_Brand_Adapter;
import com.flyby_riders.Ui.Model.BIKE_BRAND;
import com.flyby_riders.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeBrandView extends BaseActivity {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.Search_bike)
    EditText SearchBike;
    @BindView(R.id.bike_brand_list)
    GridView bikeBrandList;
    ArrayList<BIKE_BRAND> BIKE_LIST = new ArrayList<>();
    Bike_Brand_Adapter bike_brand_adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike__brand);
        ButterKnife.bind(this);
        Instantiation();
        hit_GetAllBrand();

        SearchBike.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    private void Instantiation()
    {
        ACTIVITYTITEL.setText("Add your bike");
    }

    @OnClick(R.id.Back_Btn)
    public void onViewClicked() {
        finish();
    }
    private void hit_GetAllBrand() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.GetAllBrand();

        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful())
                {
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
                            BIKE_LIST.clear();
                            String Base_Pic_url = jsonObject.getString("IMAGEPATH");
                            JSONArray jsonArray_ALLMODEL = jsonObject.getJSONArray("ALLBRAND");
                            for (int i = 0; i < jsonArray_ALLMODEL.length(); i++) {
                                BIKE_BRAND bike_BRAND = new BIKE_BRAND();
                                bike_BRAND.setID(jsonArray_ALLMODEL.getJSONObject(i).getString("ID").toString());
                                bike_BRAND.setNAME(jsonArray_ALLMODEL.getJSONObject(i).getString("NAME").toString());
                                bike_BRAND.setPIC(Base_Pic_url + jsonArray_ALLMODEL.getJSONObject(i).getString("PIC").toString());
                                BIKE_LIST.add(bike_BRAND);
                            }
                            bike_brand_adapter = new Bike_Brand_Adapter(BikeBrandView.this,BIKE_LIST);
                            bikeBrandList.setAdapter(bike_brand_adapter);
                        } else {
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
                Constant.Show_Tos_Error(getApplicationContext(),true,false);
                hide_ProgressDialog();
            }
        });
    }
    private void filter(String toString) {

        ArrayList<BIKE_BRAND> BIKE_LIST_temp = new ArrayList();
        for(BIKE_BRAND d: BIKE_LIST){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getNAME().toLowerCase().contains(toString.toLowerCase())){
                BIKE_LIST_temp.add(d);
            }
        }
        //update recyclerview
        if (bike_brand_adapter!=null)
        bike_brand_adapter.Filter(BIKE_LIST_temp);

    }
}
