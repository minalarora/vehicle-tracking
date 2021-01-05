package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.DistanceSummaryAdapter;
import com.immortal.vehicletracking.model.DistanceSummary;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.DISTANCE_SUMMARY;

public class DistanceSummaryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private DistanceSummaryAdapter adapter;
    private List<DistanceSummary> distanceitemList;
    private DistanceSummary distanceitem;
    private Intent intent;
    private Context context;
    private TextView txt_daterange;
    private Toolbar toolbar;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_summary);
        context = DistanceSummaryActivity.this;
        initId();
        SetTabTitledata();
        setupToolBar();
        SetRecyclerViewCode();
        distanceSummaryApiCall(userPreferences.getString(context, Constant.USER_ID), "1");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                distanceSummaryApiCall(userPreferences.getString(context, Constant.USER_ID), tab.getTag() + "");
                Log.e("Tag", tab.getTag() + "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void SetTabTitledata() {
        tabLayout.addTab(tabLayout.newTab().setText("Today").setTag("1"));
        tabLayout.addTab(tabLayout.newTab().setText("Yestrd").setTag("2"));
        tabLayout.addTab(tabLayout.newTab().setText("Week").setTag("3"));
        tabLayout.addTab(tabLayout.newTab().setText("Month").setTag("4"));
    }

    private void initId() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        recyclerView = findViewById(R.id.d_summary_RecyclerView);
        txt_daterange = findViewById(R.id.txt_daterange);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Distance Summary");
        setSupportActionBar(toolbar);
    }

    private void SetRecyclerViewCode() {
        distanceitemList = new ArrayList<>();
        adapter = new DistanceSummaryAdapter(this, distanceitemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//		adapter.setOnClick(this);
    }


    private void setdatatoTotalList(JSONArray data) {
        distanceitemList.clear();
        Log.e("data", data + "");
        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String distance = list.isNull("distance") ? "" : list.getString("distance");
                Log.e("vehicle_no", vehicle_no + "");

//                String vehicle_id = list.isNull("id") ? "" : list.getString("id");
//                double curr_lat = list.isNull("lat") ? 0 : list.getDouble("lat");
//                double curr_long = list.isNull("lng") ? 0 : list.getDouble("lng");
                String time = list.isNull("time") ? "" : list.getString("time");

                distanceitem = new DistanceSummary(vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png", time, distance, "rety");
                distanceitemList.add(distanceitem);
                Log.e("time", time + "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void distanceSummaryApiCall(final String user_id, String veh_id) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("week", veh_id + "");
            postparams.put("term", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DISTANCE_SUMMARY, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("data");
                                String daterange = response.isNull("daterange") ? "" : response.getString("daterange");
                                txt_daterange.setText(daterange);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
