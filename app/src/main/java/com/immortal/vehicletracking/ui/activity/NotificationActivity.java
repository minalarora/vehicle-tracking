package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.AlertAdapter;
import com.immortal.vehicletracking.adapter.NotificationAdapter;
import com.immortal.vehicletracking.model.Alert;
import com.immortal.vehicletracking.model.Notification;
import com.immortal.vehicletracking.network.MyApplication;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.NOTIFICATION_LIST;


public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.Alert_OnItemClicked {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private Notification notification;
    private Intent intent;
    private GridLayoutManager mLayoutManager;
    private Context context;
    private Toolbar toolbar;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = NotificationActivity.this;
        initId();
        setupToolBar();
        setRecyclerViewCode();
        alertListApiCall(userPreferences.getString(context, Constant.USER_ID), "", "", "");
    }

    private void initId() {
        recyclerView = findViewById(R.id.notification_RecyclerView);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
    }

    private void setRecyclerViewCode() {
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(context, notificationList);
        mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    private void alertListApiCall(String user_id, final String start, String end, CharSequence s) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("userid", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NOTIFICATION_LIST, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("notification");

                                setdatatoTotalList(data);

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

    private void setdatatoTotalList(JSONArray data) {
        notificationList.clear();
        Log.e("data", data + "");
        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String alert_type = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
                String alarm_name = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
                String location = list.isNull("address") ? "" : list.getString("address");
//                String oil_electricity_alarm = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
//                String alarm_name = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");

                Log.e("vehicle_no", vehicle_no + "");

//                String vehicle_id = list.isNull("id") ? "" : list.getString("id");
//                double curr_lat = list.isNull("lat") ? 0 : list.getDouble("lat");
//                double curr_long = list.isNull("lng") ? 0 : list.getDouble("lng");
                String time = list.isNull("time") ? "" : list.getString("time");

                notification = new Notification("1", vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png", "10-10-2020", "10:39:30", alarm_name, alert_type, location);
                notificationList.add(notification);
                Log.e("time", time + "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void alert_onItemClick(int position) {

    }
}
