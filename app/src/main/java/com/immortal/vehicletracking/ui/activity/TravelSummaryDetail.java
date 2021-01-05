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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.TravelSummaryAdapter;
import com.immortal.vehicletracking.model.TravelSummary;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.INDIVIDUAL_TRAVEL_SUMMARY;
import static com.immortal.vehicletracking.helperClass.ApiConstants.TRAVEL_SUMMARY;

public class TravelSummaryDetail extends AppCompatActivity implements TravelSummaryAdapter.News_OnItemClicked {

    private RecyclerView recyclerView;
    private TravelSummaryAdapter adapter;
    private List<TravelSummary> travelSummaryList;
    private TravelSummary travelSummary;
    private Intent intent;
    private GridLayoutManager mLayoutManager;
    private Context context;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private String startDate, endDate, device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_summary_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = TravelSummaryDetail.this;
        initId();
        getdatafromIntent();
        setRecyclerViewCode();
        MyProgressDialog.showPDialog(context);
        travelSummaryDetailsApiCall(userPreferences.getString(context, Constant.USER_ID), startDate, endDate, "", device_id);

        //  setdatatoView();
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            device_id = getIntent().getStringExtra("id");
            startDate = getIntent().getStringExtra("s_date");
            endDate = getIntent().getStringExtra("e_date");
        }
    }

    private void initId() {
        recyclerView = findViewById(R.id.recyclerview_t_summary_details);
    }


    private void setRecyclerViewCode() {
        travelSummaryList = new ArrayList<>();
        adapter = new TravelSummaryAdapter(TravelSummaryDetail.this, travelSummaryList);
        mLayoutManager = new GridLayoutManager(TravelSummaryDetail.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(TravelSummaryDetail.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }


    private void setdatatoView(JSONArray data) {
        travelSummaryList.clear();

        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String vehicle_id = list.isNull("device_id") ? "" : list.getString("device_id");
                String running = list.isNull("running") ? "" : list.getString("running");
                String max_speed = list.isNull("max_speed") ? "" : list.getString("max_speed");
                String avg_speed = list.isNull("avg_speed") ? "" : list.getString("avg_speed");
                String idle = list.isNull("idle") ? "" : list.getString("idle");
                String stop = list.isNull("stop") ? "" : list.getString("stop");
                String distance = list.isNull("distance") ? "" : list.getString("distance");
                String total_stop = list.isNull("total_stop") ? "" : list.getString("total_stop");
                String date = list.isNull("date") ? "" : list.getString("date");

                travelSummary = new TravelSummary(vehicle_id, vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png",
                        running, idle, stop, distance, total_stop, max_speed, avg_speed, "", date);
                travelSummaryList.add(travelSummary);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void travelSummaryDetailsApiCall(final String user_id, final String start, String end, CharSequence s, String device_id) {
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("from", start + "");
            postparams.put("to", end + "");
            postparams.put("term", "" + "");
            postparams.put("device", device_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, INDIVIDUAL_TRAVEL_SUMMARY, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("Travel_Summary_Details", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("travel_summary");

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
                        Log.e("Travel_Summary_Details", "Error: " + error.getMessage());
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
//        travelSummary = travelSummaryList.get(position);
//        intent = new Intent(TravelSummaryDetail.this, ProductDetails.class);
//        intent.putExtra("id", travelSummary.getItem_id() + "");
//        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
