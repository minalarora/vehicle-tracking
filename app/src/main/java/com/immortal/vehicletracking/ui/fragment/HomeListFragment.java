package com.immortal.vehicletracking.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeListFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Intent intent;
    private Context context;
    private LinearLayout inactive_layout, idle_layout, total_layout, stop_layout, running_layout;
    private TextView txt_running, txt_stop, txt_total, txt_idle, txt_inactive;
    private Fragment fragment;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private ImageView banner_image;

    public HomeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_list, container, false);
        context = getContext();
        initId();
        setListener();
        dashboardApiCall(userPreferences.getString(context, Constant.USER_ID));
        return view;
    }

    private void initId() {
        stop_layout = (LinearLayout) view.findViewById(R.id.stop_layout);
        idle_layout = (LinearLayout) view.findViewById(R.id.idle_layout);
        total_layout = (LinearLayout) view.findViewById(R.id.total_layout);
        inactive_layout = (LinearLayout) view.findViewById(R.id.inactive_layout);
        running_layout = (LinearLayout) view.findViewById(R.id.running_layout);

        txt_running = view.findViewById(R.id.txt_running_2);
        txt_stop = view.findViewById(R.id.txt_stop_2);
        txt_total = view.findViewById(R.id.txt_total_2);
        txt_idle = view.findViewById(R.id.txt_idle_2);
        txt_inactive = view.findViewById(R.id.txt_inactive_2);
        this.banner_image = (ImageView) this.view.findViewById(R.id.banner_image);

    }

    private void setListener() {
        idle_layout.setOnClickListener(this);
        stop_layout.setOnClickListener(this);
        total_layout.setOnClickListener(this);
        inactive_layout.setOnClickListener(this);
        running_layout.setOnClickListener(this);

    }

    private void dashboardApiCall(final String user_id) {
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
            Glide.with(context).load(data.getJSONObject(5).isNull("Banner_Name") ? "" : data.getJSONObject(5).getString("Banner_Name")).into(this.banner_image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stop_layout:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 2 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.inactive_layout:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 4 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;

            case R.id.idle_layout:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 3 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.total_layout:
                fragment = new VehicleStatusFragment();
                Home.replaceTotalFragment(fragment);
                break;
            case R.id.running_layout:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 1 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;

        }
    }
}
