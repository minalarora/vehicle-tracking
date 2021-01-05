package com.immortal.vehicletracking.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.ui.activity.Home;
import com.immortal.vehicletracking.ui.activity.TotalListScreen;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.DASHBOARD;


public class HomeRoundFragment extends Fragment implements View.OnClickListener{

    private LinearLayout linear_Total, linear_inactive, linear_running, linear_stop, linear_idle;
    private View view;
    private Intent intent;
    private Context context;
    private TextView txt_running, txt_stop, txt_total, txt_idle, txt_inactive;
    private Fragment fragment;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    public HomeRoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home_round, container, false);
        context = getContext();
        initId();
        setListner();
        AllCategoryApiCall(userPreferences.getString(context, Constant.USER_ID));
        return view;
    }
    private void setListner() {
        linear_Total.setOnClickListener(this);
        linear_inactive.setOnClickListener(this);
        linear_running.setOnClickListener(this);
        linear_stop.setOnClickListener(this);
        linear_idle.setOnClickListener(this);
    }

    private void initId() {
        linear_Total = view.findViewById(R.id.linear_Total_round);
        linear_inactive = view.findViewById(R.id.linear_inactive_round);
        linear_running = view.findViewById(R.id.linear_running_round);
        linear_stop = view.findViewById(R.id.linear_stop_round);
        linear_idle = view.findViewById(R.id.linear_idle_round);

        txt_running = view.findViewById(R.id.txt_running_round);
        txt_stop = view.findViewById(R.id.txt_stop_round);
        txt_total = view.findViewById(R.id.txt_total_round);
        txt_idle = view.findViewById(R.id.txt_idle_round);
        txt_inactive = view.findViewById(R.id.txt_inactive_round);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_Total_round:
//                intent = new Intent(getContext(), TotalListScreen.class);
//                intent.putExtra("id", "");
//                Log.e("id->old", 4 + "p");
//                startActivity(intent);
                fragment = new VehicleStatusFragment();
                Home.replaceTotalFragment(fragment);
                break;
            case R.id.linear_inactive_round:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 4 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_running_round:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 1 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_stop_round:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 2 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_idle_round:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 3 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
        }
    }

    private void AllCategoryApiCall(final String user_id) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DASHBOARD, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("Home_Fragment", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("data");

                                setdatatodashboardview(data);

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
                        Log.e("AllCategoryFragment", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void setdatatodashboardview(JSONArray data) {

        try {
            txt_running.setText(data.getJSONObject(0).getString("total"));
            txt_stop.setText(data.getJSONObject(1).getString("total"));
            txt_total.setText(data.getJSONObject(4).getString("total"));
            txt_idle.setText(data.getJSONObject(2).getString("total"));
            txt_inactive.setText(data.getJSONObject(3).getString("total"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
