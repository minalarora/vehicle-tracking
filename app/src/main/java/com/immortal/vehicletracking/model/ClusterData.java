package com.immortal.vehicletracking.model;


import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterData implements ClusterItem {

    private final String username;
    private final String address;
    private final LatLng latLng;
    private final String v_id;

    public ClusterData(String username, LatLng latLng, String address, String v_id) {
        this.username = username;
        this.latLng = latLng;
        this.address = address;
        this.v_id = v_id;
    }

    public String getV_id() {
        return v_id;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return username;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
