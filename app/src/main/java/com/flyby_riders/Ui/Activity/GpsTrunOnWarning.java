package com.flyby_riders.Ui.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;
import com.flyby_riders.Constants.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GpsTrunOnWarning extends AppCompatActivity {

    @BindView(R.id.Text_title_tv)
    TextView TextTitleTv;
    @BindView(R.id.text_subtitle_tv)
    TextView textSubtitleTv;
    @BindView(R.id.Setting_Overview)
    TextView SettingOverview;
    @BindView(R.id.allow_access_TV)
    TextView allowAccessTV;
    private LocationManager mlocManager;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__trun__on);
        ButterKnife.bind(this);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @OnClick(R.id.allow_access_TV)
    public void onViewClicked() {
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc(GpsTrunOnWarning.this);

        }else
        {
            finish();
        }

    }
    private void enableLoc(Context context) {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            com.google.android.gms.common.api.PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult((Activity) context, StringUtils.REQUEST_LOCATION);
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }else
        {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            com.google.android.gms.common.api.PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult((Activity) context, StringUtils.REQUEST_LOCATION);
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        enableLoc(GpsTrunOnWarning.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StringUtils.REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    finish();
                }
            }
        }
    }
}