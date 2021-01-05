package com.immortal.vehicletracking.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.helperClass.ApiConstants;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.CHANGE_PASSWORD;
import static com.immortal.vehicletracking.utils.Constant.USER_PASSWORD;

/* renamed from: com.immortal.vehicletracking.ui.activity.ChangePasswordActivity */
public class ChangePasswordActivity extends AppCompatActivity {
    private EditText confirmPin;
    /* access modifiers changed from: private */
    public Context context;
    private EditText newPin;
    private EditText oldPin;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Toolbar toolbar;
    String userEnabledPin;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.context = this;
        initView();
        setupToolBar();
    }

    private void initView() {
        this.oldPin = (EditText) findViewById(R.id.old_pin);
        this.newPin = (EditText) findViewById(R.id.newpin);
        this.confirmPin = (EditText) findViewById(R.id.confirm_pin);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setupToolBar() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        this.toolbar.setTitle((CharSequence) "Change Password");
        setSupportActionBar(this.toolbar);
    }

    public void resetPin() {
        userEnabledPin = userPreferences.getString(this.context, USER_PASSWORD);
        Log.e("userEnabledPin", userEnabledPin);
        String oldpin = this.oldPin.getText().toString().trim();
        String currntPin = this.newPin.getText().toString().trim();
        String confrmPin = this.confirmPin.getText().toString().trim();
        String str = "";
        if (oldpin.equals(str)) {
            this.oldPin.setError("Please enter old password");
        } else if (currntPin.equals(str)) {
            this.newPin.setError("Please enter new password");
        } else if (confrmPin.equals(str)) {
            this.confirmPin.setError("Please enter confirm  password");
        } else if (!confrmPin.matches(currntPin)) {
            Toast.makeText(this.context, "Please enter correct password", Toast.LENGTH_SHORT).show();
        } else {
            if (!this.userEnabledPin.matches(oldpin)) {
                Toast.makeText(this.context, "Please enter correct old password", Toast.LENGTH_SHORT).show();
            } else {
                resetPinApiCall(oldpin, confrmPin, this.userPreferences.getString(this.context, Constant.USER_ID));
            }
        }
    }

    private void resetPinApiCall(String oldpin, final String confrmPin, String userId) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Please wait.....");
        this.progressDialog.show();
        JSONObject postparams = new JSONObject();
        try {
            postparams.put(Constant.USER_ID, userId);
            postparams.put("old_pass", oldpin);
            postparams.put("new_password", confrmPin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(postparams);
        sb.append("");
        Log.e("postparams", sb.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, CHANGE_PASSWORD, postparams, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                ChangePasswordActivity.this.progressDialog.dismiss();
                StringBuilder sb = new StringBuilder();
                sb.append(response);
                sb.append("");
                Log.e("Response", sb.toString());
                try {
                    if (response.getString(NotificationCompat.CATEGORY_STATUS).equals("true")) {
                        ChangePasswordActivity.this.userEnabledPin = confrmPin;
                        ChangePasswordActivity.this.userPreferences.setString(ChangePasswordActivity.this.context, USER_PASSWORD, ChangePasswordActivity.this.userEnabledPin);
                        Toast.makeText(ChangePasswordActivity.this.context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                ChangePasswordActivity.this.finish();
                            }
                        }, 1000);
                        return;
                    }
                    ChangePasswordActivity.this.progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error: ");
                sb.append(error.getMessage());
                Log.e("ResponseError", sb.toString());
                ChangePasswordActivity.this.progressDialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void resetPassword(View view) {
        resetPin();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
