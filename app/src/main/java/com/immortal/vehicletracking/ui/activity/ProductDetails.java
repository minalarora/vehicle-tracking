package com.immortal.vehicletracking.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.service.MyService;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.SHARE_URL;
import static com.immortal.vehicletracking.helperClass.ApiConstants.UPDATE_DRIVER;
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_DETAILS;
import static com.immortal.vehicletracking.utils.Utils.ac_alarm_status;

public class ProductDetails extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private GoogleMap mMap;
    private LinearLayout layoutBottomSheet;
    private Dialog dialog;
    private BottomSheetBehavior sheetBehavior;
    String[] hours = {"1 Hour", "2 Hour", "3 Hour", "4 Hour", "5 Hour", "6 Hour", "7 Hour"};
    private Context context;
    private TextView v_d_speed_txt, d_last_stop_txt, total_km_txt, park_last_stop, total_parking,
            duration_last_stop, total_duration, curr_location_txt;
    private TextView map_type_txt;
    private String vehicle_id;
    private Handler mHandler;
    private ImageView iv_mapSetting;
    private String speed_status;
    private TextView txt_driven_today, txt_odometer;
    private String immobilize_status;

    // default interval for syncing map_type_txtdata
    public static final long DEFAULT_SYNC_INTERVAL = 3 * 1000;
    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            syncData();
            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };
    Marker mPositionMarker;
    private Location location;
    CameraPosition cameraPosition;
    int i = 0;
    private double check_lat = 0;
    private double check_lng = 0;
    private ImageView ignition_icon_img, gps_icon_img, battery_icon_img, fuel_icon_img, ac_icon_img;
    private Toolbar toolbar;
    private String driver_name;
    private String driver_phone;
    private String driver_id;
    protected PowerManager.WakeLock mWakeLock;
    private double curr_lat_navigation, curr_lng_navigation;
    private Intent intent;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private String vehicle_name;
    private String vehicle_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ProductDetails.this;
        initId();
        getdatafromIntent();
        setupToolBar();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My:Tag");
        this.mWakeLock.acquire();
        startBackgroundService();
