package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.GlobalApplication;
import com.flyby_riders.NetworkOperation.IJSONParseListener;
import com.flyby_riders.R;
import com.flyby_riders.Utils.BaseActivity;
import com.flyby_riders.Utils.PayU_Module.AppEnvironment;
import com.flyby_riders.Utils.Prefe;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.Constant.hashCal;
import static com.flyby_riders.Constants.StringUtils.PREMIUM;

public class UpgradeAccountPlan extends BaseActivity implements IJSONParseListener {

    @BindView(R.id.Back_Btn)
    TextView BackBtn;
    @BindView(R.id.pay_amp_upgrade_tv)
    TextView payAmpUpgradeTv;
    private String PaymentAmounts = "999";
    private String orderID = "";
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_premium);
        ButterKnife.bind(this);
        initPaymentGateway();


    }

    private void initPaymentGateway() {

    }

    @OnClick({R.id.Back_Btn, R.id.pay_amp_upgrade_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.pay_amp_upgrade_tv:
                startPaymentProcess();
                break;
        }
    }

    private void startPaymentProcess() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setDoneButtonText("DONE");
        payUmoneyConfig.setPayUmoneyActivityTitle("FLYBY RIDER MEMBERSHIP");
        boolean isDisableExitConfirmation = false;
        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        double amount = 0;
        try {
            amount = Double.parseDouble(PaymentAmounts);

        } catch (Exception e) {
        }
        String txnId = String.valueOf(System.currentTimeMillis());
        String phone = mPrefe.getUserPhoneNO();
        String productName = "FLYBY";
        String firstName = mPrefe.getUserPhoneNO();
        String email = mPrefe.getUserPhoneNO()+ "@flyby.com";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";
        AppEnvironment appEnvironment = ((GlobalApplication) getApplication()).getAppEnvironment();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(false)
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());
        try {
            mPaymentParams = builder.build();
            generateHashFromServer();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void hit_Subscrption(String login_user_id, String s) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.buy_subcription_plan(new Prefe(this).getUserID(),
                "Premium Account", "FLYBY-Premium", "999", s,
                Constant.get_random_String(), Constant.get_random_String(), Constant.get_random_String());
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

                        }

                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            new Prefe(getApplicationContext()).setAccountPlanStatus(PREMIUM);
                            Constant.Show_Tos(getApplicationContext(), getString(R.string.premium_user));
                            finish();

                        } else {
                            Constant.Show_Tos(getApplicationContext(), "Unsuccessful Request");
                        }

                    } catch (Exception e) {

                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();
                Constant.Show_Tos_Error(getApplicationContext(), true, false);

            }
        });


    }



    public void generateHashFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = mPaymentParams.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
        AppEnvironment appEnvironment = ((GlobalApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());
        Log.e("postParamsBuffer ", stringBuilder.toString());
        String hash = hashCal(stringBuilder.toString());
        mPaymentParams.setMerchantHash(hash);
        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, UpgradeAccountPlan.this, R.style.AppTheme_Green, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    String payuResponse = transactionResponse.getPayuResponse();
                    try {
                        hide_ProgressDialog();
                        hit_Subscrption(new Prefe(getApplicationContext()).getUserID(), payuResponse);
                    } catch (Exception e) {
                        hit_Subscrption(new Prefe(getApplicationContext()).getUserID(), payuResponse);
                    }

                } else {
                    Constant.Show_Tos(this, "Failure Transaction");
                }
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("TAG", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("TAG", "Both objects are null!");
            }
        }
    }


    @Override
    public void SuccessResponse(JSONObject jsonObject, int requestCode) {
        /*if (requestCode==904){
            try {
                orderID = jsonObject.getString("id");
                openPaymentGateway(String.valueOf(PaymentAmounts*100));
            } catch (JSONException e) {
                hide_ProgressDialog();
            }
        }*/

    }

    @Override
    public void SuccessResponseArray(JSONArray response, int requestCode) {

    }

    @Override
    public void SuccessResponseRaw(String response, int requestCode) {
       /* if (requestCode==904){
            try { JSONObject jsonObject = new JSONObject(response);
                orderID = jsonObject.getString("id");
                openPaymentGateway(String.valueOf(PaymentAmounts*100));
            } catch (JSONException e) {
                hide_ProgressDialog();
            }
        }*/
    }
}