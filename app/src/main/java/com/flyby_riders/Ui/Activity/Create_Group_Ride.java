package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.CustomInfoWindowAdapter;
import com.flyby_riders.Ui.Model.My_Ride_Model;
import com.flyby_riders.Ui.Model.Real_Time_Latlong;
import com.flyby_riders.Ui.Service.LocationTrackerService;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import net.kjulio.rxlocation.RxLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.flyby_riders.Constants.Constant.GET_timeStamp;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_ENDED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_NOT_STARTED;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_PAUSE;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_PLAY;
import static com.flyby_riders.Ui.Listener.StringUtils.RIDE_STARTED;

public class Create_Group_Ride extends BaseActivity implements OnMapReadyCallback {
    public static String RIDE_STATUS = RIDE_NOT_STARTED, My_Ride_ID = "",
            Admin_User_Id = "", TRACKSTATUS = "";
    public static boolean I_AM_ADMIN = false, Location_Shearing_Service = false,
            Track_My_Location = false, ADMIN_PREMIUM_STATUS = false;
    /*@BindView(R.id.Swipe_Refresh_tv)
    SwipeRefreshLayout SwipeRefreshTv;*/ private final LocationRequest defaultLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    LinearLayout BEFORE_RIDE_START_LAYOUT_HEADER;
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
    LinearLayout BEFORE_RIDE_START_LAYOUT_FOOTER;
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
    LinearLayout AFTER_RIDE_START_LAYOUT_FOOTER;
    @BindView(R.id.AFTER_RIDE_START_LAYOUT_HEADER)
    LinearLayout AFTER_RIDE_START_LAYOUT_HEADER;
    @BindView(R.id.Before_Header_tv)
    TextView BeforeHeaderTv;
    @BindView(R.id.Track_IV)
    ImageView TrackIV;
    double Latitude_Start = 0, Longitude_Start = 0;
    ArrayList<Real_Time_Latlong> track_list = new ArrayList<>();
    int Create_Ride_Count = 0, BackGround_Service_Count = 0;
    Back_Ground_Service back_ground_service;
    Polyline line; //added
    ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    double STARTLAT = 0, STARTLANG = 0, ENDLAT = 0, ENDLANG = 0;
    ArrayList<LatLng> End_Ride_path = new ArrayList<>();
    private Marker mCurrLocationMarker;
    private double mlattitude, mlongitude;
    private Runnable updater, runnable;
    private ArrayList<LatLng> points; //added
    private LocationManager locationManager;
    private double longi, latti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__group__ride);
        ButterKnife.bind(this);
        try {
            STARTLAT = Double.parseDouble(getIntent().getStringExtra("STARTLAT"));
            STARTLANG = Double.parseDouble(getIntent().getStringExtra("STARTLANG"));
            ENDLAT = Double.parseDouble(getIntent().getStringExtra("ENDLAT"));
        } catch (Exception e) {

        }
        Instantiation();


    }

    private void Instantiation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        rideTimeTv.setSelected(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        rideTimeTv.setText(sdf.format(new Date()));
        points = new ArrayList<LatLng>(); //added
        try {
            My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
            Admin_User_Id = getIntent().getStringExtra("Admin_User_Id");
            TRACKSTATUS = getIntent().getStringExtra("TRACKSTATUS");

            if (new Session(this).get_LOGIN_USER_ID().equalsIgnoreCase(Admin_User_Id)) {
                I_AM_ADMIN = true;
            }

            if (TRACKSTATUS.equalsIgnoreCase("NOT STARTED")) {
                RIDE_STATUS = RIDE_NOT_STARTED;
            } else if (TRACKSTATUS.equalsIgnoreCase("STARTED")) {
                RIDE_STATUS = RIDE_STARTED;
            } else if (TRACKSTATUS.equalsIgnoreCase("END")) {
                RIDE_STATUS = RIDE_ENDED;
                Fetch_All_path(My_Ride_ID);
            }
            Cursor cursor = testAdapter.GET_RIDE_DATA(My_Ride_ID, new Session(this).get_LOGIN_USER_ID());
            if (cursor.getCount() == 0) {
                testAdapter.INSERT_RIDE_DATA(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(), "", "", "", String.valueOf(Track_My_Location));
            } else {
                while (cursor.moveToNext()) {
                    if (cursor.getString(5).equalsIgnoreCase("true")) {
                        Track_My_Location = true;
                    } else {
                        Track_My_Location = false;
                    }
                }
            }
        } catch (Exception e) {
            My_Ride_ID = "";
            Admin_User_Id = "";
            I_AM_ADMIN = false;
        }
        back_ground_service = new Back_Ground_Service();
        Reload_Work();
        View_Control();
        TrackIV.setTag(RIDE_PLAY);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ShowMyLocation.setSelected(true);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Location_Shearing_Service = true;
                    testAdapter.INSERT_RIDE_DATA(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(), "", "", "", String.valueOf(Location_Shearing_Service));

                } else {
                    Location_Shearing_Service = false;
                    testAdapter.INSERT_RIDE_DATA(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(), "", "", "", String.valueOf(Location_Shearing_Service));
                }
            }
        });
    }

    private void Reload_Work() {
        Handler timerHandler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                View_Control();
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                    if (Location_Shearing_Service) {
                        if (BackGround_Service_Count == 0) {
                            TrackerService(true);
                        }
                    } else {
                        if (BackGround_Service_Count > 0) {
                            TrackerService(false);
                        }
                    }
                }
                timerHandler.postDelayed(updater, 2000);
            }
        };
        timerHandler.post(updater);


        Handler timerHandlerE = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {

                    Cursor cursor = testAdapter.GET_REALTIMELOCATION(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID());

                    if (cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
                            SetMapView(cursor.getString(2), cursor.getString(3));
                        }
                    }
                    Fetch_RealTime_Location(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID());
                    //-------------------------------------------------------------------------------------------------///

                    try {
                        get_Ride_Data(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID());

                    } catch (Exception e) {

                    }
                }

                timerHandlerE.postDelayed(runnable, 10000);
            }
        };
        timerHandler.post(runnable);


    }

    private void View_Control() {
        try {
            if (I_AM_ADMIN) {
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                    BEFORE_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_FOOTER.setVisibility(View.VISIBLE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    endMyRide.setVisibility(View.VISIBLE);
                    get_Ride_Data(My_Ride_ID, new Session(this).get_LOGIN_USER_ID());
                } else if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
                    TrackRecord.setVisibility(View.GONE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                    BEFORE_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_FOOTER.setVisibility(View.VISIBLE);
                    BeforeHeaderTv.setText(getString(R.string.ENDING_POINT));
                    Fetch_All_path(My_Ride_ID);
                    get_Ride_Data(My_Ride_ID, new Session(this).get_LOGIN_USER_ID());
                } else if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
                    BEFORE_RIDE_START_LAYOUT_FOOTER.setVisibility(View.VISIBLE);
                    AFTER_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                }
            } else {
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                    BEFORE_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_FOOTER.setVisibility(View.VISIBLE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    endMyRide.setVisibility(View.GONE);
                    get_Ride_Data(My_Ride_ID, new Session(this).get_LOGIN_USER_ID());
                } else if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
                    TrackRecord.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                    BeforeHeaderTv.setText(getString(R.string.ENDING_POINT));
                    Fetch_All_path(My_Ride_ID);
                    get_Ride_Data(My_Ride_ID, new Session(this).get_LOGIN_USER_ID());

                } else if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
                    BEFORE_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    AFTER_RIDE_START_LAYOUT_FOOTER.setVisibility(View.GONE);
                    BEFORE_RIDE_START_LAYOUT_HEADER.setVisibility(View.VISIBLE);
                    AFTER_RIDE_START_LAYOUT_HEADER.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }


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
        handler.postDelayed(runnable, 5000);


    }

    private void Switching_Track_btn() {

        if (TrackIV.getTag().equals(RIDE_PLAY)) {
            TrackIV.setTag(RIDE_PAUSE);
            TrackIV.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause));
            Track_My_Location = true;


        } else if (TrackIV.getTag().equals(RIDE_PAUSE)) {
            TrackIV.setTag(RIDE_PLAY);
            TrackIV.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play));
            Track_My_Location = false;

        }


    }

    private void Open_Update_Ride_Name(Activity mActivity, String name) {
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

                if (update_rename_ride.getText().toString().trim().isEmpty()) {
                    Constant.Show_Tos(mActivity, "Enter Group Name");
                    return;
                } else {
                    hit_Update_name(update_rename_ride.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (back_ground_service != null) {
            back_ground_service.execute();
            Auto_Hide_ToolTip();
        } else {
            new Back_Ground_Service().execute();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (back_ground_service != null) {
                if (back_ground_service.getStatus() == AsyncTask.Status.FINISHED) {
                    back_ground_service.execute();
                }
            } else {
                new Back_Ground_Service().execute();
            }
        } catch (Exception e) {

        }


    }


    void get_Ride_Data(String Ride_id, String Member_id) {
        averageSpeedTv.setText(new DecimalFormat("##.##").format(Double.valueOf(testAdapter.GET_AVERAGE_SPEED(Ride_id, Member_id))) + "km/h");
        topSpeedTv.setText(new DecimalFormat("##.##").format(Double.valueOf(testAdapter.GET_TOP_SPEED(Ride_id, Member_id))) + "km/h");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        rideTimeTv.setText(sdf.format(new Date()));


        if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(STARTLAT);
            startPoint.setLongitude(STARTLANG);
            Location endPoint = new Location("locationb");
            endPoint.setLatitude(ENDLAT);
            endPoint.setLongitude(ENDLANG);
            double Dis = Double.valueOf(startPoint.distanceTo(endPoint)) * 0.001;
            distanceCoveredTv.setText(new DecimalFormat("##.##").format(Dis) + "km");
            ShowMyLocation.setText(Constant.getCompleteAddressString(this, STARTLAT, STARTLANG));

        }


    }


    @OnClick({R.id.Back_Btn, R.id.Back_Btn_ex, R.id.Ride_name_tv, R.id.end_my_ride, R.id.Track_record,
            R.id.addmedia_btn, R.id.addmembers_btn, R.id.schedule_for_later_btn, R.id.start_ride_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.addmedia_btn:
                Intent addmedia = new Intent(getApplicationContext(), Ride_Gallery.class);
                addmedia.putExtra("My_Ride_ID", My_Ride_ID);
                addmedia.putExtra("Admin_User_Id", Admin_User_Id);
                startActivity(addmedia);
                break;
            case R.id.addmembers_btn:
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED) || RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                    Intent Ride_Members_Management = new Intent(getApplicationContext(), Ride_Members_Management.class);
                    Ride_Members_Management.putExtra("My_Ride_ID", My_Ride_ID);
                    Ride_Members_Management.putExtra("Admin_User_Id", Admin_User_Id);
                    startActivity(Ride_Members_Management);
                } else {
                    Constant.Show_Tos(getApplicationContext(), "Ride End So You Can't Access Member Management");
                }


                break;
            case R.id.schedule_for_later_btn:
                break;
            case R.id.start_ride_btn:
                if (Latitude_Start != 0 && Longitude_Start != 0) {
                    hit_Start_End_Ride(true, false);
                } else {
                    if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED))
                        Fetch_Location_With_RX();
                }

                break;
            case R.id.Back_Btn_ex:
                finish();
                break;
            case R.id.Ride_name_tv:
                Open_Update_Ride_Name(this, RideNameTv.getText().toString());
                break;
            case R.id.end_my_ride:
                hit_Start_End_Ride(false, true);
                break;
            case R.id.Track_record:
                Switching_Track_btn();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
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
            if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED))
                Fetch_Location_With_RX();
        }
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                        LatLng midLatLng = mMap.getCameraPosition().target;
                        mCurrLocationMarker = mMap.addMarker(new MarkerOptions().
                                position(midLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                    }
                }


            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
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
            }
        });

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


    private void TrackerService(boolean track_My_Location) {
        if (track_My_Location) {
            BackGround_Service_Count++;
            startService(new Intent(this, LocationTrackerService.class));
        } else {
            BackGround_Service_Count = 0;
            stopService(new Intent(this, LocationTrackerService.class));
        }

    }

    private void SetMapView(String LATITUDE, String LONGITUDE) {
        if (mMap != null) {
            LatLng currentPosition = new LatLng(Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE));
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            markerOptions.position(currentPosition);
            markerOptions.title("YOU");
            markerOptions.draggable(false);
            points.add(currentPosition); //added
            mMap.clear();  //clears all Markers and Polylines
            PolylineOptions options = new PolylineOptions().width(10).color(Color.parseColor("#F7B500")).geodesic(true);
            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            mCurrLocationMarker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            line = mMap.addPolyline(options); //add Polyline
        }
    }

    void Other_Location_View(ArrayList<Real_Time_Latlong> track_list) {
        mMap.clear();
        for (int i = 0; i < track_list.size(); i++) {
            if (!track_list.get(i).getMEMBERID().equalsIgnoreCase(new Session(getApplicationContext()).get_LOGIN_USER_ID())) {
                if (mMap != null) {
                    LatLng currentPosition = new LatLng(Double.valueOf(track_list.get(i).getLatitude_Start()), Double.valueOf(track_list.get(i).getLongitude_Start()));
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.valueOf(track_list.get(i).getLatitude_Start()), Double.valueOf(track_list.get(i).getLongitude_Start())));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                    markerOptions.position(currentPosition);
                    markerOptions.title(track_list.get(i).getRider_Name());
                    markerOptions.draggable(false);
                    points.add(currentPosition);
                    mMap.addMarker(markerOptions);
                }
            }
        }
    }

    private void Fetch_Location_With_RX() {
        show_ProgressDialog();
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
                Get_Manual_Gps();
            }

        }
    }

    private void Get_Manual_Gps() {
        mMap.clear();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 199);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                if (location != null) {
                    try {
                        latti = location.getLatitude();
                        longi = location.getLongitude();
                        locationFetched(location);
                        /*  progressDialog.dismiss();*/
                    } catch (NullPointerException e) {

                    }

                } else if (location1 != null) {
                    try {
                        latti = location1.getLatitude();
                        longi = location1.getLongitude();
                        locationFetched(location1);
                        /*   progressDialog.dismiss();*/

                    } catch (NullPointerException e) {

                    }
                } else if (location2 != null) {
                    try {
                        latti = location2.getLatitude();
                        longi = location2.getLongitude();
                        locationFetched(location2);
                    } catch (NullPointerException e) {

                    }
                } else {
                    RxLocation.locationUpdates(this, defaultLocationRequest)
                            .firstElement()
                            .subscribe(new Consumer<Location>() {
                                @Override
                                public void accept(Location location) throws Exception {
                                    locationFetched(location);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void locationFetched(Location location) {
        hide_ProgressDialog();
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
                Latitude_Start = location.getLatitude();
                Longitude_Start = location.getLongitude();
                LatLng currentPosition = new LatLng(Latitude_Start, Longitude_Start);
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

                if (Create_Ride_Count == 0) {
                    if (My_Ride_ID != null) {
                        if (My_Ride_ID.equalsIgnoreCase("")) {
                            hit_create_Ride();
                        }
                    } else {
                        hit_create_Ride();
                    }
                }


            }
        } catch (Exception e) {
            hide_ProgressDialog();
        }

    }


    //Hit_Api
    private void hit_Update_name(String GroupName) {


    }

    private void hit_my_ride(String user_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_fetch_my_ride(user_id);
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
                        MyRide_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            JSONArray RIDEDETAILS_LIST = jsonObject.getJSONArray("RIDEDETAILS");
                            for (int i = 0; i < RIDEDETAILS_LIST.length(); i++) {
                                JSONObject JS = RIDEDETAILS_LIST.getJSONObject(i);
                                if (JS.getString("RIDEID").equalsIgnoreCase(My_Ride_ID)) {
                                    My_Ride_Model RD = new My_Ride_Model();
                                    RD.setRide_Name(JS.getString("RIDENAME"));
                                    RD.setRide_Total_Member(JS.getString("RIDENAME"));
                                    RD.setRide_Start_Date(JS.getString("CREATIONDATE"));
                                    RD.setRide_Total_Distance(JS.getString("TOTALKM"));
                                    RD.setRide_Cover_pic(JS.getString("PICMEDIAFILE"));
                                    RD.setRide_Status(JS.getString("TRACKSTATUS"));
                                    RD.setRide_Admin_Id(JS.getString("ADMINUSERID"));
                                    RD.setRide_ID(JS.getString("RIDEID"));
                                    RD.setTotal_media(JS.getString("COUNTIMAGELIST"));
                                    RD.setADMIN_PLANNAME(JS.getString("PLANNAME"));
                                    RD.setSTARTLANG(JS.getString("STARTLANG"));
                                    RD.setSTARTLAT(JS.getString("STARTLAT"));
                                    RD.setENDLANG(JS.getString("ENDLANG"));
                                    RD.setENDLAT(JS.getString("ENDLAT"));
                                    MyRide_List.add(RD);
                                }
                            }
                            if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase("NOT STARTED")) {
                                RIDE_STATUS = RIDE_NOT_STARTED;
                            } else if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase("STARTED")) {
                                RIDE_STATUS = RIDE_STARTED;
                            } else if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase("END")) {
                                RIDE_STATUS = RIDE_ENDED;
                                STARTLAT = Double.valueOf(MyRide_List.get(0).getSTARTLAT());
                                STARTLANG = Double.valueOf(MyRide_List.get(0).getSTARTLANG());
                                ENDLAT = Double.valueOf(MyRide_List.get(0).getENDLAT());
                                ENDLANG = Double.valueOf(MyRide_List.get(0).getENDLANG());
                                Fetch_All_path(My_Ride_ID);
                            }
                            RideNameTv.setText(MyRide_List.get(0).getRide_Name());

                            if (MyRide_List.get(0).getADMIN_PLANNAME().equalsIgnoreCase("Premium Account")) {
                                ADMIN_PREMIUM_STATUS = true;
                            } else {
                                ADMIN_PREMIUM_STATUS = false;
                            }
                            ;


                        } else {
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        hide_ProgressDialog();
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });


    }

    private void Fetch_RealTime_Location(String My_Ride_ID, String Member_ID) {

        Call<ResponseBody> requestCall = retrofitCallback.fetch_location_tracker(My_Ride_ID, Member_ID);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            track_list.clear();
                            JSONArray TRACKDETAILS = jsonObject.getJSONArray("TRACKDETAILS");

                            for (int i = 0; i < TRACKDETAILS.length(); i++) {
                                JSONObject JS = TRACKDETAILS.getJSONObject(i);
                                Real_Time_Latlong real_time_latlong = new Real_Time_Latlong();
                                real_time_latlong.setRider_Name(JS.getString("FULL_NAME"));
                                real_time_latlong.setMEMBERID(JS.getString("MEMBERID"));
                                real_time_latlong.setLongitude_Start(Double.valueOf(JS.getString("LONGTIUDE")));
                                real_time_latlong.setLatitude_Start(Double.valueOf(JS.getString("LATITUDE")));
                                track_list.add(real_time_latlong);
                            }

                            try {

                            } catch (Exception e) {
                                Other_Location_View(track_list);
                            }


                        } else {
                        }


                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });

    }

    private void Fetch_All_path(String My_Ride_ID) {

        Call<ResponseBody> requestCall = retrofitCallback.fetch_location_tracker(My_Ride_ID);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

                        End_Ride_path.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            track_list.clear();
                            JSONArray TRACKDETAILS = jsonObject.getJSONArray("TRACKDETAILS");

                            for (int i = 0; i < TRACKDETAILS.length(); i++) {
                                JSONObject JS = TRACKDETAILS.getJSONObject(i);
                                if (JS.getString("MEMBERID").equalsIgnoreCase(new Session(getApplicationContext()).get_LOGIN_USER_ID())) {
                                    End_Ride_path.add(new LatLng(Double.valueOf(JS.getString("LATITUDE")), Double.valueOf(JS.getString("LONGTIUDE"))));
                                }
                                End_Ride_Poly(STARTLAT, STARTLANG, ENDLAT, ENDLANG, End_Ride_path);
                            }

                        } else {
                            End_Ride_Poly(STARTLAT, STARTLANG, ENDLAT, ENDLANG, End_Ride_path);

                        }


                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });

    }

    void hit_update_My_Location(double mlattitude, double mlongitude) {
        Call<ResponseBody> requestCall = retrofitCallback.location_tracker(String.valueOf(mlattitude), String.valueOf(mlongitude), My_Ride_ID, new Session(this).get_LOGIN_USER_ID(), GET_timeStamp());
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });


    }

    private void hit_create_Ride() {
        Create_Ride_Count = Create_Ride_Count + 1;
        show_ProgressDialog();
        String Ride_NAME = "Ride" + Constant.generateRandomNumber();
        Call<ResponseBody> requestCall = retrofitCallback.create_bike_ride(new Session(this).get_LOGIN_USER_ID()
                , Ride_NAME, ShowMyLocation.getText().toString(), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start));
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
                            Constant.Show_Tos(getApplicationContext(), "Ride Created Successfully");
                            My_Ride_ID = jsonObject.getString("ride_id");
                            I_AM_ADMIN = true;
                            RideNameTv.setText(Ride_NAME);
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
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });
    }

    private void hit_Start_End_Ride(boolean Start, boolean End) {
        if (Start) {

            show_ProgressDialog();
            Call<ResponseBody> requestCall = retrofitCallback.START_RIDE(My_Ride_ID, Admin_User_Id, Constant.GET_timeStamp(), Constant.getCompleteAddressString(Create_Group_Ride.this, Latitude_Start, Longitude_Start), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start), "1");
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
                                Constant.Show_Tos(getApplicationContext(), "Ride Started Successfully");
                                STARTLAT = Latitude_Start;
                                STARTLANG = Longitude_Start;
                                View_Control();
                                RIDE_STATUS = RIDE_STARTED;
                                hit_my_ride(new Session(Create_Group_Ride.this).get_LOGIN_USER_ID());

                            } else {
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hide_ProgressDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Constant.Show_Tos_Error(getApplicationContext(), true, false);
                    hide_ProgressDialog();
                }
            });

        } else if (End) {
            show_ProgressDialog();
            Call<ResponseBody> requestCall = retrofitCallback.END_RIDE(My_Ride_ID, Admin_User_Id, Constant.GET_timeStamp(), Constant.getCompleteAddressString(Create_Group_Ride.this, Latitude_Start, Longitude_Start), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start), "0");
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
                                Constant.Show_Tos(getApplicationContext(), "Ride Ended Successfully");
                                hit_my_ride(new Session(Create_Group_Ride.this).get_LOGIN_USER_ID());
                                RIDE_STATUS = RIDE_ENDED;
                                BackGround_Service_Count = 0;
                                View_Control();
                                stopService(new Intent(Create_Group_Ride.this, LocationTrackerService.class));
                            } else {
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hide_ProgressDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Constant.Show_Tos_Error(getApplicationContext(), true, false);
                    hide_ProgressDialog();
                }
            });
        }
    }

    void End_Ride_Poly(double st_lat, double st_long, double end_lat, double end_long, ArrayList<LatLng> points_) {
        try {

            LatLng Start = new LatLng(st_lat, st_long);
            LatLng END = new LatLng(end_lat, end_long);
            if (points_.size() == 0) {
                points_.add(Start);
                points_.add(END);
            }
            if (mMap != null) {
                mMap.clear();  //clears all Markers and Polylines
                PolylineOptions options = new PolylineOptions().width(10).color(Color.parseColor("#F7B500")).geodesic(true);
                for (int i = 0; i < points_.size(); i++) {
                    LatLng point = points_.get(i);
                    options.add(point);
                }
                MarkerOptions markerOptions = new MarkerOptions().position(Start);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                markerOptions.position(Start);
                mMap.addMarker(markerOptions);
                MarkerOptions markerOptionss = new MarkerOptions().position(END);
                markerOptionss.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                markerOptionss.position(END);
                markerOptionss.draggable(false);
                mMap.addMarker(markerOptionss);
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Start));
                mCurrLocationMarker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
                line = mMap.addPolyline(options); //add Polyline
            }
        } catch (Exception e) {
        }
    }

    class Back_Ground_Service extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            mlattitude = Double.parseDouble(intent.getStringExtra(LocationTrackerService.EXTRA_LATITUDE));
                            mlongitude = Double.parseDouble(intent.getStringExtra(LocationTrackerService.EXTRA_LONGITUDE));

                            if (My_Ride_ID != null) {
                                if (!My_Ride_ID.equalsIgnoreCase("")) {
                                    if (Track_My_Location) {
                                        hit_update_My_Location(mlattitude, mlongitude);
                                    }
                                }
                            }

                            if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                                if (Track_My_Location) {
                                    testAdapter.INSERT_REALTIMELOCATION(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(), String.valueOf(mlattitude), String.valueOf(mlongitude), GET_timeStamp());

                                    double Real_Time_Speed = Double.valueOf(intent.getStringExtra(LocationTrackerService.EXTRA_SPEED));//meters/second
                                    double Real_Time_Speed_kmph = Real_Time_Speed * 3.6;
                                    Location startPoint = new Location("locationA");
                                    startPoint.setLatitude(STARTLAT);
                                    startPoint.setLongitude(STARTLANG);
                                    Location endPoint = new Location("locationb");
                                    endPoint.setLatitude(mlattitude);
                                    endPoint.setLongitude(mlongitude);
                                    double Dis = Double.valueOf(startPoint.distanceTo(endPoint)) * 0.001;
                                    distanceCoveredTv.setText(new DecimalFormat("##.##").format(Dis) + "km");
                                    testAdapter.INSERT_RIDE_DATA(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(),
                                            String.valueOf(Real_Time_Speed_kmph), String.valueOf(Real_Time_Speed_kmph), GET_timeStamp(), RIDE_STATUS);
                                }
                            }


                        }
                    }, new IntentFilter(LocationTrackerService.ACTION_LOCATION_BROADCAST)
            );
            return "";
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);
        }
    }
}