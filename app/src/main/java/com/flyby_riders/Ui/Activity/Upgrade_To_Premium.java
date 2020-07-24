package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Ui.Listener.StringUtils.PREMIUM;

public class Upgrade_To_Premium extends BaseActivity {

    @BindView(R.id.Back_Btn)
    TextView BackBtn;
    @BindView(R.id.pay_amp_upgrade_tv)
    TextView payAmpUpgradeTv;
    private String My_Ride_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_premium);
        ButterKnife.bind(this);
        try {
            My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
        } catch (Exception e) {
        }
    }

    @OnClick({R.id.Back_Btn, R.id.pay_amp_upgrade_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.pay_amp_upgrade_tv:
                hit_Subscrption(new Session(getApplicationContext()).get_LOGIN_USER_ID());
                break;
        }
    }

    private void hit_Subscrption(String login_user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.buy_subcription_plan(new Session(this).get_LOGIN_USER_ID(),
                "Premium Account","FLYBY-Premium","999","FLYBY",
                "XXXXXXX","XXXXXXX",My_Ride_ID);
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

                            new Session(getApplicationContext()).set_MEMBER_STATUS(PREMIUM);
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
                hide_ProgressDialog();
            }
        });


    }


}