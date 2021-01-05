package com.immortal.vehicletracking.ui.fragment;


import android.app.DatePickerDialog;
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
import com.immortal.vehicletracking.adapter.StoppageSummaryAdapter;
import com.immortal.vehicletracking.model.StoppageSummary;
import com.immortal.vehicletracking.model.VehicleStatus;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.ui.activity.StoppageSummaryDetails;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_STATUS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_STOPPAGE;
import static com.immortal.vehicletracking.utils.Utils.getFormattedDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoppageSummaryFragment extends Fragment implements StoppageSummaryAdapter.News_OnItemClicked, View.OnClickListener {

    private RecyclerView recyclerView;
    private StoppageSummaryAdapter adapter;
    private List<StoppageSummary> stoppageSummaryList;
    private StoppageSummary stoppageSummary;
    private Intent intent;
    private View view;
    private GridLayoutManager mLayoutManager;
    private Context context;

    private TextView go_textView_stop, startDate, endDate;
    private EditText sBarET_stop;
    private LinearLayout sDateLayout_stop, eDateLayout_stop;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    public StoppageSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stoppage_summary, container, false);
        context = getActivity();
        initId();
        setRecyclerViewCode();
        setListeners();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        startDate.setText(formattedDate);
        endDate.setText(formattedDate);

        MyProgressDialog.showPDialog(context);
        stoppageSummaryApiCall(userPreferences.getString(context, Constant.USER_ID), formattedDate, formattedDate, "");

        sBarET_stop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                stoppageSummaryApiCall(userPreferences.getString(context, Constant.USER_ID), formattedDate, formattedDate, s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        go_textView_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDialog.showPDialog(context);
                stoppageSummaryApiCall(userPreferences.getString(context, Constant.USER_ID), startDate.getText().toString(), endDate.getText().toString(), "");
            }
        });
        return view;
    }

    private void setdatatoView(JSONArray data) {
        Log.e("data", "total_stop" + "po");

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

                Log.e("data", total_stop + "po");
                stoppageSummary = new StoppageSummary(vehicle_id, vehicle_no, "http://royalcruiser.com/Royal_Cruiser/slider/images/site/Slider_08.png",
                        running, idle, stop, distance, total_stop, max_speed, avg_speed);
                stoppageSummaryList.add(stoppageSummary);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initId() {
        recyclerView = view.findViewById(R.id.recyclerview_s_summary);
        go_textView_stop = view.findViewById(R.id.go_textView_stop);
        startDate = view.findViewById(R.id.startDate_stop);
        endDate = view.findViewById(R.id.endDate_stop);
        sBarET_stop = view.findViewById(R.id.sBarET_stop);
        sDateLayout_stop = view.findViewById(R.id.sDateLayout_stop);
        eDateLayout_stop = view.findViewById(R.id.eDateLayout_stop);
    }

    private void setListeners() {
        sDateLayout_stop.setOnClickListener(this);
        eDateLayout_stop.setOnClickListener(this);
    }

    private void setRecyclerViewCode() {
        stoppageSummaryList = new ArrayList<>();
        adapter = new StoppageSummaryAdapter(getContext(), stoppageSummaryList);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sDateLayout_stop:
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
                                go_textView_stop.setText("Go");
                                go_textView_stop.setTextColor(getResources().getColor(R.color.white));


                            }
                        }, mYear, mMonth, mDay);
                dialog.show();
                break;
            case R.id.eDateLayout_stop:
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
                                go_textView_stop.setText("Go");
                                go_textView_stop.setTextColor(getResources().getColor(R.color.white));

                            }
                        }, eYear, eMonth, eDay);
                dialog_new.show();
                break;

        }
    }

    private void stoppageSummaryApiCall(final String user_id, final String start, String end, CharSequence s) {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VAHICLE_STOPPAGE, postparams,
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

    @Override
    public void news_onItemClick(int position) {
        stoppageSummary=stoppageSummaryList.get(position);
        intent = new Intent(getContext(), StoppageSummaryDetails.class);
        intent.putExtra("id", stoppageSummary.getItem_id() + "");
        intent.putExtra("s_date", startDate.getText().toString());
        intent.putExtra("e_date", endDate.getText().toString());
        startActivity(intent);
    }
}
