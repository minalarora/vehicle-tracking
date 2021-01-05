package com.immortal.vehicletracking.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private final String MY_PREFERENCES = "vehicle";

    private static UserPreferences userPreferences = new UserPreferences();

    UserPreferences() {
    }

    public static UserPreferences getUserPreferences() {
        if (userPreferences != null) {
            return userPreferences;
        } else {
            return new UserPreferences();
        }
    }

    public SharedPreferences getSharedPref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return pref;
    }


    public void setString(Context mContext, String key, String value) {
        if (key != null) {
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putString(key, value);
            edit.commit();
        }
    }

    public void setInt(Context mContext, String key, int value) {
        if (key != null) {
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putInt(key, value);
            edit.commit();
        }
    }

    public void setLong(Context mContext, String key, Long value) {
        if (key != null) {
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putLong(key, value);
            edit.commit();
        }
    }

    public void setBoolean(Context mContext, String key, boolean value) {
        if (key != null) {
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putBoolean(key, value);
            edit.commit();
        }
    }


    public String getString(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        String val = null;
        try {
            if (pref.contains(key))
                val = pref.getString(key, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public int getInt(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        int val = 0;
        try {
            if (pref.contains(key)) val = pref.getInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public Long getLong(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        Long val = null;
        try {
            if (pref.contains(key)) val = pref.getLong(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public boolean getBoolean(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        boolean val = false;
        try {
            if (pref.contains(key)) val = pref.getBoolean(key, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public boolean containkey(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        return pref.contains(key);
    }

   /* public void setObject(Context context, String key, List<Object> data) {
//Set the values
        Gson gson = new Gson();
        List<Object> textList = new ArrayList<>();
        textList.addAll(data);
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        String jsonText = gson.toJson(textList);
        edit.putString(key, jsonText);
        edit.apply();
    }

    public Object[] getObject(Context context, String key) {
        //Retrieve the values
        Gson gson = new Gson();
        SharedPreferences pref = context.getSharedPreferences(SyncStateContract.Constants.MY_PRERENCES, Context.MODE_PRIVATE);
        String jsonText = pref.getString(key, null);
        return gson.fromJson(jsonText, Object[].class);  //EDIT: gso to gson
    }*/

    public void clearPrefrences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public void setNotification(Context mContext, String key, boolean value) {
        if (key != null) {
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putBoolean(key, value);
            edit.commit();
        }
    }

    public boolean getNotification(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        boolean val = false;
        try {
            if (pref.contains(key)) val = pref.getBoolean(key, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
}
