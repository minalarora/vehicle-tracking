package com.immortal.vehicletracking.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.immortal.vehicletracking.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils implements Constants {

    //Fragments Tags
    public static final String HomeFragment = "Home Fragment";
    public static final String CatogaryDataFragment = "CatogaryData Fragment";
    public static final String ProductDetailFragment = "Product Detail Fragment";
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    public static final String DueBalanceFragment = "DueBalanceFragment";
    public static final String CartFragment = "CartFragment";
    public static final String PaymentModeFragment = "PaymentModeFragment";
    public static final String HistoryFragment = "HistoryFragment";
    public static final String HistoryDetailFragment = "HistoryDetailFragment";


    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";


    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getFormattedDate(Calendar calendar) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String decodeUrl(String pro_desc) {
        String after_decode = null;
        try {
            pro_desc = pro_desc.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            pro_desc = pro_desc.replaceAll("\\+", "%2B");
            after_decode = URLDecoder.decode(pro_desc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return after_decode;
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static int ac_alarm_status(String key) {
        int status = 0;
        switch (key) {
            case "AC_HIGH":
                status = R.drawable.ac_icon_green;
                break;
            case "AC_LOW":
                status = R.drawable.ac_icon_red;
                break;
            case "OK":
                Log.e("ac_status", key + "-");
                status = R.drawable.ac_icon_red;
                break;
        }
        return status;
    }

    public static int gps_alarm_status(String key) {
        int status = 0;
        switch (key) {
            case "GPS_TRACKING_ON":
                status = R.drawable.gps_icon_green;
                break;
            case "GPS_TRACKING_OFF":
                status = R.drawable.gps_icon_red;
                break;
            case "OK":
                status = R.drawable.gps_icon_blank;
                break;
        }
        return status;
    }

    public static int fuel_alarm_status(String key) {
        int status = 0;

        switch (key) {
            case "GAS_OIL_AND_ELECTRICITY_CONNECTED":
                status = R.drawable.fuel_icon_green;
                break;
            case "OIL_AND_ELECTRICITY_DISCONNECTED":
                status = R.drawable.fuel_icon_red;
                break;
            case "OK":
                status = R.drawable.fuel_icon_blank;
                break;
        }
        return status;
    }

    public static int charge_alarm_status(String key) {
        int status = 0;

        switch (key) {
            case "CHARGE_ON":
                status = R.drawable.battery_icon_green;
                break;
            case "CHARGE_OFF":
                status = R.drawable.battery_icon_red;
                break;
            case "OK":
                status = R.drawable.battery_icon_blank;
                break;
        }
        return status;
    }

    public static int gsm_alarm_status(String key) {
        int status = 0;

        switch (key) {
            case "STRONG_SIGNAL":
                status = R.drawable.ignition_icon_green;
                break;
            case "GOOD_SIGNAL":
                status = R.drawable.ignition_icon_red;
                break;
            case "":
                status = R.drawable.ignition_icon_blank;
                break;
        }
        return status;
    }
}