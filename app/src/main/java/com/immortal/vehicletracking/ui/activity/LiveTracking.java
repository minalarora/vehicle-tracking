package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.clusterRenderer.MarkerClusterRenderer;
import com.immortal.vehicletracking.model.ClusterData;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.GoogleMapHelper;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.LIVE_TRACKING;

public class LiveTracking extends AppCompatActivity {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    private Marker marker1, marker2, marker3, marker4;
    private Context context;
    List<ClusterData> items = new ArrayList<>();
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    private LatLng[] lat = new LatLng[]{new LatLng(28.616492, 77.241791), new LatLng(28.599640, 77.200986),
            new LatLng(28.635165, 77.198600), new LatLng(28.655366, 77.212488), new LatLng(28.649429, 77.282340),
            new LatLng(28.575569, 77.317591), new LatLng(28.559586, 77.185526), new LatLng(28.635847, 77.147012),
            new LatLng(28.610076, 77.152584), new LatLng(28.566542, 77.156574), new LatLng(28.510129, 77.223984),
            new LatLng(28.508695, 77.114188), new LatLng(28.459316, 77.152151), new LatLng(28.442290, 77.288398),
            new LatLng(28.571466, 77.427808), new LatLng(28.480763, 77.365789), new LatLng(28.445548, 77.347343)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = LiveTracking.this;
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//        );
        setContentView(R.layout.activity_live_tracking);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            GoogleMapHelper.defaultMapSettings(googleMap);
//            setUpClusterManager(googleMap);
            LiveTrackingApiCall(userPreferences.getString(context, Constant.USER_ID), googleMap);
        });
    }

    private void setUpClusterManager(GoogleMap googleMap) {
        // LiveTrackingApiCall(common_account_id);

        ClusterManager<ClusterData> clusterManager = new ClusterManager<>(this, googleMap);
        MarkerClusterRenderer markerClusterRenderer = new MarkerClusterRenderer(this, googleMap, clusterManager);
        clusterManager.setRenderer(markerClusterRenderer);


//        List<ClusterData> items = getItems();
        // items = getItems();

        clusterManager.addItems(items);
        clusterManager.cluster();
        googleMap.isMyLocationEnabled();
//        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//        googleMap.setMyLocationEnabled(true);

//        LatLng sydney = new LatLng(28.616492, 77.241791);
////        googleMap.addMarker(new MarkerOptions().position(sydney));
////        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(sydney)
//                .zoom(9)
//                .build();
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void LiveTrackingApiCall(final String user_id, GoogleMap googleMap) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LIVE_TRACKING, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("data");

                                setdatatoView(data, googleMap);
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


    private void setdatatoView(JSONArray data, GoogleMap googleMap) {

        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                Double device_lat = list.isNull("device_lat") ? 0 : list.getDouble("device_lat");
                Double device_lng = list.isNull("device_lng") ? 0 : list.getDouble("device_lng");
                String id = list.isNull("id") ? "" : list.getString("id");
//                String current_time = list.isNull("current_time") ? "" : list.getString("current_time");
                if (i == 0) {
                    LatLng sydney = new LatLng(device_lat, device_lng);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney)//wrong
                            .zoom(9)
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                ClusterData vehicleStatus = new ClusterData(vehicle_no, new LatLng(device_lat, device_lng), vehicle_no, id);
                items.add(vehicleStatus);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setUpClusterManager(googleMap);

    }


//    private List<ClusterData> getItems() {
////        LiveTrackingApiCall(common_account_id);
//        Log.e("ClusterData", "ClusterData");
//        return Arrays.asList(
//                new ClusterData("test_name", new LatLng(28.616492, 77.241791), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.599640, 77.200986), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.635165, 77.198600), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.655366, 77.212488), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.649429, 77.282340), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.575569, 77.317591), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.559586, 77.185526), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.635847, 77.147012), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.610076, 77.152584), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.566542, 77.156574), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.510129, 77.223984), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.508695, 77.114188), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.459316, 77.152151), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.442290, 77.288398), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.571466, 77.427808), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.480763, 77.365789), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.445548, 77.347343), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.664406, 77.390696), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.597170, 77.491762), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.755512, 77.262939), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.755512, 77.262345), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.755512, 77.438987), "HR 26 DK 8337", ""),
//                new ClusterData("test_name", new LatLng(28.755512, 77.463252), "HR 26 DK 8337", "")
//        );
//    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}

