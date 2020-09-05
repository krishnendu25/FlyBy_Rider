package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.flyby_riders.Ui.Listener.DirectionPointListener;
import com.flyby_riders.Ui.Model.My_Ride_Model;
import com.flyby_riders.Ui.Model.Real_Time_Latlong;
import com.flyby_riders.Ui.Service.LocationTrackerService;
import com.flyby_riders.Utils.DistanceCalculator;
import com.flyby_riders.Utils.GetPathFromLocation;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import net.kjulio.rxlocation.RxLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

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

public class Create_Group_Ride extends BaseActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
    public static boolean I_AM_ADMIN = false, Location_Shearing_Service = false, Track_My_Location = false,
            ADMIN_PREMIUM_STATUS = false;
    private final LocationRequest defaultLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    public String RIDE_STATUS = RIDE_NOT_STARTED, My_Ride_ID = "", My_Ride_Name = "",
            Admin_User_Id = "", TRACKSTATUS = "";
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.Before_Header_tv)
    TextView BeforeHeaderTv;
    @BindView(R.id.Show_My_Location)
    EditText ShowMyLocation;
    @BindView(R.id.BEFORE_RIDE_START_LAYOUT_HEADER)
    LinearLayout Before_ride_start_header;
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
    @BindView(R.id.AFTER_RIDE_START_LAYOUT_HEADER)
    LinearLayout After_ride_start_header;
    @BindView(R.id.tooltip1)
    TextView tooltip1;
    @BindView(R.id.addmedia_btn)
    LinearLayout addmediaBtn;
    @BindView(R.id.tooltip2)
    TextView tooltip2;
    @BindView(R.id.addmembers_btn)
    LinearLayout addmembersBtn;
    @BindView(R.id.schedule_for_later_btn)
    TextView scheduleForLaterBtn;
    @BindView(R.id.start_ride_btn)
    TextView startRideBtn;
    @BindView(R.id.BEFORE_RIDE_START_LAYOUT_FOOTER)
    LinearLayout Before_ride_start_footer;
    @BindView(R.id.distance_covered_tv)
    TextView distanceCoveredTv;
    @BindView(R.id.top_speed_tv)
    TextView topSpeedTv;
    @BindView(R.id.ride_time_tv)
    TextView rideTimeTv;
    @BindView(R.id.average_speed_tv)
    TextView averageSpeedTv;
    @BindView(R.id.Track_IV)
    ImageView TrackIV;
    @BindView(R.id.Track_record)
    LinearLayout TrackRecord;
    @BindView(R.id.AFTER_RIDE_START_LAYOUT_FOOTER)
    LinearLayout After_ride_start_footer;
    @BindView(R.id.track_location_btn)
    LinearLayout trackLocationBtn;
    ArrayList<Real_Time_Latlong> team_mate_Loc;
    private GoogleMap mMap;
    private Session session;
    private ArrayList<My_Ride_Model> MyRide_List = new ArrayList<>();
    private int Create_Ride_Flag = 0, BackGround_Service_Count = 0;
    private double Latitude_Start = 0, Longitude_Start = 0, Latitude_End = 0, Longitude_End = 0;
    private Marker mCurrLocationMarker;
    private double currentLatitude = 0, currentLongitude = 0;
    private Runnable updater, runnable, locupdate;
    private ArrayList<LatLng> points;
    private Location_Track_Service location_track_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__group__ride);
        ButterKnife.bind(this);
        GetIntent();
        Instantiation();


    }

    private void GetIntent() {
        try {
            if (getIntent().getStringExtra("STARTLAT") != null)
                Latitude_Start = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("STARTLAT")));
            if (getIntent().getStringExtra("STARTLANG") != null)
                Longitude_Start = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("STARTLANG")));
            if (getIntent().getStringExtra("ENDLAT") != null)
                Latitude_End = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("ENDLAT")));
            if (getIntent().getStringExtra("ENDLANG") != null)
                Longitude_End = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("ENDLANG")));
            if (getIntent().getStringExtra("My_Ride_Name") != null)
                My_Ride_Name = Objects.requireNonNull(getIntent().getStringExtra("My_Ride_Name"));
        } catch (Exception e) {
        }
        try {
            if (getIntent().getStringExtra("My_Ride_ID") != null)
                My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
            if (getIntent().getStringExtra("Admin_User_Id") != null)
                Admin_User_Id = getIntent().getStringExtra("Admin_User_Id");
            if (getIntent().getStringExtra("TRACKSTATUS") != null)
                RIDE_STATUS = getIntent().getStringExtra("TRACKSTATUS");
        } catch (Exception e) {

        }

        if (Admin_User_Id.equalsIgnoreCase("")) {
            Change_Status(true, RIDE_NOT_STARTED, true, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID());
        } else {
            Change_Status(true, RIDE_STATUS, true, Admin_User_Id);

        }
        SetText("", "", My_Ride_Name);


    }

    private void Instantiation() {
        location_track_service = new Location_Track_Service();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        team_mate_Loc = new ArrayList<Real_Time_Latlong>();
        points = new ArrayList<LatLng>();
        locationSwitch.setOnCheckedChangeListener(this);
        TrackIV.setTag(RIDE_PLAY);

    }

    private void Reload_UI() {
        Handler timerHandler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
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
                }
                timerHandlerE.postDelayed(runnable, 3000);
            }
        };
        timerHandler.post(runnable);

        Handler timermember = new Handler();
        locupdate = new Runnable() {
            @Override
            public void run() {
                if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                    hit_member_location_fetch(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID());
                }
                timermember.postDelayed(runnable, 10000);
            }
        };
        timermember.post(locupdate);
    }

    private void Change_Status(boolean Ride_Status, String Ride_State, boolean Admin_Status, String UserId) {
        if (Ride_Status) {
            if (Ride_State.equalsIgnoreCase(RIDE_NOT_STARTED)) {
                RIDE_STATUS = RIDE_NOT_STARTED;
            } else if (Ride_State.equalsIgnoreCase(RIDE_STARTED)) {
                RIDE_STATUS = RIDE_STARTED;
            } else if (Ride_State.equalsIgnoreCase(RIDE_ENDED)) {
                RIDE_STATUS = RIDE_ENDED;
                EndRideView();
            }
            Set_Component_Visibility();
        }
        if (Admin_Status) {
            if (new Session(Create_Group_Ride.this).get_LOGIN_USER_ID().equalsIgnoreCase(UserId)) {
                I_AM_ADMIN = true;
            } else {
                I_AM_ADMIN = false;
            }
            if (I_AM_ADMIN) {
                Admin_User_Id = UserId;
            }
        }
    }

    private void SetText(String Start_Address, String End_Address, String Ride_Name) {
        if (!Start_Address.equalsIgnoreCase("")) {
            ShowMyLocation.setText(Start_Address);
        } else if (!End_Address.equalsIgnoreCase("")) {
            ShowMyLocation.setText(End_Address);
        } else if (!Ride_Name.equalsIgnoreCase("")) {
            RideNameTv.setText(Ride_Name);
        }
    }

    private void Set_Component_Visibility() {
        if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
            AfterSubComponent(false, false);
            BeforeSubComponent(true, true);

        }
        if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
            AfterSubComponent(true, true);
            BeforeSubComponent(false, false);

        }
        if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
            AfterSubComponent(false, true);
            BeforeSubComponent(true, false);
            BeforeHeaderTv.setText(getString(R.string.ENDING_POINT));
            TrackRecord.setVisibility(View.GONE);
        }
    }

    private void AfterSubComponent(boolean header, boolean footer) {
        if (header) {
            After_ride_start_header.setVisibility(View.VISIBLE);
        } else {
            After_ride_start_header.setVisibility(View.GONE);
        }
        if (footer) {
            After_ride_start_footer.setVisibility(View.VISIBLE);
        } else {
            After_ride_start_footer.setVisibility(View.GONE);
        }
    }

    private void BeforeSubComponent(boolean header, boolean footer) {
        if (header) {
            Before_ride_start_header.setVisibility(View.VISIBLE);
        } else {
            Before_ride_start_header.setVisibility(View.GONE);
        }
        if (footer) {
            Before_ride_start_footer.setVisibility(View.VISIBLE);
        } else {
            Before_ride_start_footer.setVisibility(View.GONE);
        }
    }

    private void Fetch_My_Location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11111);
        } else {
            show_ProgressDialog();
            if (mMap != null) {
                LocationTracker tracker = new LocationTracker(
                        this, new TrackerSettings().setUseGPS(true).setUseNetwork(true).setUsePassive(true)) {
                    @Override
                    public void onLocationFound(Location location) {
                        locationFetched(location);
                    }

                    @Override
                    public void onTimeout() {
                        hide_ProgressDialog();
                    }
                };
                tracker.startListening();
                Get_Manual_Gps();
            }
        }
    }

    private void Get_Manual_Gps() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 199);
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (location != null) {
                    try {
                        locationFetched(location);
                    } catch (Exception e) {
                    }
                } else if (location1 != null) {
                    try {
                        locationFetched(location1);
                    } catch (Exception e) {
                    }
                } else if (location2 != null) {
                    try {
                        locationFetched(location2);
                    } catch (Exception e) {
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
                                public void accept(Throwable throwable) {
                                }
                            });
                }
            }
        } catch (Exception e) {
            Constant.Show_Tos_Error(this, false, true);
        }
    }

    private void locationFetched(Location location) {
        hide_ProgressDialog();
        if (mMap != null) {
            if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED)) {
                mMap.clear();
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
                String Start_Address = Constant.getCompleteAddressString(this, currentPosition.latitude, currentPosition.longitude);
                SetText(Start_Address, "", "");
                //Tigger To Hide ToolTip
                Auto_Hide_ToolTip();
                if (Create_Ride_Flag == 0) {
                    if (My_Ride_ID != null) {
                        if (My_Ride_ID.equalsIgnoreCase("")) {
                            hit_Ride_api(false, false, false, false, false, true);
                        }
                    } else {
                        hit_Ride_api(false, false, false, false, false, true);

                    }
                }
            }

            if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(currentPosition);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                markerOptions.position(currentPosition);
                markerOptions.draggable(false);
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                String Start_Address = Constant.getCompleteAddressString(this, currentPosition.latitude, currentPosition.longitude);
                SetText(Start_Address, "", "");
            }
            if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
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

    private void EndRideView() {
        if (mMap != null) {
            LatLng StartPosition = new LatLng(Latitude_Start, Longitude_Start);
            LatLng EndPosition = new LatLng(Latitude_End, Longitude_End);
            //Start Position Marker
            MarkerOptions markerStart = new MarkerOptions().position(StartPosition);
            markerStart.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            markerStart.position(StartPosition);
            markerStart.title("STARTING POINT");
            markerStart.draggable(false);
            mMap.addMarker(markerStart);
            //End Position Marker
            MarkerOptions markerEnd = new MarkerOptions().position(EndPosition);
            markerEnd.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            markerEnd.position(EndPosition);
            markerEnd.draggable(false);
            markerEnd.title("ENDING POINT");
            mMap.addMarker(markerEnd);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(StartPosition)
                    .include(EndPosition).build();
            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

            /*Start Point To End Point Path Draw*/
            show_ProgressDialog();
            new GetPathFromLocation(StartPosition, EndPosition, new DirectionPointListener() {
                @Override
                public void onPath(PolylineOptions polyLine) {
                    hide_ProgressDialog();
                    mMap.addPolyline(polyLine);

                }
            }).execute();
        }
        /*End Point Address*/
        String End_Address = Constant.getCompleteAddressString(getApplicationContext(), Latitude_End, Longitude_End);
        SetText("", End_Address, "");
        //Set Ride_Data
        String Distance = DistanceCalculator.distance(Latitude_Start, Longitude_Start, Latitude_End, Longitude_End);
        RideData_analytics(Distance);


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
                    RideNameTv.setText(update_rename_ride.getText().toString());
                    hit_Ride_api(true, false, false, false, false, false);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();


    }

    private void Record_Btn() {
        if (TrackIV.getTag().equals(RIDE_PLAY)) {
            TrackIV.setTag(RIDE_PAUSE);
            TrackIV.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause));
            Track_My_Location = true;


        } else if (TrackIV.getTag().equals(RIDE_PAUSE)) {
            TrackIV.setTag(RIDE_PLAY);
            TrackIV.setImageDrawable(this.getResources().getDrawable(R.drawable.play_rc));
            Track_My_Location = false;

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
            mMap.addPolyline(options); //add Polyline
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

    void Members_Live_Location(ArrayList<Real_Time_Latlong> track_list) {
        mMap.clear();
        for (int i = 0; i < track_list.size(); i++) {
            if (!track_list.get(i).getMEMBERID().equalsIgnoreCase(new Session(getApplicationContext()).get_LOGIN_USER_ID())) {
                if (mMap != null) {
                    LatLng currentPosition = new LatLng(track_list.get(i).getLatitude_Start(), track_list.get(i).getLongitude_Start());
                    MarkerOptions markerOptions = new MarkerOptions().position(currentPosition);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                    markerOptions.position(currentPosition);
                    markerOptions.title(track_list.get(i).getRider_Name());
                    markerOptions.draggable(false);
                    mMap.addMarker(markerOptions);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        } catch (Exception e) {
            Constant.Show_Tos_Error(getApplicationContext(), false, true);
        }
        Change_Status(true, RIDE_STATUS, true, Admin_User_Id);
        Fetch_My_Location();
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
                        String Start_Address = Constant.getCompleteAddressString(getApplicationContext(), position.latitude, position.longitude);
                        SetText(Start_Address, "", "");
                    }
                }
            }
        });
    }


    @OnClick({R.id.track_location_btn, R.id.Back_Btn, R.id.Back_Btn_ex, R.id.Ride_name_tv, R.id.end_my_ride, R.id.addmedia_btn, R.id.addmembers_btn, R.id.schedule_for_later_btn, R.id.start_ride_btn, R.id.Track_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.Back_Btn_ex:
                finish();
                break;
            case R.id.Ride_name_tv:
                Open_Update_Ride_Name(this, RideNameTv.getText().toString());
                break;
            case R.id.end_my_ride:
                if (I_AM_ADMIN) {
                    hit_Ride_api(false, false, true, false, false, false);
                } else {
                    Constant.Show_Tos(this, "Only Admin Can End This Ride");
                }
                break;
            case R.id.addmedia_btn:
                Intent addmedia = new Intent(getApplicationContext(), Ride_Gallery.class);
                addmedia.putExtra("My_Ride_ID", My_Ride_ID);
                addmedia.putExtra("Admin_User_Id", Admin_User_Id);
                startActivity(addmedia);
                break;
            case R.id.addmembers_btn:
                if (I_AM_ADMIN) {
                    if (RIDE_STATUS.equalsIgnoreCase(RIDE_NOT_STARTED) || RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                        Intent Ride_Members_Management = new Intent(getApplicationContext(), Ride_Members_Management.class);
                        Ride_Members_Management.putExtra("My_Ride_ID", My_Ride_ID);
                        Ride_Members_Management.putExtra("Admin_User_Id", Admin_User_Id);
                        startActivity(Ride_Members_Management);
                    } else {
                        Constant.Show_Tos(getApplicationContext(), "Ride End So You Can't Access Member Management");
                    }
                } else {
                    Constant.Show_Tos(getApplicationContext(), "Only Admin Can Add Members To Ride");
                }
                break;
            case R.id.schedule_for_later_btn:
                //hit_Ride_api(false, false, false, true, false, false);
                break;
            case R.id.start_ride_btn:
                hit_Ride_api(false, true, false, false, false, false);
                break;
            case R.id.Track_record:
                Record_Btn();
                break;
            case R.id.track_location_btn:
                Fetch_My_Location();
                break;
        }
    }


    private void hit_Ride_api(boolean Update_Name, boolean Start_ride, boolean End_ride, boolean schedule, boolean My_Ride, boolean create_ride) {
        Call<ResponseBody> requestCall = null;
        String Ride_Name = "Ride" + Constant.generateRandomNumber();
        if (Start_ride) {
            show_ProgressDialog();
            requestCall = retrofitCallback.START_RIDE(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID(), GET_timeStamp(), Constant.getCompleteAddressString(Create_Group_Ride.this, Latitude_Start, Longitude_Start), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start), "1");
        } else if (End_ride) {
            show_ProgressDialog();
            requestCall = retrofitCallback.END_RIDE(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID(), GET_timeStamp(), Constant.getCompleteAddressString(Create_Group_Ride.this, Latitude_End, Longitude_End), String.valueOf(Latitude_End), String.valueOf(Longitude_End), "0");
        } else if (schedule) {
            show_ProgressDialog();

        } else if (Update_Name) {
            show_ProgressDialog();
            requestCall = retrofitCallback.Update_ride_name(My_Ride_ID, RideNameTv.getText().toString().trim());
        } else if (My_Ride) {
            show_ProgressDialog();
            requestCall = retrofitCallback.fetch_fetch_my_ride(new Session(Create_Group_Ride.this).get_LOGIN_USER_ID());
        } else if (create_ride) {
            Create_Ride_Flag = Create_Ride_Flag + 1;
            requestCall = retrofitCallback.create_bike_ride(new Session(this).get_LOGIN_USER_ID()
                    , Ride_Name, ShowMyLocation.getText().toString(), String.valueOf(Latitude_Start), String.valueOf(Longitude_Start));
        }

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
                        if (Start_ride) {
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Constant.Show_Tos(getApplicationContext(), "Ride Started Successfully");
                                //Ride Start
                                Change_Status(true, RIDE_STARTED, false, "");
                                hit_Ride_api(false, false, false, false, true, false);
                            } else {
                                Constant.Show_Tos(getApplicationContext(), "Ride Starting Process Failed");
                            }
                        } else if (End_ride) {
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Constant.Show_Tos(getApplicationContext(), "Ride Ended Successfully");
                                RIDE_STATUS = RIDE_ENDED;
                                //Ride Start
                                Change_Status(true, RIDE_ENDED, false, "");
                                hit_Ride_api(false, false, false, false, true, false);
                            } else {
                                Constant.Show_Tos(getApplicationContext(), "Ride Ending Process Failed");
                            }
                        } else if (My_Ride) {
                            MyRide_List.clear();
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                try {
                                    ParseRideResponse(jsonObject);
                                } catch (Exception e) {
                                }
                            } else {
                                Constant.Show_Tos(getApplicationContext(), "Fetching Ride Details Process Failed");
                            }
                        } else if (create_ride) {
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Constant.Show_Tos(getApplicationContext(), "Ride Created Successfully");
                                My_Ride_ID = jsonObject.getString("ride_id");
                                Change_Status(false, "", true, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID());
                                SetText("", "", Ride_Name);
                            } else {
                                Constant.Show_Tos(getApplicationContext(), "Ride Created Failed");
                            }
                        } else if (Update_Name) {
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Constant.Show_Tos(getApplicationContext(), "Ride Name Updated Successfully");
                            } else {
                                Constant.Show_Tos(getApplicationContext(), "Ride Name Update Failed");
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }

            private void ParseRideResponse(JSONObject jsonObject) throws JSONException {
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
                if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase(RIDE_NOT_STARTED)) {
                    Change_Status(true, RIDE_NOT_STARTED, false, "");
                } else if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase(RIDE_STARTED)) {
                    Change_Status(true, RIDE_STARTED, false, "");
                } else if (MyRide_List.get(0).getRide_Status().equalsIgnoreCase(RIDE_ENDED)) {
                    if (String.valueOf(MyRide_List.get(0).getSTARTLAT()).isEmpty() && String.valueOf(MyRide_List.get(0).getSTARTLAT()).equals(""))
                        Latitude_Start = Double.parseDouble(MyRide_List.get(0).getSTARTLAT());
                    if (String.valueOf(MyRide_List.get(0).getSTARTLANG()).isEmpty() && String.valueOf(MyRide_List.get(0).getSTARTLANG()).equals(""))
                        Longitude_Start = Double.parseDouble(MyRide_List.get(0).getSTARTLANG());
                    if (String.valueOf(MyRide_List.get(0).getENDLAT()).isEmpty() && String.valueOf(MyRide_List.get(0).getENDLAT()).equals(""))
                        Latitude_End = Double.parseDouble(MyRide_List.get(0).getENDLAT());
                    if (String.valueOf(MyRide_List.get(0).getENDLANG()).isEmpty() && String.valueOf(MyRide_List.get(0).getENDLANG()).equals(""))
                        Longitude_End = Double.parseDouble(MyRide_List.get(0).getENDLANG());
                    Change_Status(true, RIDE_ENDED, false, "");
                    String End_Address = Constant.getCompleteAddressString(getApplicationContext(), Latitude_End, Longitude_End);
                    SetText("", End_Address, "");
                }
                SetText("", "", MyRide_List.get(0).getRide_Name());
                if (MyRide_List.get(0).getADMIN_PLANNAME().equalsIgnoreCase("Premium Account")) {
                    ADMIN_PREMIUM_STATUS = true;
                } else {
                    ADMIN_PREMIUM_STATUS = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(), true, false);
                hide_ProgressDialog();
            }
        });
    }

    private void hit_update_My_Location(double mlattitude, double mlongitude) {
        Call<ResponseBody> requestCall = retrofitCallback.location_tracker(String.valueOf(mlattitude), String.valueOf(mlongitude), My_Ride_ID, new Session(this).get_LOGIN_USER_ID(), GET_timeStamp());
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void hit_update_ride_data(String Average_Speed, String Top_Speed, String Distance) {
        Call<ResponseBody> requestCall = retrofitCallback.my_ride_update(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID(),
                Average_Speed, Top_Speed, Distance, Constant.GET_timeStamp());
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void hit_member_location_fetch(String My_Ride_ID, String Member_ID) {
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
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            team_mate_Loc.clear();
                            JSONArray TRACKDETAILS = jsonObject.getJSONArray("TRACKDETAILS");
                            for (int i = 0; i < TRACKDETAILS.length(); i++) {
                                JSONObject JS = TRACKDETAILS.getJSONObject(i);
                                Real_Time_Latlong real_time_latlong = new Real_Time_Latlong();
                                real_time_latlong.setRider_Name(JS.getString("FULL_NAME"));
                                real_time_latlong.setMEMBERID(JS.getString("MEMBERID"));
                                real_time_latlong.setLongitude_Start(Double.parseDouble(JS.getString("LONGTIUDE")));
                                real_time_latlong.setLatitude_Start(Double.parseDouble(JS.getString("LATITUDE")));
                                team_mate_Loc.add(real_time_latlong);
                            }
                            try {
                                Members_Live_Location(team_mate_Loc);
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


    //LifeCircle Method
    @Override
    protected void onResume() {
        super.onResume();
        Reload_UI();
        if (location_track_service != null) {
            try {
                location_track_service.execute();
            } catch (IllegalStateException e) {
            } catch (Exception e) {
            }
            Auto_Hide_ToolTip();
        } else {
            try {
                new Location_Track_Service().execute();
            } catch (IllegalStateException e) {
            } catch (Exception e) {
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (location_track_service != null) {
                if (location_track_service.getStatus() == AsyncTask.Status.FINISHED) {
                    location_track_service.execute();
                }
            } else {
                new Location_Track_Service().execute();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Location_Shearing_Service = true;
        } else {
            Location_Shearing_Service = false;
        }
    }

    void RideData_analytics(String Distance) {
        if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
            try {
                averageSpeedTv.setText(new DecimalFormat("##.##").format(Double.parseDouble(testAdapter.GET_AVERAGE_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()))) + "km/h");
                topSpeedTv.setText(new DecimalFormat("##.##").format(Double.parseDouble(testAdapter.GET_TOP_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()))) + "km/h");
            } catch (Exception e) {
            }
            distanceCoveredTv.setText(Distance + " km");
            try {


                Double Total_Time = Double.parseDouble(Distance) / Double.parseDouble(testAdapter.GET_AVERAGE_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()));
                Double Tota_Secound = Total_Time * 3600;
                Double hours =  Tota_Secound / 3600;
                Double minutes = (Tota_Secound % 3600) / 60;
                Double seconds = Tota_Secound % 60;
                rideTimeTv.setText(new DecimalFormat("##").format(hours)+":"+new DecimalFormat("##").format(minutes)+":"+new DecimalFormat("##").format(seconds));

            } catch (Exception e) {
            }


            if (averageSpeedTv.getText().toString() != null) {
                hit_update_ride_data(averageSpeedTv.getText().toString().replaceAll("km/h", "").trim(),
                        topSpeedTv.getText().toString().replaceAll("km/h", "").trim(), Distance);
            }
        } else if (RIDE_STATUS.equalsIgnoreCase(RIDE_ENDED)) {
            try {
                averageSpeedTv.setText(new DecimalFormat("##.##").format(Double.parseDouble(testAdapter.GET_AVERAGE_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()))) + "km/h");
                topSpeedTv.setText(new DecimalFormat("##.##").format(Double.parseDouble(testAdapter.GET_TOP_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()))) + "km/h");
            } catch (Exception e) {
            }
            distanceCoveredTv.setText(Distance + " km");
            try {

                Double Total_Time = Double.parseDouble(Distance) / Double.parseDouble(testAdapter.GET_AVERAGE_SPEED(My_Ride_ID, new Session(Create_Group_Ride.this).get_LOGIN_USER_ID()));
                Double Tota_Secound = Total_Time * 3600;
                Double hours =  Tota_Secound / 3600;
                Double minutes = (Tota_Secound % 3600) / 60;
                Double seconds = Tota_Secound % 60;
                rideTimeTv.setText(new DecimalFormat("##").format(hours)+":"+new DecimalFormat("##").format(minutes)+":"+new DecimalFormat("##").format(seconds));
            } catch (Exception e) {
            }
        }
    }


    public class Location_Track_Service extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            currentLatitude = Double.parseDouble(intent.getStringExtra(LocationTrackerService.EXTRA_LATITUDE));
                            currentLongitude = Double.parseDouble(intent.getStringExtra(LocationTrackerService.EXTRA_LONGITUDE));
                            Latitude_End = currentLatitude;
                            Longitude_End = currentLongitude;
                            if (My_Ride_ID != null) {
                                if (!My_Ride_ID.equalsIgnoreCase("")) {
                                    if (Track_My_Location) {
                                        hit_update_My_Location(currentLatitude, currentLongitude);
                                    }
                                }
                            }
                            if (RIDE_STATUS.equalsIgnoreCase(RIDE_STARTED)) {
                                if (Track_My_Location) {
                                    testAdapter.INSERT_REALTIMELOCATION(My_Ride_ID, new Session(getApplicationContext()).get_LOGIN_USER_ID(),
                                            String.valueOf(currentLatitude), String.valueOf(currentLongitude), GET_timeStamp());
                                    double Real_Time_Speed = Double.parseDouble(intent.getStringExtra(LocationTrackerService.EXTRA_SPEED));//meters/second
                                    double Real_Time_Speed_kmph = Real_Time_Speed * 3.6;
                                    String Distance = DistanceCalculator.distance(Latitude_Start, Longitude_Start, currentLatitude, currentLongitude);
                                    RideData_analytics(Distance);
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
        }
    }


}