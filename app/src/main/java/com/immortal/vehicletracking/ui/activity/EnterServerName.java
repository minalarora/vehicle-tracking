package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.immortal.vehicletracking.R;

public class EnterServerName extends AppCompatActivity {
    private Intent intent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_server_name);
        context = EnterServerName.this;
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            String id = getIntent().getStringExtra("id");
            String key = getIntent().getStringExtra("key");
            intent = new Intent(this, Home.class);
            intent.putExtra("id", id);
            intent.putExtra("key", key);
            startActivity(intent);
        }
    }

}