package com.flyby_riders.Ui.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.NetworkOperation.IJSONParseListener;
import com.flyby_riders.R;
import com.flyby_riders.Utils.CommentKeyBoardFix;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Ui.Listener.StringUtils.PHONE_NO;
import static com.flyby_riders.Ui.Listener.StringUtils.USER_ID;

public class Login_Form extends BaseActivity  implements IJSONParseListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    @BindView(R.id.goto_regestion)
    TextView gotoRegestion;
    @BindView(R.id.Country_code_ed)
    TextView CountryCodeEd;
    @BindView(R.id.pnone_no_ed)
    EditText pnoneNoEd;
    @BindView(R.id.edit_text_container)
    LinearLayout editTextContainer;
    @BindView(R.id.Error_tv)
    TextView ErrorTv;
    @BindView(R.id.send_otp_btn)
    Button sendOtpBtn;
    private static final int RC_HINT = 1000;
    private GoogleApiClient mCredentialsApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);
        ButterKnife.bind(this);
        new CommentKeyBoardFix(this);
        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        getphonenumber();
        pnoneNoEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ErrorTv.setText("");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editTextContainer.setBackground(getResources().getDrawable(R.drawable.ph_edittext_bg));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrorTv.setText("");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editTextContainer.setBackground(getResources().getDrawable(R.drawable.ph_edittext_bg));
                }
                if (s.length()==10) {
                    hit_Send_otp(pnoneNoEd.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                ErrorTv.setText("");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editTextContainer.setBackground(getResources().getDrawable(R.drawable.ph_edittext_bg));
                }
            }
        });
    }

    private void hit_Send_otp(String toString) {

        if (toString.isEmpty()) {
            ErrorTv.setText("Enter your phone number");
            editTextContainer.setBackground(getResources().getDrawable(R.drawable.edittext_bg_red));
            return;
        } else if (toString.length() < 10) {
            ErrorTv.setText("Enter a valid number");
            editTextContainer.setBackground(getResources().getDrawable(R.drawable.edittext_bg_red));
            return;
        } else {


            show_ProgressDialog();
            Call<ResponseBody> requestCall = retrofitCallback.RiderLogin(toString);

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
                                Intent intent = new Intent(Login_Form.this, OTP_Verify.class);
                                intent.putExtra(PHONE_NO, toString);
                                startActivity(intent);
                                Toast.makeText(Login_Form.this, "YOUR OTP- "+jsonObject.getString("otp"), Toast.LENGTH_LONG).show();
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
                    hide_ProgressDialog();
                }
            });


        }



    }

    @OnClick(R.id.send_otp_btn)
    public void onViewClicked() {
        hit_Send_otp(pnoneNoEd.getText().toString());
    }
    private void getphonenumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            Log.e("Login", "Could not start hint picker Intent", e);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {


                    if (credential.getId().length() > 10) {
                        pnoneNoEd.setText(credential.getId().substring(credential.getId().length() - 10));
                    } else {
                        pnoneNoEd.setText(credential.getId());
                    }

                } else {
                    Toast.makeText(this, "err", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
