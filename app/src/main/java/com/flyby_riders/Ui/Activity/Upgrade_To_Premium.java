package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Upgrade_To_Premium extends AppCompatActivity {

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
                break;
            case R.id.pay_amp_upgrade_tv:
                break;
        }
    }
}