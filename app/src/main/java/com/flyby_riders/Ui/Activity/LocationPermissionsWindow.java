package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationPermissionsWindow extends AppCompatActivity {

    @BindView(R.id.Text_title_tv)
    TextView TextTitleTv;
    @BindView(R.id.text_subtitle_tv)
    TextView textSubtitleTv;
    @BindView(R.id.Setting_Overview)
    TextView SettingOverview;
    @BindView(R.id.allow_access_TV)
    TextView allowAccessTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__permission);
        ButterKnife.bind(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @OnClick(R.id.allow_access_TV)
    public void onViewClicked() {
        if (allowAccessTV.getText().toString().equals("ALLOW ACCESS"))
        {
            request_location_permission();
        }else if (allowAccessTV.getText().toString().equals("SETTINGS"))
        {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void request_location_permission() {

        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (check == PERMISSION_GRANTED) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 11111);
            }
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                boolean  isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        TextTitleTv.setText(getString(R.string.unable_to_proceed));
                        textSubtitleTv.setText(getString(R.string.just_a_minute));
                        SettingOverview.setVisibility(View.VISIBLE);
                        allowAccessTV.setText(getString(R.string.SETTINGS));
                    } else {
                        if (openDialogOnce) {
                            TextTitleTv.setText(getString(R.string.unable_to_proceed));
                            textSubtitleTv.setText(getString(R.string.just_a_minute));
                            SettingOverview.setVisibility(View.VISIBLE);
                            allowAccessTV.setText(getString(R.string.SETTINGS));
                        }
                    }
                }else if (isPermitted)
                {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


        }
    }
}