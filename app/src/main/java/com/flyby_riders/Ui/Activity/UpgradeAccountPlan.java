package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.flyby_riders.Constants.Constant;
import com.flyby_riders.NetworkOperation.IJSONParseListener;
import com.flyby_riders.NetworkOperation.JSONRequestResponse;
import com.flyby_riders.NetworkOperation.MyVolley;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Utils.BaseActivity;
import com.google.android.gms.maps.model.LatLng;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.StringUtils.PREMIUM;

public class UpgradeAccountPlan extends BaseActivity  implements IJSONParseListener, PaymentResultListener {

    @BindView(R.id.Back_Btn)
    TextView BackBtn;
    @BindView(R.id.pay_amp_upgrade_tv)
    TextView payAmpUpgradeTv;
    private int PaymentAmounts=999;
    private String orderID="";
    private Checkout checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_premium);
        ButterKnife.bind(this);
        initRazorpay();



    }

    private void initRazorpay() {
        Checkout.preload(getApplicationContext());
        checkout = new Checkout();
        checkout.setKeyID("rzp_test_VB1RdpKZUd9TfA");
        checkout.setImage(R.mipmap.ic_appiconrider);
    }
    @OnClick({R.id.Back_Btn, R.id.pay_amp_upgrade_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.pay_amp_upgrade_tv:
                hitcreateOrderID();

                break;
        }
    }

    private void hitcreateOrderID() {
        show_ProgressDialog();
        JSONObject orderBody= new JSONObject();
        String amount_razor="";
        try{
            amount_razor = String.valueOf(Integer.valueOf(PaymentAmounts) * 100);
            orderBody.put("amount",amount_razor);
            orderBody.put("currency","INR");
            orderBody.put("receipt","FLYBY"+mPrefe.getUserID());
        }catch (Exception e){
        }

        JSONRequestResponse mResponse = new JSONRequestResponse(this);
        Bundle parms = new Bundle();
        MyVolley.init(this);
        try {
            mResponse.getResponse(Request.Method.POST, "https://api.razorpay.com/v1/orders",
                    904, this, parms, false, true, orderBody);
        } catch (Exception e) {

        }
    }

    private void openPaymentGateway(String amount) {
        String txnId = Constant.get_random_String();
        String phone = mPrefe.getUserPhoneNO().toString().trim();
        String productName = "FLYBY RIDE MEMBERSHIP";
        String email = "flyby299kmph@gmail.com";
        try {
            /* orderId*/

            JSONObject options = new JSONObject();
            options.put("name", productName);
            options.put("description", "FLYBY RIDE MEMBERSHIP");
           // options.put("image", "https://firebasestorage.googleapis.com/v0/b/tropogoinsurance.appspot.com/o/razor_tglogo.png?alt=media&token=644185f8-bc4c-4256-ad58-ef1184048cf5");
            if (orderID != null) {
                if (!orderID.equalsIgnoreCase("")) {
                    options.put("order_id", orderID);
                }
            }
            options.put("currency", "INR");
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);
            options.put("prefill", preFill);
            checkout.open(this, options);
        } catch (Exception e) {
            Log.e(getLocalClassName(), "Error in starting Razorpay Checkout", e);
        }
    }

    private void hit_Subscrption(String login_user_id, String s) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.buy_subcription_plan(new Prefe(this).getUserID(),
                "Premium Account","FLYBY-Premium","999",s,
                Constant.get_random_String(),Constant.get_random_String(),Constant.get_random_String());
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

                            new Prefe(getApplicationContext()).setAccountPlanStatus(PREMIUM);
                            Constant.Show_Tos(getApplicationContext(), getString(R.string.premium_user));
                            finish();

                        } else {
                            Constant.Show_Tos(getApplicationContext(), "Unsuccessful Request");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });


    }


    @Override
    public void onPaymentSuccess(String s) {
        hit_Subscrption(new Prefe(getApplicationContext()).getUserID(),s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Constant.Show_Tos(getApplicationContext(), "Failure Transaction");
    }


    @Override
    public void SuccessResponse(JSONObject jsonObject, int requestCode) {
        if (requestCode==904){
            try {
                orderID = jsonObject.getString("id");
                openPaymentGateway(String.valueOf(PaymentAmounts*100));
            } catch (JSONException e) {
                hide_ProgressDialog();
            }
        }

    }

    @Override
    public void SuccessResponseArray(JSONArray response, int requestCode) {

    }

    @Override
    public void SuccessResponseRaw(String response, int requestCode) {

        if (requestCode==904){
            try { JSONObject jsonObject = new JSONObject(response);
                orderID = jsonObject.getString("id");
                openPaymentGateway(String.valueOf(PaymentAmounts*100));
            } catch (JSONException e) {
                hide_ProgressDialog();
            }
        }

    }
}