//        VehicleDetailsApiCall(vehicle_id);

        MyProgressDialog.showPDialog(context);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setPeekHeight(170);
        sheetBehavior.setHideable(false);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        map_type_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, map_type_txt);
                popup.inflate(R.menu.map_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_map_1:
                                // UpdateETADialog();
                                Toast.makeText(context, "popup", Toast.LENGTH_SHORT).show();
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case R.id.menu_map_2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                                break;
                            case R.id.menu_map_3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    private void startBackgroundService() {
        if (!isMyServiceRunning()) {
            Log.e("booton", "on1");
            if (Build.VERSION.SDK_INT >= 26) {
                startService(new Intent(this.context, MyService.class));
            } else {
                startService(new Intent(this.context, MyService.class));
            }
        }
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray));
        setSupportActionBar(toolbar);
    }

    private synchronized void syncData() {
        // call your rest service here
        Log.e("start", "start");
        VehicleDetailsApiCall(vehicle_id);

    }

    private void LiveLocation() {
        mHandler = new Handler();
        // Execute a runnable task as soon as possible
        mHandler.post(runnableService);
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(runnableService);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LiveLocation();
        super.onResume();
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            vehicle_id = getIntent().getStringExtra("id");
        }
    }

    private void initId() {
        toolbar = findViewById(R.id.toolbar);
        v_d_speed_txt = findViewById(R.id.v_d_speed_txt);
        d_last_stop_txt = findViewById(R.id.d_last_stop_txt);
        total_km_txt = findViewById(R.id.total_km_txt);
        park_last_stop = findViewById(R.id.park_last_stop);
        total_parking = findViewById(R.id.total_parking);
        duration_last_stop = findViewById(R.id.duration_last_stop);
        total_duration = findViewById(R.id.total_duration);
        curr_location_txt = findViewById(R.id.curr_location_txt);
        map_type_txt = findViewById(R.id.map_type_txt);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        iv_mapSetting = findViewById(R.id.iv_mapSetting);
        ignition_icon_img = findViewById(R.id.ignition_icon_img);
        gps_icon_img = findViewById(R.id.gps_icon_img);
        battery_icon_img = findViewById(R.id.battery_icon_img);
        fuel_icon_img = findViewById(R.id.fuel_icon_img);
        ac_icon_img = findViewById(R.id.ac_icon_img);
        txt_driven_today = findViewById(R.id.txt_driven_today);
        txt_odometer = findViewById(R.id.txt_odometer);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(25);

//        mMap.setMyLocationEnabled(true);
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(28.7041, 77.1025);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Delhi"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(sydney)
//                .zoom(12)
//                .build();
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void setdatatoView(JSONObject data) {
        try {
            String speed = data.isNull("speed") ? "" : data.getString("speed");
            String distance_last_stop = data.isNull("distance_from_last_stop") ? "" : data.getString("distance_from_last_stop");
            String total_km = data.isNull("total_distance") ? "" : data.getString("total_distance");
            String parking_dur_last = data.isNull("parking_last_duration") ? "" : data.getString("parking_last_duration");
            String total_parking_dur = data.isNull("parking_total_duration") ? "" : data.getString("parking_total_duration");
            String dur_last_stop = data.isNull("duration_from_last_stop") ? "" : data.getString("duration_from_last_stop");
            String total_distance_dur = data.isNull("total_duration") ? "" : data.getString("total_duration");
            String curr_location = data.isNull("address") ? "" : data.getString("address");
            double curr_lat = data.isNull("lat") ? 0 : data.getDouble("lat");
            double curr_lng = data.isNull("lng") ? 0 : data.getDouble("lng");
            curr_lat_navigation = curr_lat;
            curr_lng_navigation = curr_lng;
            vehicle_no = data.isNull("vehicle_no") ? "" : data.getString("vehicle_no");

            String vehicle_meter = data.isNull("vehicle_meter") ? "" : data.getString("vehicle_meter");
            String static_time = data.isNull("static_time") ? "" : data.getString("static_time");
            String running_time = data.isNull("running_time") ? "" : data.getString("running_time");
            String driven_today = data.isNull("driven_today") ? "" : data.getString("driven_today");

            String oil_electricity_alarm = data.isNull("oil_electricity_alarm") ? "" : data.getString("oil_electricity_alarm");
            String gps_tracking_alarm = data.isNull("gps_tracking_alarm") ? "" : data.getString("gps_tracking_alarm");
            String shock_alarm = data.isNull("shock_alarm") ? "" : data.getString("shock_alarm");
            String charging_alarm = data.isNull("charging_alarm") ? "" : data.getString("charging_alarm");
            String ac_alarm = data.isNull("ac_alarm") ? "0" : data.getString("ac_alarm");
            String defence_alarm = data.isNull("defence_alarm") ? "" : data.getString("defence_alarm");
            String voltage_alarm = data.isNull("voltage_alarm") ? "" : data.getString("voltage_alarm");
            String gsm_signal_alarm = data.isNull("gsm_signal_alarm") ? "" : data.getString("gsm_signal_alarm");

            driver_name = data.isNull("driver_name") ? "" : data.getString("driver_name");
            driver_id = data.isNull("drive_id") ? "" : data.getString("drive_id");
            driver_phone = data.isNull("driver_phone") ? "" : data.getString("driver_phone");

            vehicle_name = data.isNull("device_name") ? "" : data.getString("device_name");

            speed_status = speed.substring(0, 1);
            v_d_speed_txt.setText(speed);
            d_last_stop_txt.setText(distance_last_stop);
            total_km_txt.setText(total_km);
            park_last_stop.setText(parking_dur_last);
            total_parking.setText(total_parking_dur);
            duration_last_stop.setText(static_time);
            total_duration.setText(running_time);
            curr_location_txt.setText(curr_location);

            txt_driven_today.setText(driven_today);
            txt_odometer.setText(vehicle_meter);
            int ac_status = ac_alarm_status(ac_alarm);
            ac_icon_img.setImageResource(ac_status);
            toolbar.setTitle(vehicle_name);

            if (gps_tracking_alarm.equals("GPS_TRACKING_ON")) {
                gps_icon_img.setImageResource(R.drawable.gps_icon_green);
            } else if (gps_tracking_alarm.equals("GPS_TRACKING_OFF")) {
                gps_icon_img.setImageResource(R.drawable.gps_icon_red);
            } else {
                gps_icon_img.setImageResource(R.drawable.gps_icon_red);
            }
            if (oil_electricity_alarm.equals("GAS_OIL_AND_ELECTRICITY_CONNECTED")) {
                immobilize_status = "on";
                fuel_icon_img.setImageResource(R.drawable.fuel_icon_green);
            } else if (oil_electricity_alarm.equals("OIL_AND_ELECTRICITY_DISCONNECTED")) {
                immobilize_status = "off";
                fuel_icon_img.setImageResource(R.drawable.fuel_icon_red);
            } else {
                immobilize_status = "on";
                fuel_icon_img.setImageResource(R.drawable.fuel_icon_red);
            }

            if (charging_alarm.equals("CHARGE_ON")) {
                battery_icon_img.setImageResource(R.drawable.battery_icon_green);
            } else if (charging_alarm.equals("CHARGE_OFF")) {
                battery_icon_img.setImageResource(R.drawable.battery_icon_red);
            } else {
                battery_icon_img.setImageResource(R.drawable.battery_icon_red);
            }
            if (gsm_signal_alarm.equals("STRONG_SIGNAL")) {
                ignition_icon_img.setImageResource(R.drawable.ignition_icon_green);
            } else if (gsm_signal_alarm.equals("GOOD_SIGNAL")) {
                ignition_icon_img.setImageResource(R.drawable.ignition_icon_red);
            } else {
                ignition_icon_img.setImageResource(R.drawable.ignition_icon_red);
            }

            Log.e("Speed->", speed.substring(0, 1));
            LatLng sydney = new LatLng(curr_lat, curr_lng);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Delhi")
//                    .anchor(0.5f, 0.5f)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
            Log.e("temp_2", curr_lat + "");

            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(curr_lat);
            temp.setLongitude(curr_lng);
//            float distance = location.distanceTo(temp);
            Log.e("temp_2", temp.getLatitude() + "");
            if (mPositionMarker == null) {
                int drawable;
                if (speed_status.equals("0")) {
                    drawable = R.drawable.ic_car_red;
                } else {
                    drawable = R.drawable.ic_car_green;

                }
                mPositionMarker = mMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .icon(BitmapDescriptorFactory
                                .fromResource(drawable))
                        .anchor(0.5f, 0.5f)
                        .position(
                                new LatLng(curr_lat, curr_lng)));
            }
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(sydney)
//                    .zoom(15)
//                    .build();
//
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

//            if (i == 0) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curr_lat, curr_lng), 18.0f));

//                i++;
//            }

//            mPositionMarker.setRotation(bearingBetweenLocations(mPositionMarker.getPosition(), new LatLng(curr_lat, curr_lng)));
            if (check_lat != curr_lat) {
                animateMarker(mPositionMarker, temp);
                check_lat = curr_lat;
                Log.e("check->>", "if");
                // Helper method for smooth
            } else {
                Log.e("check->>", "else");
            }
            // animation

            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(curr_lat, curr_lng)));
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(sydney)
//                    .zoom(12)
//                    .build();
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //    private void animateMarkerNew(final Location destination, final Marker marker) {
//
//        if (marker != null) {
//
//            final LatLng startPosition = marker.getPosition();
//            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
//
//            final float startRotation = marker.getRotation();
//            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();
//
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//            valueAnimator.setDuration(3000); // duration 3 second
//            valueAnimator.setInterpolator(new LinearInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    try {
//                        float v = animation.getAnimatedFraction();
//                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
//                        marker.setPosition(newPosition);
//                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                                .target(newPosition)
//                                .zoom(15.5f)
//                                .build()));
//
//                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));
//                    } catch (Exception ex) {
//                        //I don't care atm..
//                    }
//                }
//            });
//            valueAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//
//                    // if (mMarker != null) {
//                    // mMarker.remove();
//                    // }
//                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
//
//                }
//            });
//            valueAnimator.start();
//        }
//    }
//
    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                        .target(new LatLng(lat, lng))
