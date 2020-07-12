package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Create_Group_Ride extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.Show_My_Location)
    EditText ShowMyLocation;
    GoogleMap mMap;
    @BindView(R.id.addmedia_btn)
    LinearLayout addmediaBtn;
    @BindView(R.id.addmembers_btn)
    LinearLayout addmembersBtn;
    @BindView(R.id.schedule_for_later_btn)
    TextView scheduleForLaterBtn;
    @BindView(R.id.start_ride_btn)
    TextView startRideBtn;
    @BindView(R.id.tooltip1)
    TextView tooltip1;
    @BindView(R.id.tooltip2)
    TextView tooltip2;
    @BindView(R.id.BEFORE_RIDE_START_LAYOUT_HEADER)
    LinearLayout BEFORERIDESTARTLAYOUTHEADER;
    @BindView(R.id.Back_Btn_ex)
    RelativeLayout BackBtnEx;
    @BindView(R.id.Ride_name_tv)
    TextView RideNameTv;
    @BindView(R.id.location_switch)
    SwitchCompat locationSwitch;
    @BindView(R.id.Location_status_tv)
    TextView LocationStatusTv;
    @BindView(R.id.end_my_ride)
    TextView endMyRide;
    @BindView(R.id.BEFORE_RIDE_START_LAYOUT_FOOTER)
    LinearLayout BEFORERIDESTARTLAYOUTFOOTER;
    @BindView(R.id.distance_covered_tv)
    TextView distanceCoveredTv;
    @BindView(R.id.top_speed_tv)
    TextView topSpeedTv;
    @BindView(R.id.ride_time_tv)
    TextView rideTimeTv;
    @BindView(R.id.average_speed_tv)
    TextView averageSpeedTv;
    @BindView(R.id.Track_record)
    LinearLayout TrackRecord;
    @BindView(R.id.AFTER_RIDE_START_LAYOUT_FOOTER)
    LinearLayout AFTERRIDESTARTLAYOUTFOOTER;
    private Marker mCurrLocationMarker;
    double Latitude_Start = 0, Longitude_Start = 0;
    String My_Ride_ID="",Admin_User_Id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__group__ride);
        ButterKnife.bind(this);
        try{ My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
            Admin_User_Id = getIntent().getStringExtra("Admin_User_Id");
        }catch (Exception e){My_Ride_ID="";Admin_User_Id="";}
        Instantiation();
        View_Control();
    }

    private void View_Control() {
        if (new Session(this).get_LOGIN_USER_ID().equalsIgnoreCase(Admin_User_Id))
        {BEFORERIDESTARTLAYOUTFOOTER.setVisibility(View.VISIBLE); endMyRide.setVisibility(View.VISIBLE);
        }else{BEFORERIDESTARTLAYOUTFOOTER.setVisibility(View.GONE);
            endMyRide.setVisibility(View.GONE); }
    }

    private void Instantiation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ShowMyLocation.setSelected(true);
        //hitCreateRide
        if (My_Ride_ID!=null)
        {if (My_Ride_ID.equalsIgnoreCase(""))
        {hit_create_Ride();} }else
        {hit_create_Ride();}

    }

    private void Auto_Hide_ToolTip() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tooltip1.setVisibility(View.GONE);
                tooltip2.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(runnable, 2500);


    }

    @OnClick({R.id.Back_Btn,R.id.Back_Btn_ex, R.id.Ride_name_tv, R.id.end_my_ride, R.id.Track_record,R.id.addmedia_btn, R.id.addmembers_btn, R.id.schedule_for_later_btn, R.id.start_ride_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.addmedia_btn:
                Intent addmedia = new Intent(getApplicationContext(),Ride_Gallery.class);
                addmedia.putExtra("My_Ride_ID",My_Ride_ID);
                addmedia.putExtra("Admin_User_Id",Admin_User_Id);
                startActivity(addmedia);
                break;
            case R.id.addmembers_btn:
                Intent Ride_Members_Management = new Intent(getApplicationContext(),Ride_Members_Management.class);
                Ride_Members_Management.putExtra("My_Ride_ID",My_Ride_ID);
                Ride_Members_Management.putExtra("Admin_User_Id",Admin_User_Id);
                startActivity(Ride_Members_Management);
                break;
            case R.id.schedule_for_later_btn:
                break;
            case R.id.start_ride_btn:
                hit_Start_Ride();
                break;
            case R.id.Back_Btn_ex:
                finish();
                break;
            case R.id.Ride_name_tv:
                Open_Update_Ride_Name(this,RideNameTv.getText().toString());
                break;
            case R.id.end_my_ride:
                break;
            case R.id.Track_record:
                break;
        }
    }

    private void hit_Start_Ride() {
        //Latitude_Start,Longitude_Start;

    }

    private void hit_create_Ride() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.create_bike_ride(new Session(this).get_LOGIN_USER_ID()
                , "Ride" + Constant.generateRandomNumber(), ShowMyLocation.getText().toString(), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start));
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(), "Ride Created Successfully");
                            My_Ride_ID = jsonObject.getString("ride_id");
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        show_ProgressDialog();
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
        } catch (Resources.NotFoundException e) {
        }

        //Track my location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11111);
        } else {
            Fetch_Location_With_RX();
        }
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                    LatLng midLatLng = mMap.getCameraPosition().target;
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions().
                            position(midLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                }
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                    LatLng position = mCurrLocationMarker.getPosition();
                    Latitude_Start = position.latitude;
                    Longitude_Start = position.longitude;
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions().
                            position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                    ShowMyLocation.setText(Constant.getCompleteAddressString(getApplicationContext(), position.latitude, position.longitude));
                }

            }
        });

    }
    private void Fetch_Location_With_RX() {
        if (mMap != null) {
            if (mMap != null) {
                LocationTracker tracker = new LocationTracker(
                        this,
                        new TrackerSettings()
                                .setUseGPS(true)
                                .setUseNetwork(true)
                                .setUsePassive(true)
                ) {

                    @Override
                    public void onLocationFound(Location location) {
                        locationFetched(location);
                    }

                    @Override
                    public void onTimeout() {
                        hide_ProgressDialog();
                    }
                };
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                    return;
                }
                tracker.startListening();
            }
        }
    }
    private void locationFetched(Location location) {

        mMap.clear();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11111);
                    hide_ProgressDialog();
                }

            } else {
                mMap.setMyLocationEnabled(false);
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                LatLng currentPosition = new LatLng(latti, longi);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                markerOptions.position(currentPosition);
                markerOptions.draggable(false);
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                mMap.setMyLocationEnabled(true);
                ShowMyLocation.setText(Constant.getCompleteAddressString(this, currentPosition.latitude, currentPosition.longitude));
                hide_ProgressDialog();
                //Tigger To Hide ToolTip
                Auto_Hide_ToolTip();


            }
        } catch (Exception e) {
            hide_ProgressDialog();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];
                boolean isPermitted = grantResults[i] == PERMISSION_GRANTED;
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                } else if (isPermitted) {
                    Fetch_Location_With_RX();
                }
            }
        }
    }



    private void Open_Update_Ride_Name(Activity mActivity,String name) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = (mActivity).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_ride_name, null);
        dialogBuilder.setView(dialogView);

        EditText update_rename_ride = dialogView.findViewById(R.id.update_rename_ride);
        TextView update_tv = dialogView.findViewById(R.id.update_tv);
        TextView close_rename_ride = dialogView.findViewById(R.id.close_rename_ride);
        update_rename_ride.setText(name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        close_rename_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.hide();
            }
        });

        update_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (update_rename_ride.getText().toString().trim().isEmpty())
                {
                    Constant.Show_Tos(mActivity,"Enter Group Name");
                    return;
                }else
                {
                    hit_Update_name(update_rename_ride.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();


    }

    private void hit_Update_name(String GroupName)
    {




    }


}