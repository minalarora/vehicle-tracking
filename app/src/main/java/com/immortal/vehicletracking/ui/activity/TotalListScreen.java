package com.immortal.vehicletracking.ui.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.TotalListAdapter;
import com.immortal.vehicletracking.model.ItemList;
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
import static com.immortal.vehicletracking.helperClass.ApiConstants.LIVE_STATUS;
import static com.immortal.vehicletracking.helperClass.ApiConstants.image_str;

public class TotalListScreen extends FragmentActivity implements OnMapReadyCallback, TotalListAdapter.News_OnItemClicked {

    private GoogleMap mMap;
    private RecyclerView recyclerView;
    private TotalListAdapter adapter;
    private List<ItemList> totallist;
    private ItemList itemList;
    private int visibleItemCount, totalItemCount, firstVisibleItemPosition, lastVisibleItem;
    private GridLayoutManager mLayoutManager;
    private Intent intent;
    private Context context;
    private String veh_id;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_list);
        context = TotalListScreen.this;
        initId();
        getdatafromIntent();
        setRecyclerViewCode();
        AllCategoryApiCall(userPreferences.getString(context, Constant.USER_ID), veh_id);

        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrentItem();
                    Log.e("position", position + "");
//                    if (position == 0) {
//                        itemList = totallist.get(position);
//                        Toast.makeText(TotalListScreen.this, "0->" + itemList.getItem_id() + ":" + position + mLayoutManager.findFirstCompletelyVisibleItemPosition() + "," + lastVisibleItem, Toast.LENGTH_SHORT).show();
//                    }
                    LatLng sydney = new LatLng(itemList.getItem_lat(), itemList.getItem_long());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(itemList.getItem_name()).icon(BitmapDescriptorFactory.fromResource(R.drawable.truck_icon_green)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney)
                            .zoom(12)
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //onPageChanged(position);
                    itemList = totallist.get(position);
                    Toast.makeText(TotalListScreen.this, itemList.getItem_id() + ":" + position + mLayoutManager.findFirstCompletelyVisibleItemPosition() + "," + lastVisibleItem, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            veh_id = getIntent().getStringExtra("id");
        }
        // Log.e("id->old", veh_id + "p");
    }

    private int getCurrentItem() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    private void AllCategoryApiCall(final String user_id, String veh_id) {
        MyProgressDialog.showPDialog(context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("account_id", user_id);
            postparams.put("live_status", veh_id + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LIVE_STATUS, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyProgressDialog.hidePDialog();
                        Log.e("TotalListScreen", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                // Toast.makeText(getContext(), " Register Successfully", Toast.LENGTH_LONG).show();
                                JSONArray data = response.getJSONArray("data");

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
        for (int i = 0; i <= data.length(); i++) {
            JSONObject list = null;
            try {
                list = data.getJSONObject(i);
                String vehicle_no = list.isNull("vehicle_no") ? "" : list.getString("vehicle_no");
                String vehicle_img = list.isNull("vehicle_img") ? "" : list.getString("vehicle_img");
                String max_speed = list.isNull("max_speed") ? "" : list.getString("max_speed");
                String vehicle_id = list.isNull("id") ? "" : list.getString("id");
                double curr_lat = list.isNull("lat") ? 0 : list.getDouble("lat");
                double curr_long = list.isNull("lng") ? 0 : list.getDouble("lng");
                String address = list.isNull("address") ? "" : list.getString("address");

                itemList = new ItemList(vehicle_no, "", vehicle_img, max_speed, address, curr_lat, curr_long, vehicle_id);
                totallist.add(itemList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initId() {
        recyclerView = findViewById(R.id.recyclerview_total);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(20);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(28.7041, 77.1025);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Delhi").icon(BitmapDescriptorFactory.fromResource(R.drawable.truck_icon_green)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(sydney)
//                .zoom(12)
//                .build();
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setRecyclerViewCode() {
        totallist = new ArrayList<>();
        adapter = new TotalListAdapter(this, totallist);
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    @Override
    public void news_onItemClick(int position) {
        itemList = totallist.get(position);
        intent = new Intent(this, ProductDetails.class);
        intent.putExtra("id", itemList.getItem_id() + "");
        startActivity(intent);
    }
}