package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textfield.TextInputEditText;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.immortal.vehicletracking.helperClass.ApiConstants.UPDATE_DRIVER;

public class EditDriverActivity extends AppCompatActivity {

    private String name, mobile, driver_id;
    private TextInputEditText et_driver_name, et_driver_number;
    private Context context;
    private String vehicle_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Driver");
        context = EditDriverActivity.this;
        initId();
        getdataFromIntent();
        setdataFromIntent();
    }

    private void setdataFromIntent() {
        et_driver_name.setText(name + "");
        et_driver_number.setText(mobile + "");
    }

    private void getdataFromIntent() {
        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            mobile = getIntent().getStringExtra("number");
            driver_id = getIntent().getStringExtra("id");
            vehicle_id = getIntent().getStringExtra("vehicle_id");
        }
    }

    private void initId() {
        et_driver_name = findViewById(R.id.et_driver_name);
        et_driver_number = findViewById(R.id.et_driver_number);
    }

    private void checkValidation() {

        String getName = et_driver_name.getText().toString().trim();
        String getPhone = et_driver_number.getText().toString().trim();

        // Check for both field is empty or not
        if (getName.equals("") || getName.length() == 0
                || getPhone.equals("") || getPhone.length() == 0) {
            Toast.makeText(context, "Enter Both Details.", Toast.LENGTH_SHORT).show();

//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Enter both credentials.");

        } else {

            updateDriverApiCall(getName, getPhone);
        }
    }

    private void updateDriverApiCall(String getName, String getPhone) {
        MyProgressDialog.showPDialog(context);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("driver_id", driver_id);
            postparams.put("driver_name", getName);
            postparams.put("phone", getPhone);
            postparams.put("vehicle_id",vehicle_id+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_DRIVER, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LogUp", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                MyProgressDialog.hidePDialog();
                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                MyProgressDialog.hidePDialog();
                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
                                finish();
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
                        Log.e("Login Activity" + "dayInStat", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void addDriverDetails(View view) {
        checkValidation();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
