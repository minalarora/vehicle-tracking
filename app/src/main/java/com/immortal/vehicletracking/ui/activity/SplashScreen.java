package com.immortal.vehicletracking.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.UserPreferences;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static com.immortal.vehicletracking.utils.Utils.isNetworkAvailable;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView;
    private Animation animSlide;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Refer the ImageView like this
        imageView = (ImageView) findViewById(R.id.img);

// Load the animation like this
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide);
        requestMultiplePermissions();

    }

    private void startHandler() {
        imageView.startAnimation(animSlide);
//        int PROGRESS_TIME_OUT = 2000;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                finish();
//            }
//        }, PROGRESS_TIME_OUT);
        int PROGRESS_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean result = isNetworkAvailable(SplashScreen.this);
                if (result) {

                    if (userPreferences.getString(SplashScreen.this, Constant.USER_ID) != null) {
                        startActivity(new Intent(SplashScreen.this, Home.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    try {
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SplashScreen.this).create();

                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        alertDialog.show();
                    } catch (Exception e) {
                        Log.d("Tag", "Show Dialog: " + e.getMessage());
                    }
                }
            }
        }, PROGRESS_TIME_OUT);

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            startHandler();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startHandler();
            }
        });
        builder.show();
    }
}
