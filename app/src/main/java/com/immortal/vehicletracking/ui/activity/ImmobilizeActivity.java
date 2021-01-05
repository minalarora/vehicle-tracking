package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.IMMOBILIZE_DEVICE;
import static com.immortal.vehicletracking.helperClass.ApiConstants.VAHICLE_DETAILS;

public class ImmobilizeActivity extends AppCompatActivity {

    private String vehicle_id;
    private TextView immobilize_status;
    private String status;
    private TextView immobilize_btn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobilize);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        initId();
        getdatafromIntent();
        if (status.equals("on")) {
            immobilize_status.setText("Off");
            immobilize_btn.setText("Turn On Immobilize");
        } else {
            immobilize_status.setText("On");
            immobilize_btn.setText("Turn Off Immobilize");

        }
    }

    private void initId() {
        immobilize_status = findViewById(R.id.immobilize_status);
        immobilize_btn = findViewById(R.id.immobilize_btn);
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            vehicle_id = getIntent().getStringExtra("id");
            status = getIntent().getStringExtra("status");
        }
    }

    private void ImmobilizeApiCall(final String vehicle_id, String i_status) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("device", vehicle_id);
            postparams.put("status", i_status);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, IMMOBILIZE_DEVICE, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {

                                finish();
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();

                            } else {
                                finish();
                                //Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
//                                new CustomToast().Show_Toast(context, view,
//                                        response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyProgressDialog.hidePDialog();

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

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void onImmobilizeDevice(View view) {
        String i_status = immobilize_status.getText().toString();
        if (i_status.equals("on")) {
            i_status = "off";
        } else {
            i_status = "on";

        }
        ImmobilizeApiCall(vehicle_id, i_status);
    }
}