//                        //.zoom(15.5f)
//                        .build()));
                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(bearingBetweenLocations(mPositionMarker.getPosition(), new LatLng(location.getLatitude(), location.getLongitude())));


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private float bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return (float) brng;
    }
//    private void rotateMarker(final Marker marker, final float toRotation) {
//        if(!isMarkerRotating) {
//            final Handler handler = new Handler();
//            final long start = SystemClock.uptimeMillis();
//            final float startRotation = marker.getRotation();
//            final long duration = 1000;
//
//            final Interpolator interpolator = new LinearInterpolator();
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    isMarkerRotating = true;
//
//                    long elapsed = SystemClock.uptimeMillis() - start;
//                    float t = interpolator.getInterpolation((float) elapsed / duration);
//
//                    float rot = t * toRotation + (1 - t) * startRotation;
//
//                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
//                    if (t < 1.0) {
//                        // Post again 16ms later.
//                        handler.postDelayed(this, 16);
//                    } else {
//                        isMarkerRotating = false;
//                    }
//                }
//            });
//        }
//    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void calltoCustomer(View view) {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_driver_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        TextView cancel = dialog.findViewById(R.id.cancel_port);
        TextView save = dialog.findViewById(R.id.save_port);
        TextView tv_driver_name = dialog.findViewById(R.id.tv_driver_name);
        TextView tv_driver_number = dialog.findViewById(R.id.tv_driver_number);
        FloatingActionButton fb_call = dialog.findViewById(R.id.fb_call);
        tv_driver_name.setText(driver_name + "");
        tv_driver_number.setText(driver_phone + "");
        fb_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetails.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + driver_phone + ""));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(context, EditDriverActivity.class);
                intent.putExtra("id", driver_id + "");
                intent.putExtra("name", driver_name + "");
                intent.putExtra("number", driver_phone + "");
                intent.putExtra("vehicle_id", vehicle_id + "");
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public void sharelocation(View view) {
        Toast.makeText(ProductDetails.this, "clicked", Toast.LENGTH_SHORT).show();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_share_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        TextView cancel = dialog.findViewById(R.id.cancel_port);
        TextView save = dialog.findViewById(R.id.save_port);

        Spinner spin = dialog.findViewById(R.id.shareLocation_spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hours);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                shareLocationApiCall(userPreferences.getString(context, Constant.USER_ID), vehicle_id, "2");
            }
        });
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void VehicleDetailsApiCall(final String user_id) {
//        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("tracking_device", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VAHICLE_DETAILS, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONObject data = response.getJSONObject("data");

                                setdatatoView(data);

                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
//                                new CustomToast().Show_Toast(context, view,
//                                        response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TotalListScreen", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    public void shareLocation(View view) {
        String myLat = this.userPreferences.getString(this.context, Constant.Lat);
        String myLong = this.userPreferences.getString(this.context, Constant.Long);
        StringBuilder sb = new StringBuilder();
        String str = "http://maps.google.com/maps?saddr=";
        sb.append(str);
        sb.append(myLat);
        String str2 = ",";
        sb.append(str2);
        sb.append(myLong);
        sb.append("&daddr=");
        sb.append(this.curr_lat_navigation);
        sb.append(str2);
        sb.append(this.curr_lng_navigation);
        String location2 = sb.toString();
        Double latitude = Double.valueOf(this.curr_lat_navigation);
        Double longitude = Double.valueOf(this.curr_lng_navigation);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(latitude);
        sb2.append(str2);
        sb2.append(longitude);
        sb2.append("&mode=d");
        String sb3 = sb2.toString();
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra("android.intent.extra.SUBJECT", "Here is my location");
        sharingIntent.putExtra("android.intent.extra.TEXT", location2);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    public void startNavigation(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + curr_lat_navigation + "," + curr_lng_navigation + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void gotoImmobilize(View view) {
        intent = new Intent(context, ImmobilizeActivity.class);
        intent.putExtra("id", vehicle_id);
        intent.putExtra("status", immobilize_status);
        startActivity(intent);
    }

    public void gotoDocumentList(View view) {
        this.intent = new Intent(this.context, DocumentListActivity.class);
        this.intent.putExtra("id", this.vehicle_no);
        this.intent.putExtra("name", vehicle_name);
        startActivity(this.intent);
    }

    public void gotoVehicle(View view) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curr_lat_navigation, curr_lng_navigation), 18.0f));
    }

    public void changeMap(View view) {
        PopupMenu popup = new PopupMenu(context, iv_mapSetting);
        popup.inflate(R.menu.map_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_map_1:
                        // UpdateETADialog();
                        Toast.makeText(context, "popup", Toast.LENGTH_SHORT).show();
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.menu_map_2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        break;
                    case R.id.menu_map_3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void shareLocationApiCall(String user_id, String device_id, String interval) {
        MyProgressDialog.showPDialog(context);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("user", user_id);
            postparams.put("device", device_id);
            postparams.put("interval", interval);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SHARE_URL, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Share Location", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                MyProgressDialog.hidePDialog();
                                String share_url = response.getString("share_url");
                                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Mr.");
                                i.putExtra(android.content.Intent.EXTRA_TEXT, share_url);
                                startActivity(Intent.createChooser(i, "Share via"));
                            } else {
                                MyProgressDialog.hidePDialog();
                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyProgressDialog.hidePDialog();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Share Location", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
