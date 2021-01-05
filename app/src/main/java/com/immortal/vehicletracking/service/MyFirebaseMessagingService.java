package com.immortal.vehicletracking.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.ui.activity.Home;
import com.immortal.vehicletracking.utils.Config;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.NotificationUtils;
import com.immortal.vehicletracking.utils.UserPreferences;


import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    public static int notification_id;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("remoteMessage", "From:->> " + remoteMessage);
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //  Log.e(TAG, "key" + json.getJSONObject("cases").getString("key"));
//                if (json.getJSONObject("cases").getString("key").equals("5")) {
//                    cancelNotification(getApplicationContext(), json.getJSONObject("cases").getInt("value"));
//                    Log.e("if", "if");
//                } else {
//                    Log.e("else", "else");
                handleDataMessage(json);
//                }
            } catch (Exception e) {
                Log.e(TAG, "Exception:67 " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Data Payload:->else " + remoteMessage.getData().toString());
        }
    }

    private void handleNotification(String message) {
        Log.e("enter Notification", message);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {

            // If the app is in background, firebase itself handles the notification
        }
    }


    private void handleDataMessage(JSONObject json) {

        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = "";
            String timestamp = "";
//            JSONObject payload = json.getJSONObject("cases");
//            String case_id = payload.getString("value");
//            String key = payload.getString("key");
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);

            //startcountDown(case_id);
//
//            if (key.equals("5")) {
//                Log.e("Enter", "Enter");
//                cancelNotification(getApplicationContext(), Integer.parseInt(case_id));
//            }

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e("msg", "onMessageReceived: message");
                Intent intent;
//                if (key.equals("1")) {
//                    Log.e("1->>>>>if", "key-" + case_id);
//                    intent = new Intent(this, Home.class);
//                    intent.putExtra("case_id", case_id + "");
//                    intent.putExtra("user_id", "0");
//                } else {
                intent = new Intent(this, Home.class);
//                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent);
                ;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                // manager.notify(Integer.parseInt(case_id), builder.build());

                Log.e("background", "background");
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                Intent resultIntent;

                // app is in background, show the notification in notification tray
                if (true) {
                    //  firebase_case_id = case_id;
//                    Log.e("1->>>>>else", "key-" + case_id);
                    resultIntent = new Intent(getApplicationContext(), Home.class);
//                    resultIntent.putExtra("case_id", case_id + "");
//                    resultIntent.putExtra("user_id", "0");

                } else {
//                    Log.e("if", "resultIntent-" + case_id);
                    resultIntent = new Intent(getApplicationContext(), Home.class);
                }
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "Refreshed token: " + s);
        super.onNewToken(s);
        sendRegistrationToServer(s);
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        userPreferences.setString(getApplicationContext(), Constant.FIREBASE_TOKEN, token);

    }
}