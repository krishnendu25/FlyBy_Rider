package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Choose_Way_Screen extends BaseActivity {

    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.trams_conition_tv)
    TextView tramsConitionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__way__screen);
        ButterKnife.bind(this);
        //Fetch Location


    }

    @OnClick({R.id.sign_in_btn, R.id.trams_conition_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_in_btn:
                startActivity(new Intent(Choose_Way_Screen.this,Login_Form.class));
                break;
            case R.id.trams_conition_tv:
                startActivity(new Intent(Choose_Way_Screen.this,Legal.class));
                break;
        }
    }
}
