package com.immortal.vehicletracking.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.immortal.vehicletracking.R;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getResources().getString(R.string.permission));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
