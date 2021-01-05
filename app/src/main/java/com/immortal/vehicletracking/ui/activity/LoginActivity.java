package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.LOGIN;
import static com.immortal.vehicletracking.utils.Constant.TIMEOUT_VOLLY;

public class LoginActivity extends AppCompatActivity {
    private Intent intent;
    private Context context;
    private TextInputEditText emailid, password;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        initId();
    }

    private void initId() {
        emailid = findViewById(R.id.name_edit_text);
        password = findViewById(R.id.password_edit_text);
    }

    public void gotoHome(View view) {
        checkValidation();
    }

    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString().trim();
        String getPassword = password.getText().toString().trim();

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
//            loginLayout.startAnimation(shakeAnimation);
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Enter both credentials.");
            Toast.makeText(context, "Enter both credentials", Toast.LENGTH_SHORT).show();

        } else {
//            if (getEmailId.equals("u") || getEmailId.equals("t") || getEmailId.equals("o") || getEmailId.equals("m")) {
//                switch (getEmailId) {
//                    case "u":
//                        key = "user";
//                        break;
//                }

            Toast.makeText(context, "Do Login.", Toast.LENGTH_SHORT).show();
            LoginApiCall(getEmailId, getPassword);
//                LoginApiCall();
//            } else {
//                loginLayout.startAnimation(shakeAnimation);
//                new CustomToast().Show_Toast(getActivity(), view,
//                        "Invalid User");
//            }
        }
    }

    private void LoginApiCall(String getEmailId, String getPassword) {
        MyProgressDialog.showPDialog(context);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("email", getEmailId);
            postparams.put("password", getPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this,"sd",Toast.LENGTH_LONG).show();
                        Log.e("LogIn->Response", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                MyProgressDialog.hidePDialog();
                                JSONObject data = response.getJSONObject("data");
                                String user_id = data.getString("id");
                                String user_name = data.getString("customer_name");
                                String email = data.getString("email");

                                userPreferences.setString(getApplicationContext(), Constant.USER_ID, user_id);
                                userPreferences.setString(getApplicationContext(), Constant.USER_NAME, user_name);
                                userPreferences.setString(getApplicationContext(), Constant.USER_EMAIL, email);
                                userPreferences.setString(getApplicationContext(), Constant.USER_PASSWORD, getPassword);

                                Log.e("id->>", userPreferences.getString(context, Constant.USER_ID));
                                startActivity(new Intent(context, Home.class));
                                finish();
                            } else {
                                MyProgressDialog.hidePDialog();
//                                new CustomToast().Show_Toast(getActivity(), view,
//                                        response.getString("message"));
                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(LoginActivity.this,"sde",Toast.LENGTH_LONG).show();
                        Log.e("Login Activity" + "dayInStat", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_VOLLY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
