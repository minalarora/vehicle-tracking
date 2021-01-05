package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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
import com.immortal.vehicletracking.model.Alert;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.CenterZoomLayoutManager;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.ALERT_LIST;
import static com.immortal.vehicletracking.utils.Utils.getFormattedDate;

public class AlertActivity extends AppCompatActivity implements AlertAdapter.Alert_OnItemClicked,
        View.OnClickListener {

    private RecyclerView recyclerView;
    private AlertAdapter adapter;
    private List<Alert> alertList;
    private Alert alert;
    private Intent intent;
    private GridLayoutManager mLayoutManager;
    private Context context;
    private Toolbar toolbar;
    private LinearLayout startDateLayout, endDateLayout;
    private TextView go_textView, startDate, endDate;
    private EditText sBarET_alert;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        context = AlertActivity.this;
        initId();
        setupToolBar();
        setListeners();
        setRecyclerViewCode();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        startDate.setText(formattedDate);
        endDate.setText(formattedDate);
        MyProgressDialog.showPDialog(context);
        alertListApiCall(userPreferences.getString(context, Constant.USER_ID), formattedDate, formattedDate, "");

        sBarET_alert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                alertListApiCall(userPreferences.getString(context, Constant.USER_ID), formattedDate, formattedDate, s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initId() {
        recyclerView = findViewById(R.id.alert_RecyclerView);
        toolbar = findViewById(R.id.toolbar);
        go_textView = findViewById(R.id.go_textView);
        startDateLayout = findViewById(R.id.startDateLayout);
        startDate = findViewById(R.id.startDate);
        endDateLayout = findViewById(R.id.endDateLayout);
        endDate = findViewById(R.id.endDate);
        sBarET_alert = findViewById(R.id.sBarET_alert);
    }

    private void setListeners() {
        startDateLayout.setOnClickListener(this);
        endDateLayout.setOnClickListener(this);
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Alert");
        setSupportActionBar(toolbar);
    }

    private void setRecyclerViewCode() {
        alertList = new ArrayList<>();
        adapter = new AlertAdapter(context, alertList);
        mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDateLayout:
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, month, dayOfMonth);

                                startDate.setText(getFormattedDate(selectedDate));
                                go_textView.setText("Go");
                                go_textView.setTextColor(getResources().getColor(R.color.white));


                            }
                        }, mYear, mMonth, mDay);
                dialog.show();
                break;
            case R.id.endDateLayout:
                Calendar cal = Calendar.getInstance();
                int eYear = cal.get(Calendar.YEAR);
                int eMonth = cal.get(Calendar.MONTH);
                int eDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog_new =
                        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, month, dayOfMonth);

                                endDate.setText(getFormattedDate(selectedDate));
                                go_textView.setText("Go");
                                go_textView.setTextColor(getResources().getColor(R.color.white));

                            }
                        }, eYear, eMonth, eDay);
                dialog_new.show();
                break;

        }
    }


    private void alertListApiCall(String user_id, final String start, String end, CharSequence s) {
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("from", start);
            postparams.put("to", end + "");
            postparams.put("term", s + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ALERT_LIST, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("alarm");

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
        alertList.clear();
        Log.e("data", data + "");
        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String alert_type = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
                String alarm_name = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
                String location = list.isNull("location") ? "" : list.getString("location");
//                String oil_electricity_alarm = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");
//                String alarm_name = list.isNull("oil_electricity_alarm") ? "" : list.getString("oil_electricity_alarm");

                Log.e("vehicle_no", vehicle_no + "");

//                String vehicle_id = list.isNull("id") ? "" : list.getString("id");
//                double curr_lat = list.isNull("lat") ? 0 : list.getDouble("lat");
//                double curr_long = list.isNull("lng") ? 0 : list.getDouble("lng");
                String time = list.isNull("time") ? "" : list.getString("time");

                alert = new Alert("1", vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png", "10-10-2020", "10:39:30", alarm_name, alert_type, location);
                alertList.add(alert);
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

    public void applyDatefilter(View view) {
        MyProgressDialog.showPDialog(context);
        alertListApiCall(userPreferences.getString(context, Constant.USER_ID), startDate.getText().toString(), endDate.getText().toString(), "");
    }
}