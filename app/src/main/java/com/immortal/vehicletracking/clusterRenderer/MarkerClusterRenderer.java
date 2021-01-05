package com.immortal.vehicletracking.clusterRenderer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.ClusterData;
import com.immortal.vehicletracking.ui.activity.ProductDetails;


@SuppressLint("InflateParams")
public class MarkerClusterRenderer extends DefaultClusterRenderer<ClusterData> implements ClusterManager.OnClusterClickListener<ClusterData>, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private LayoutInflater layoutInflater;
    private final IconGenerator clusterIconGenerator;
    private final View clusterItemView;
    private Intent intent;

    public MarkerClusterRenderer(@NonNull Context context, GoogleMap map, ClusterManager<ClusterData> clusterManager) {
        super(context, map, clusterManager);

        this.googleMap = map;

        layoutInflater = LayoutInflater.from(context);

        clusterItemView = layoutInflater.inflate(R.layout.single_cluster_marker_view, null);

        clusterIconGenerator = new IconGenerator(context);
        Drawable drawable = ContextCompat.getDrawable(context, android.R.color.transparent);
        clusterIconGenerator.setBackground(drawable);
        clusterIconGenerator.setContentView(clusterItemView);
        clusterManager.setOnClusterClickListener(this);
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        googleMap.setOnInfoWindowClickListener(this);
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomClusterItemInfoView());
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterData item, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck_icon_green));
        markerOptions.title(item.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClusterData> cluster, MarkerOptions markerOptions) {
        TextView singleClusterMarkerSizeTextView = clusterItemView.findViewById(R.id.singleClusterMarkerSizeTextView);
        singleClusterMarkerSizeTextView.setText(String.valueOf(cluster.getSize()));
        Bitmap icon = clusterIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected void onClusterItemRendered(ClusterData clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }

    @Override
    public boolean onClusterClick(Cluster<ClusterData> cluster) {
        if (cluster == null) return false;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterData clusterData : cluster.getItems())
            builder.include(clusterData.getPosition());
        LatLngBounds bounds = builder.build();
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Context context = clusterItemView.getContext();
        ClusterData clusterData = (ClusterData) marker.getTag(); //  handle the clicked marker object
        if (context != null && clusterData != null) {
            intent = new Intent(context, ProductDetails.class);
            intent.putExtra("id", clusterData.getV_id() + "");
            context.startActivity(intent);
            Toast.makeText(context, clusterData.getV_id(), Toast.LENGTH_SHORT).show();
        }
    }

    private class MyCustomClusterItemInfoView implements GoogleMap.InfoWindowAdapter {

        private final View clusterItemView;

        MyCustomClusterItemInfoView() {
            clusterItemView = layoutInflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            ClusterData clusterData = (ClusterData) marker.getTag();
            if (clusterData == null) return clusterItemView;
            TextView itemNameTextView = clusterItemView.findViewById(R.id.itemNameTextView);
            TextView itemAddressTextView = clusterItemView.findViewById(R.id.itemAddressTextView);
            itemNameTextView.setText(marker.getTitle());
            itemAddressTextView.setText(clusterData.getAddress());
            return clusterItemView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
