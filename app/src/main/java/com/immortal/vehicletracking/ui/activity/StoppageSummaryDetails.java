package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.StoppageDetailsAdapter;
import com.immortal.vehicletracking.model.StoppageDetail;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.INDIVIDUAL_VAHICLE_STOPPAGE;
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_STOPPAGE;

public class StoppageSummaryDetails extends AppCompatActivity implements StoppageDetailsAdapter.News_OnItemClicked {

    private RecyclerView recyclerView;
    private StoppageDetailsAdapter adapter;
    private List<StoppageDetail> stoppageSummaryList;
    private StoppageDetail stoppageSummary;
    private Intent intent;
    private GridLayoutManager mLayoutManager;
    private Context context;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private String startDate, endDate, device_id;
    private String lat, lng;
    private ImageView s_summary_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoppage_summary_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = StoppageSummaryDetails.this;
        initId();
        getdatafromIntent();
        setRecyclerViewCode();
        stoppageSummaryDetailsApiCall(userPreferences.getString(context, Constant.USER_ID), startDate, endDate, "", device_id);

    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            device_id = getIntent().getStringExtra("id");
            startDate = getIntent().getStringExtra("s_date");
            endDate = getIntent().getStringExtra("e_date");
        }
    }

    private void stoppageSummaryDetailsApiCall(final String user_id, final String start, String end, CharSequence s, String device_id) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("from", start);
            postparams.put("to", end + "");
            postparams.put("term", s + "");
            postparams.put("device", device_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, INDIVIDUAL_VAHICLE_STOPPAGE, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("stoppage_summary");
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

    private void setdatatoView(JSONArray data) {
        if (data.length() == 0) {
            s_summary_no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            s_summary_no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i <= data.length(); i++) {
                JSONObject list = null;
                try {
                    list = data.getJSONObject(i);
                    String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                    String vehicle_id = list.isNull("device_id") ? "" : list.getString("device_id");
                    String address = list.isNull("address") ? "" : list.getString("address");
                    lat = list.isNull("lat") ? "" : list.getString("lat");
                    lng = list.isNull("lng") ? "" : list.getString("lng");
                    String duration = list.isNull("idle") ? "" : list.getString("idle");
                    String parking = list.isNull("stop") ? "" : list.getString("stop");
                    String distance = list.isNull("distance") ? "" : list.getString("distance");
                    String alert = list.isNull("alert") ? "" : list.getString("alert");
                    String date = list.isNull("date") ? "" : list.getString("date");

                    stoppageSummary = new StoppageDetail(vehicle_id, vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png",
                            alert, duration, parking, distance, date, "05:00", date, "06:00", address, lat, lng);
                    stoppageSummaryList.add(stoppageSummary);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    private void initId() {
        recyclerView = findViewById(R.id.recyclerview_summary_details);
        s_summary_no_data = findViewById(R.id.s_summary_no_data);
    }


    private void setRecyclerViewCode() {
        stoppageSummaryList = new ArrayList<>();
        adapter = new StoppageDetailsAdapter(this, stoppageSummaryList);
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    @Override
    public void news_onItemClick(int position) {
        stoppageSummary = stoppageSummaryList.get(position);
        intent = new Intent(this, StoppageDetails.class);
        intent.putExtra("lat", stoppageSummary.getLat());
        intent.putExtra("lng", stoppageSummary.getLng());
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
