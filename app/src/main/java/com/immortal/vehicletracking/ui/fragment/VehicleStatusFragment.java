package com.immortal.vehicletracking.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.VehicleStatusAdapter;
import com.immortal.vehicletracking.model.VehicleStatus;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.ui.activity.ProductDetails;
import com.immortal.vehicletracking.utils.CenterZoomLayoutManager;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_STATUS;

public class VehicleStatusFragment extends Fragment implements VehicleStatusAdapter.News_OnItemClicked {


    private RecyclerView recyclerView;
    private VehicleStatusAdapter adapter;
    private List<VehicleStatus> vehicleStatusList;
    private VehicleStatus vehicleStatus;
    private Intent intent;
    private View view;
    private GridLayoutManager mLayoutManager;
    private Context context;
    private EditText sBarET_vStatus;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private int gps_tracking_alarm_int;
    private int oil_electricity_alarm_int;
    private int ac_alarm_int;
    private int charging_alarm_int;

    public VehicleStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vehicle_status, container, false);
        context = getContext();
        initId();
        setRecyclerViewCode();

        MyProgressDialog.showPDialog(context);
        VehicleStatusApiCall(userPreferences.getString(context, Constant.USER_ID), "");

        sBarET_vStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                VehicleStatusApiCall(userPreferences.getString(context, Constant.USER_ID), s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void setdatatoView(JSONArray data) {
        String vehicle_icon = null;
        String oil_electricity_alarm;
        String gps_tracking_alarm;
        String charging_alarm;
        String ac_alarm;
        vehicleStatusList.clear();
        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String max_speed = list.isNull("max_speed") ? "" : list.getString("max_speed");
                String vehicle_id = list.isNull("id") ? "" : list.getString("id");
                String date = list.isNull("activated_date") ? "" : list.getString("activated_date");
                String current_time = list.isNull("current_time") ? "" : list.getString("current_time");
                String address = list.isNull("device_current_address") ? "" : list.getString("device_current_address");
//                String location = list.isNull("activated_date") ? "" : list.getString("activated_date");
//                String current_time = list.isNull("current_time") ? "" : list.getString("current_time");
                oil_electricity_alarm = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
                gps_tracking_alarm = list.isNull("gps_tracking_alarm") ? "" : list.getString("gps_tracking_alarm");
                ac_alarm = list.isNull("ac_alarm") ? "" : list.getString("ac_alarm");
                charging_alarm = list.isNull("charging_alarm") ? "" : list.getString("charging_alarm");
                vehicle_icon = list.isNull("vehicle_icon") ? "" : list.getString("vehicle_icon");


                if (gps_tracking_alarm.equals("GPS_TRACKING_ON")) {
                    this.gps_tracking_alarm_int = R.drawable.gps_icon_green;
                } else if (gps_tracking_alarm.equals("GPS_TRACKING_OFF")) {
                    this.gps_tracking_alarm_int = R.drawable.gps_icon_red;
                } else {
                    this.gps_tracking_alarm_int = R.drawable.gps_icon_red;
                }
                if (oil_electricity_alarm.equals("GAS_OIL_AND_ELECTRICITY_CONNECTED")) {
                    this.oil_electricity_alarm_int = R.drawable.fuel_icon_green;
                } else if (oil_electricity_alarm.equals("OIL_AND_ELECTRICITY_DISCONNECTED")) {
                    this.oil_electricity_alarm_int = R.drawable.fuel_icon_red;
                } else {
                    this.oil_electricity_alarm_int = R.drawable.fuel_icon_red;
                }
                if (charging_alarm.equals("CHARGE_ON")) {
                    this.charging_alarm_int = R.drawable.battery_icon_green;
                } else if (charging_alarm.equals("CHARGE_OFF")) {
                    this.charging_alarm_int = R.drawable.battery_icon_red;
                } else {
                    this.charging_alarm_int = R.drawable.battery_icon_red;
                }
                if (ac_alarm.equals("AC_HIGH")) {
                    this.ac_alarm_int = R.drawable.ac_icon_green;
                } else if (ac_alarm.equals("AC_LOW")) {
                    this.ac_alarm_int = R.drawable.ac_icon_red;
                } else {
                    this.ac_alarm_int = R.drawable.ac_icon_red;
                }

                vehicleStatus = new VehicleStatus(vehicle_no, vehicle_icon, max_speed, date, current_time, address, vehicle_id, oil_electricity_alarm_int, ac_alarm_int, charging_alarm_int, gps_tracking_alarm_int);
                vehicleStatusList.add(vehicleStatus);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initId() {
        recyclerView = view.findViewById(R.id.recyclerview_v_status);
        sBarET_vStatus = view.findViewById(R.id.sBarET_vStatus);
    }

    private void setRecyclerViewCode() {
        vehicleStatusList = new ArrayList<>();
        adapter = new VehicleStatusAdapter(getContext(), vehicleStatusList);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }


    private void VehicleStatusApiCall(final String user_id, CharSequence s) {
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("term", s + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VAHICLE_STATUS, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("VehicleStatus", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("data");

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
    public void news_onItemClick(int position) {
        vehicleStatus = vehicleStatusList.get(position);
        intent = new Intent(getContext(), ProductDetails.class);
        intent.putExtra("id", vehicleStatus.getItem_id() + "");
        startActivity(intent);
    }
}
