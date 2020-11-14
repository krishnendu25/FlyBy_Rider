package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Utils.BaseActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.StringUtils.PREMIUM;

public class UpgradeAccountPlan extends BaseActivity {

    @BindView(R.id.Back_Btn)
    TextView BackBtn;
    @BindView(R.id.pay_amp_upgrade_tv)
    TextView payAmpUpgradeTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_premium);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.Back_Btn, R.id.pay_amp_upgrade_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.pay_amp_upgrade_tv:
                hit_Subscrption(new Prefe(getApplicationContext()).getUserID());
                break;
        }
    }

    private void hit_Subscrption(String login_user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.buy_subcription_plan(new Prefe(this).getUserID(),
                "Premium Account","FLYBY-Premium","999","FLYBY",
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


}