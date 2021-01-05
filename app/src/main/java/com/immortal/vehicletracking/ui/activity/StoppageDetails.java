package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.immortal.vehicletracking.R;

public class StoppageDetails extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoppage_dettails);
        context = StoppageDetails.this;
        getdatafromintent();
        initId();
    }

    private void getdatafromintent() {
        if (getIntent() != null) {
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
            lng = Double.parseDouble(getIntent().getStringExtra("lng"));
        }
    }

    private void initId() {
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
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(12)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
