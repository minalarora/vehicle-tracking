package com.immortal.vehicletracking.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.ui.fragment.HomeFragment;
import com.immortal.vehicletracking.ui.fragment.HomeListFragment;
import com.immortal.vehicletracking.ui.fragment.HomeRoundFragment;
import com.immortal.vehicletracking.ui.fragment.StoppageSummaryFragment;
import com.immortal.vehicletracking.ui.fragment.TravelSummaryFragment;
import com.immortal.vehicletracking.ui.fragment.VehicleStatusFragment;
import com.immortal.vehicletracking.utils.Config;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.NotificationUtils;
import com.immortal.vehicletracking.utils.UserPreferences;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.immortal.vehicletracking.helperClass.ApiConstants.UPDATE_TOKEN;
import static com.immortal.vehicletracking.utils.Constant.TIMEOUT_VOLLY;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //    private Context context;
    private static FragmentManager fragmentManager;
    private Fragment fragment;
    private FrameLayout fragmentContainer;
    private Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = Home.class.getSimpleName();
    private Context context;
    private TextView user_name, user_email;
    boolean doubleBackToExitPressedOnce = false;
    public Dialog dialog;
    TextView cancel,tv_startup_save;
    private int rb_Tag;
    private RadioGroup radioGroup;

    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragmentintoHomeScreen();
                    return true;
                case R.id.navigation_status:
                    fragment = new StoppageSummaryFragment();
                    replaceHomeFragment(fragment,"StoppageSummary Fragment");
                    return true;
                case R.id.navigation_summary:
                    fragment = new TravelSummaryFragment();
                    replaceHomeFragment(fragment,"TravelSummary Fragment");
                    return true;
                case R.id.navigation_alert:
                    intent = new Intent(Home.this, AlertActivity.class);
                    startActivity(intent);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = Home.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_bottom_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View header_view = navigationView.getHeaderView(0);
        user_name = header_view.findViewById(R.id.htv_user_name);
        user_email = header_view.findViewById(R.id.htv_user_email);
        setheaderdata();
        navigationView.setNavigationItemSelectedListener(this);
        showFirebaseToken();
        loadFragmentintoHomeScreen();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("recive", intent.getAction() + "");

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Toast.makeText(getApplicationContext(), "Push notification: ", Toast.LENGTH_LONG).show();
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    //    UpdateETADialog();
                    // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setheaderdata() {
        user_name.setText(userPreferences.getString(context, Constant.USER_NAME));
        user_email.setText(userPreferences.getString(context, Constant.USER_EMAIL));

    }

    private void loadFragmentintoHomeScreen() {
        Fragment fragment = null;
        fragmentManager = getSupportFragmentManager();
        if (userPreferences.getInt(context, Constant.STARTUP_ID) == 0) {
            Log.e("User->>0", userPreferences.getInt(context, Constant.STARTUP_ID) + "");
            fragment = new HomeFragment();
            replaceHomeFragment(fragment,"Home Fragment");
        } else if (userPreferences.getInt(context, Constant.STARTUP_ID) == 1) {
            Log.e("User->>1", userPreferences.getInt(context, Constant.STARTUP_ID) + "");
            fragment = new HomeListFragment();
            replaceHomeFragment(fragment,"HomeList Fragment");
        } else if (userPreferences.getInt(context, Constant.STARTUP_ID) == 2) {
            Log.e("User->>2", userPreferences.getInt(context, Constant.STARTUP_ID) + "");
            fragment = new HomeRoundFragment();
            replaceHomeFragment(fragment, "HomeRound Fragment");
        } else if (userPreferences.getInt(context, Constant.STARTUP_ID) == 3) {
            Log.e("User->>3", userPreferences.getInt(context, Constant.STARTUP_ID) + "");
            fragment = new VehicleStatusFragment();
            replaceHomeFragment(fragment,"VehicleStatus Fragment");
//        } else if (userPreferences.getInt(context, Constant.STARTUP_ID) == 4) {
//            Log.e("User->>4", userPreferences.getInt(context, Constant.STARTUP_ID) + "");
//            intent = new Intent(context, LiveTracking.class);
//            startActivity(intent);
        }
    }

    private void showFirebaseToken() {
        Log.e("FIREBASE_TOKEN->>", userPreferences.getString(context, Constant.FIREBASE_TOKEN));
        sendTokenToServer(userPreferences.getString(context, Constant.USER_ID), userPreferences.getString(context, Constant.FIREBASE_TOKEN));
    }

    private void sendTokenToServer(String user, String token) {
        MyProgressDialog.showPDialog(context);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("user", user + "");
            postparams.put("token", token + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_TOKEN, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Home->FireBase->", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
//                                new CustomToast().Show_Toast(getActivity(), view,
//                                        response.getString("message"));
//                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
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
                        Log.e("Home->FireBase", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_VOLLY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
//        dayInStatus(regId);
        Log.e(TAG, "Firebase reg id: " + regId);

    }

    public void onBackPressed() {
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag("Home Fragment");
        Fragment findFragmentByTag2 = fragmentManager.findFragmentByTag("HomeList Fragment");
        Fragment findFragmentByTag3 = fragmentManager.findFragmentByTag("HomeRound Fragment");
        Fragment VehicleStatusFragment2 = fragmentManager.findFragmentByTag("VehicleStatus Fragment");
        Fragment StoppageSummaryFragment2 = fragmentManager.findFragmentByTag("StoppageSummary Fragment");
        Fragment TravelSummaryFragment2 = fragmentManager.findFragmentByTag("TravelSummary Fragment");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else {
            String str = "back";
            if (VehicleStatusFragment2 != null) {
                loadFragmentintoHomeScreen();
                Log.e("quizUpcoming", str);
            } else if (StoppageSummaryFragment2 != null) {
                loadFragmentintoHomeScreen();
                Log.e("QuizPlayFragment", str);
            } else if (TravelSummaryFragment2 != null) {
                loadFragmentintoHomeScreen();
                Log.e("QuizPlayResultFragment", str);
            } else {
                Log.e("else", str);
                if (this.doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Home.this.doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else
        if (id == R.id.action_notifications) {
            intent = new Intent(context, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            StartUpDialog();
//            fragment = new HomeFragment();
//            replaceFragment(fragment);
//        } else if (id == R.id.nav_home_2) {
//            fragment = new HomeChartFragment();
//            replaceFragment(fragment);
//        } else if (id == R.id.nav_home_3) {
//            fragment = new HomeListFragment();
//            replaceFragment(fragment);
        } else if (id == R.id.nav_live_tracking) {
            intent = new Intent(this, LiveTracking.class);
            startActivity(intent);
        } else if (id == R.id.nav_vehicle_status) {
            fragment = new VehicleStatusFragment();
            replaceFragment(fragment,"VehicleStatus Fragment");
        } else if (id == R.id.nav_travel_summary) {
            fragment = new TravelSummaryFragment();
            replaceFragment(fragment, "TravelSummary Fragment");
        } else if (id == R.id.nav_stoppage_summary) {
            fragment = new StoppageSummaryFragment();
            replaceFragment(fragment, "StoppageSummary Fragment");
        } else if (id == R.id.nav_distance_summary) {
            intent = new Intent(this, DistanceSummaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_alert) {
            intent = new Intent(this, AlertActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_changepass) {
            this.intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(this.intent);

        } else if (id == R.id.nav_log) {
            userPreferences.setString(Home.this, Constant.USER_ID, null);
            Intent intent = new Intent(Home.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, String Tag) {
        if (!fragment.isAdded()) {
            fragmentManager
                    .beginTransaction().addToBackStack(null)
                    .replace(R.id.fragmentContainer, fragment,
                            Tag).commit();
        }
    }

    public static void replaceTotalFragment(Fragment fragment) {

        if (!fragment.isAdded()) {
            fragmentManager
                    .beginTransaction().addToBackStack(null)
                    .replace(R.id.fragmentContainer, fragment,
                            "Accepted Jobs").commit();
        }
    }

    public void replaceHomeFragment(Fragment fragment,String tag) {
        if (!fragment.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment,
                            tag).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: ");
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        Log.e("pause", "pause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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
}
