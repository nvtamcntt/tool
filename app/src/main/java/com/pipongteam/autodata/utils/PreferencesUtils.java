package com.pipongteam.autodata.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private static final String TAG = "PreferencesUtils";

    public static final String PREFNAME = "pipong_team";
    public static final String ENABLE_SERVICE = "enable_service";
    public static final String ENABLE_USER = "enable_user";

    public synchronized static boolean getSharedPrefAppEnable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFNAME, context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        boolean enableApp = prefs.getBoolean(ENABLE_SERVICE, false) ;
        return enableApp;
    }

    public synchronized static boolean getSharedPrefUserEnable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFNAME, context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        boolean enableApp = prefs.getBoolean(ENABLE_SERVICE, false) ;
        boolean enableUser = prefs.getBoolean(ENABLE_USER, false);
        return enableApp && enableUser;
    }

    public synchronized static void setSharedPrefUserEnable(Context context, boolean active) {
        SharedPreferences prefs = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(ENABLE_USER, active);
        editor.apply();
    }
    public synchronized static void setSharedPrefAppEnable(Context context, boolean active) {
        SharedPreferences prefs = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(ENABLE_SERVICE, active);
        editor.apply();
    }
}

