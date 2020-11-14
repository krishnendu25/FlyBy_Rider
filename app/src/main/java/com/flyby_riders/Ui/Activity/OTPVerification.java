package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.NetworkOperation.IJSONParseListener;
import com.flyby_riders.R;
import com.flyby_riders.Utils.AppSignatureHashHelper;
import com.flyby_riders.Utils.MySMSBroadcastReceiver;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Utils.BaseActivity;
import com.flyby_riders.Utils.CommentKeyBoardFix;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.StringUtils.PHONE_NO;

public class OTPVerification extends BaseActivity implements MySMSBroadcastReceiver.OTPReceiveListener, IJSONParseListener {

    private static final String FORMAT_T = "%02d:%02d:%02d";
    @BindView(R.id.otp_ed)
    OtpTextView otpEd;
    @BindView(R.id.otp_ed_error)
    OtpTextView otpEdError;
    @BindView(R.id.edit_text_container)
    LinearLayout editTextContainer;
    @BindView(R.id.Error_tv)
    TextView ErrorTv;
    @BindView(R.id.show_otp_Mobile)
    TextView showOtpMobile;
    @BindView(R.id.edit_phone_no)
    TextView editPhoneNo;
    @BindView(R.id.Resend_btn)
    TextView ResendBtn;
    @BindView(R.id.otp_verify_btn)
    Button otpVerifyBtn;
    String Phone_Number, User_Id;
    String Device_Id="";
    CountDownTimer cTimer = null;
    private MySMSBroadcastReceiver smsReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p__verify);
        ButterKnife.bind(this);
        new CommentKeyBoardFix(this);
        Initialization();
        otpEdError.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ErrorTv.setText("");
                otpEdError.setVisibility(View.GONE);
                otpEd.setVisibility(View.VISIBLE);
                Constant.showKeyboard(otpEd, getApplicationContext());
                return true;
            }
        });
        otpEd.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                ErrorTv.setText("");
            }

            @Override
            public void onOTPComplete(String otp) {
                Verifotp(otp);
            }
        });
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        Log.i("HashKey", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        try{
            Constant.setClipboard(this,appSignatureHashHelper.getAppSignatures().get(0));
        }catch (Exception e)
        {

        }

    }

    private void Verifotp(String otp) {

        Call<ResponseBody> requestCall = retrofitCallback.validate_otp(Phone_Number,otp,Device_Id);
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
                            Constant.Show_Tos(getApplicationContext(),"Login Successful");
                            new Prefe(getApplicationContext()).setUserID(jsonObject.getString("uid"));
                            new Prefe(getApplicationContext()).setUserPhoneNO(Phone_Number);
                            new Prefe(getApplicationContext()).set_IsLogin("true");
                            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            new Prefe(getApplicationContext()).set_IsLogin("false");
                            Constant.Show_Tos(getApplicationContext(), jsonObject.getString("msg"));
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
                hide_ProgressDialog();Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });
    }

    @OnClick({R.id.edit_phone_no, R.id.Resend_btn, R.id.otp_verify_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_phone_no:
                finish();
                break;
            case R.id.Resend_btn:
                if (ResendBtn.getText().toString().equals("RESEND OTP"))
                    Send_OTP_AGAIN();
                break;
            case R.id.otp_verify_btn:
                Verifotp(otpEd.getOTP());
                break;
        }
    }
    private void startSMSListener()
    {
        try {
            smsReceiver = new MySMSBroadcastReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (Exception e) {
        }
    }


    private void Send_OTP_AGAIN() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.LoginApi(Phone_Number);

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

                            Constant.Show_Tos(getApplicationContext(), "OTP Sent Successfully");
                            startTimer();
                        } else {
                            Constant.Show_Tos(getApplicationContext(), jsonObject.getString("msg"));
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
                hide_ProgressDialog();Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });
    }

    private void Initialization() {
        startSMSListener();
        startTimer();
        Phone_Number = getIntent().getStringExtra(PHONE_NO);
        showOtpMobile.setSelected(true);
        showOtpMobile.setText("An OTP has been sent to " + Phone_Number);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private void startTimer() {
        cTimer = new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
                ResendBtn.setTextColor(getResources().getColor(R.color.Gray));
                ResendBtn.setText("RESEND OTP in " + String.format(FORMAT_T,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                cancelTimer();
                ResendBtn.setTextColor(getResources().getColor(R.color.light_green));
                ResendBtn.setText(R.string.resend_otp);
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onOTPReceived(String otp) {
        String code = parseCode(otp);//Parse verification code
        otpEd.setOTP(code);
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }
}
