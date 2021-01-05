package com.immortal.vehicletracking.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.adapter.TotalListAdapter;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;


public class SyncService extends Service {


    private Handler mHandler;
    // default interval for syncing data
    public static final long DEFAULT_SYNC_INTERVAL = 3 * 1000;

    // task to be run here
    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            syncData();
            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create the Handler object
        mHandler = new Handler();
        // Execute a runnable task as soon as possible
        mHandler.post(runnableService);

        return START_STICKY;
    }

    private synchronized void syncData() {
        // call your rest service here
        Log.e("start", "start");
       // VehicleDetailsApiCall("58");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private void VehicleDetailsApiCall(final String user_id) {
//        JSONObject postparams = new JSONObject();
//        try {
//            postparams.put("tracking_device", user_id);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e("postparams", postparams + "");
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VAHICLE_DETAILS, postparams,
//                new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        MyProgressDialog.hidePDialog();
//                        Log.e("TotalListScreen", response + "");
//                        try {
//                            if (response.getString("status").equals("true")) {
//                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
//                                JSONObject data = response.getJSONObject("data");
//
//                                setdatatoView(data);
//
//                            } else {
////                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
////                                new CustomToast().Show_Toast(context, view,
////                                        response.getString("message"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TotalListScreen", "Error: " + error.getMessage());
//                        MyProgressDialog.hidePDialog();
//                    }
//                }
//        );
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
//
//    }

    private void setdatatoView(JSONObject data) {
        Log.e("start", data.toString());

    }
}