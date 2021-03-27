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

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationPermissionsWindow extends AppCompatActivity {


    TextView Text_title_tv;
    TextView text_subtitle_tv;
    TextView allow_access_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__permission);
      iniView();

    }

    private void iniView() {
        Text_title_tv = (TextView) findViewById(R.id.Text_title_tv);
        text_subtitle_tv = (TextView) findViewById(R.id.text_subtitle_tv);
        allow_access_TV = (TextView) findViewById(R.id.allow_access_TV);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)== PERMISSION_GRANTED
        ) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    private void request_location_permission() {

        int check =  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (check == PERMISSION_GRANTED) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 11111);
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openDialogOnce = true;
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                boolean isPermitted = grantResults[i] == PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        Text_title_tv.setText(getString(R.string.unable_to_proceed));
                        text_subtitle_tv.setText(getString(R.string.just_a_minute));
                        // SettingOverview.setVisibility(View.VISIBLE);
                        allow_access_TV.setText(getString(R.string.SETTINGS));
                        Constant.Show_Tos(getApplicationContext(),"Allow all the time - Permission For Ride");
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        if (openDialogOnce) {
                            Text_title_tv.setText(getString(R.string.unable_to_proceed));
                            text_subtitle_tv.setText(getString(R.string.just_a_minute));
                            // SettingOverview.setVisibility(View.VISIBLE);
                            allow_access_TV.setText(getString(R.string.SETTINGS));
                            Constant.Show_Tos(getApplicationContext(),"Allow all the time - Permission For Ride");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                } else if (isPermitted) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


        }
    }


    public void getPermission(View view) {
        if (allow_access_TV.getText().toString().equals("GIVE PERMISSION")) {
            request_location_permission();
        } else if (allow_access_TV.getText().toString().equals("SETTINGS")) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}