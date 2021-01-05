package com.immortal.vehicletracking.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.immortal.vehicletracking.helperClass.ApiConstants;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import static com.immortal.vehicletracking.helperClass.ApiConstants.LIVE_TRACKING;


public class MyService extends Service {
    private static final float LOCATION_DISTANCE = 0.0f;
    private static final int LOCATION_INTERVAL = 2000;
    private static final String TAG = "Update_Service";
    Context context;
    Location mLastLocation;
    LocationListener[] mLocationListeners = {new LocationListener("gps"), new LocationListener("network")};
    private LocationManager mLocationManager = null;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    private class LocationListener implements android.location.LocationListener {
        public LocationListener(String provider) {
            StringBuilder sb = new StringBuilder();
            sb.append("LocationListener ");
            sb.append(provider);
            Log.e(MyService.TAG, sb.toString());
            MyService.this.mLastLocation = new Location(provider);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(MyService.this.mLastLocation.getLatitude());
            sb2.append("");
            Log.e(FirebaseAnalytics.Param.LOCATION, sb2.toString());
        }

        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("onLocationChanged: ");
            sb.append(location);
            Log.e(MyService.TAG, sb.toString());
            MyService.this.mLastLocation.set(location);
            UserPreferences userPreferences = MyService.this.userPreferences;
            Context context = MyService.this.context;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(location.getLatitude());
            String str = "";
            sb2.append(str);
            userPreferences.setString(context, Constant.Lat, sb2.toString());
            UserPreferences userPreferences2 = MyService.this.userPreferences;
            Context context2 = MyService.this.context;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(location.getLongitude());
            sb3.append(str);
            userPreferences2.setString(context2, Constant.Long, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(location.getLatitude());
            sb4.append(str);
            Log.e("location", location.getLatitude() + "");
        }

        public void onProviderDisabled(String provider) {
            if (provider.equals("gps")) {
                Log.e("Disabled: ", provider);
            }
        }

        public void onProviderEnabled(String provider) {
            StringBuilder sb = new StringBuilder();
            sb.append("onProviderEnabled: ");
            sb.append(provider);
            Log.e(MyService.TAG, sb.toString());
            if (provider.equals("gps")) {
                Log.e("Disabled: ", provider);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            StringBuilder sb = new StringBuilder();
            sb.append("onStatusChanged: ");
            sb.append(provider);
            Log.e(MyService.TAG, sb.toString());
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void onCreate() {
        String str = "gps";
        String str2 = "network";
        String str3 = "fail to request location update, ignore";
        String str4 = TAG;
        Log.e(str4, "onCreate");
        this.context = this;
        initializeLocationManager();
        try {
            this.mLocationManager.requestLocationUpdates("network", 2000, 0.0f, this.mLocationListeners[1]);
            Log.e(str2, str2);
        } catch (SecurityException ex) {
            Log.i(str4, str3, ex);
        } catch (IllegalArgumentException ex2) {
            StringBuilder sb = new StringBuilder();
            sb.append("network provider does not exist, ");
            sb.append(ex2.getMessage());
            Log.d(str4, sb.toString());
        }
        try {
            this.mLocationManager.requestLocationUpdates("gps", 2000, 0.0f, this.mLocationListeners[0]);
            Log.e(str, str);
        } catch (SecurityException ex3) {
            Log.i(str4, str3, ex3);
        } catch (IllegalArgumentException ex4) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("gps provider does not exist ");
            sb2.append(ex4.getMessage());
            Log.d(str4, sb2.toString());
        }
    }

    public void onDestroy() {
        String str = "stop";
        String str2 = "stop->>>";
        Log.e(str2, str);
        String str3 = TAG;
        Log.e(str3, "onDestroy");
        super.onDestroy();
        if (this.mLocationManager != null) {
            int i = 0;
            while (true) {
                LocationListener[] locationListenerArr = this.mLocationListeners;
                if (i >= locationListenerArr.length) {
                    break;
                }
                try {
                    this.mLocationManager.removeUpdates(locationListenerArr[i]);
                } catch (Exception ex) {
                    Log.i(str3, "fail to remove location listners, ignore", ex);
                }
                i++;
            }
        }
        Log.e(str2, str);
    }

    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void dayInStatus(double latitude, double longitude) {
        String str = "";
        JSONObject postparams = new JSONObject();
        String str2 = Constant.Lat;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(latitude);
            sb.append(str);
            postparams.put(str2, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(longitude);
            sb2.append(str);
            postparams.put("lng", sb2.toString());
            postparams.put("case_id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Param", postparams.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, ApiConstants.LIVE_TRACKING, postparams, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                StringBuilder sb = new StringBuilder();
                sb.append(response);
                sb.append("");
                Log.e("LIVE_TRACKING->>", sb.toString());
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error: ");
                sb.append(error.getMessage());
                Log.e("Update_ServiceLIVE_TRAC", sb.toString());
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}