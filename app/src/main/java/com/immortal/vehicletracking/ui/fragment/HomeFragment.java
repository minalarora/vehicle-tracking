package com.immortal.vehicletracking.ui.fragment;


import android.app.Dialog;
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
import com.android.volley.Response;
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

public class HomeFragment extends Fragment implements View.OnClickListener {

    private LinearLayout linear_Total, linear_inactive, linear_running, linear_stop, linear_idle;
    private View view;
    private Intent intent;
    private Context context;
    private TextView txt_running, txt_stop, txt_total, txt_idle, txt_inactive;
    private Fragment fragment;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private ImageView banner_image;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
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
        linear_Total = view.findViewById(R.id.linear_Total);
        linear_inactive = view.findViewById(R.id.linear_inactive);
        linear_running = view.findViewById(R.id.linear_running);
        linear_stop = view.findViewById(R.id.linear_stop);
        linear_idle = view.findViewById(R.id.linear_idle);

        txt_running = view.findViewById(R.id.txt_running);
        txt_stop = view.findViewById(R.id.txt_stop);
        txt_total = view.findViewById(R.id.txt_total);
        txt_idle = view.findViewById(R.id.txt_idle);
        txt_inactive = view.findViewById(R.id.txt_inactive);
        this.banner_image = (ImageView) this.view.findViewById(R.id.banner_image);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_Total:
//                intent = new Intent(getContext(), TotalListScreen.class);
//                intent.putExtra("id", "");
//                Log.e("id->old", 4 + "p");
//                startActivity(intent);
                fragment = new VehicleStatusFragment();
                Home.replaceTotalFragment(fragment);
                break;
            case R.id.linear_inactive:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 4 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_running:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 1 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_stop:
                intent = new Intent(getContext(), TotalListScreen.class);
                intent.putExtra("id", 2 + "");
                Log.e("id->old", 4 + "p");
                startActivity(intent);
                break;
            case R.id.linear_idle:
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
                                offerApiCall(userPreferences.getString(context, Constant.USER_ID));

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
    /* access modifiers changed from: private */
    public void offerApiCall(String user_id) {
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("userid", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, "https://www.citopay.xyz/motor-vahan-aws/motor-vahan2/ApiController/ListOffers", postparams, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                StringBuilder sb = new StringBuilder();
                sb.append(response);
                String str = "";
                sb.append(str);
                Log.e("Home_Fragment", sb.toString());
                try {
                    if (response.getString("status").equals("true")) {
                        String offer_image = response.getString("data");

                        Log.e("offer_image", offer_image.toString());
                        HomeFragment.this.openplacebidDialog(offer_image);
                        return;
                    }
                    Toast.makeText(HomeFragment.this.context, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error: ");
                sb.append(error.getMessage());
                Log.e("AllCategoryFragment", sb.toString());
                MyProgressDialog.hidePDialog();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, 1.0f));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void openplacebidDialog(String banner_image2) {
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.dialog_offer);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView cancel_dialog = (ImageView) dialog.findViewById(R.id.cancel_dialog);
        ImageView image_dialog = (ImageView) dialog.findViewById(R.id.image_dialog);
        StringBuilder sb = new StringBuilder();
        sb.append(banner_image2);
        sb.append("");
        Log.e("banner_image->", sb.toString());
        Glide.with(this.context).load(banner_image2).into(image_dialog);
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}