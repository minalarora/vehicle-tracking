package com.immortal.vehicletracking.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.UserPreferences;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Dialog dialog;
    TextView cancel_ringtone, cancel, tv_startup_save, set_ringtone, cancel_port, cancel_map_setting, cancel_alert;
    LinearLayout startup, alert, port, map_setting, permission, parking, disconneted;
    private RadioGroup radioGroup;
    private Context context;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private int rb_Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = SettingActivity.this;
//        getSupportActionBar().setTitle(getResources().getString(R.string.setting_map));
        initId();
        Listener();
    }

    private void getdatafromintent() {
        if (getIntent() != null) {
            String id = getIntent().getStringExtra("id");
        }
    }

    private void initId() {
        disconneted = findViewById(R.id.disconnect_layout);
        parking = findViewById(R.id.parking_layout);
        permission = findViewById(R.id.permission_layout);
        map_setting = findViewById(R.id.map_setting_layout);
        startup = findViewById(R.id.start_up_layout);
        alert = findViewById(R.id.alert_layout);
        port = findViewById(R.id.port_layout);
    }

    private void Listener() {
        alert.setOnClickListener(this);
        port.setOnClickListener(this);
        map_setting.setOnClickListener(this);
        permission.setOnClickListener(this);
        parking.setOnClickListener(this);
        startup.setOnClickListener(this);
        disconneted.setOnClickListener(this);

    }

    private void StartUpDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_start_up_screen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        cancel = dialog.findViewById(R.id.tv_startup_cancel);
        tv_startup_save = dialog.findViewById(R.id.tv_startup_save);
        radioGroup = dialog.findViewById(R.id.rg_startUp);

//        radioGroup.clearCheck();
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
//        Toast.makeText(context, idx + "", Toast.LENGTH_SHORT).show();

        rb_Tag = idx;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId != -1) {
//                    Toast.makeText(context, rb.getText() + "" + radioGroup.indexOfChild(rb), Toast.LENGTH_SHORT).show();
                    rb_Tag = radioGroup.indexOfChild(rb);
                }

            }
        });
        tv_startup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPreferences.setInt(context, Constant.STARTUP_ID, rb_Tag);
                Toast.makeText(context, rb_Tag + "", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void RingToneDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_select_ringtone);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        cancel_ringtone = dialog.findViewById(R.id.cancel_ringtone);
        cancel_ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void AlertDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        cancel_alert = dialog.findViewById(R.id.cancel_alert);
//        set_ringtone = dialog.findViewById(R.id.set_ringtone);
//        set_ringtone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RingToneDialog();
//
//            }
//        });
        cancel_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void RingtoneDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        cancel_alert = dialog.findViewById(R.id.cancel_alert);
        cancel_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void PortDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_port);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        TextView cancel = dialog.findViewById(R.id.cancel_port);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void MapSettingDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_map_setting);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        TextView setting = dialog.findViewById(R.id.cancel_map_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_up_layout:
                StartUpDialog();
                break;
            case R.id.map_setting_layout:
                MapSettingDialog();
                break;
            case R.id.alert_layout:
                AlertDialog();
                break;
            case R.id.port_layout:
                PortDialog();
                break;
        }
    }

    public void gotoPermissionPage(View view) {
        Intent intent = new Intent(getApplicationContext(), PermissionActivity.class);
        startActivity(intent);
    }
}
