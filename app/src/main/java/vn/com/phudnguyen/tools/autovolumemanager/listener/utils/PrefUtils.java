package vn.com.phudnguyen.tools.autovolumemanager.listener.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

public class PrefUtils {

    private static String TAG = PrefUtils.class.getName();

    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getSleepInterval(Context context) {
        return getPrefs(context).getInt("sleep_interval", 0);
    }

    public static void setSleepInterval(Context context, int interval) {
        getPrefs(context).edit().putInt("sleep_interval", interval).apply();
    }

    public static boolean isNotificationLogEnabled(Context context) {
        return getPrefs(context).getBoolean("notification_log_enabled", false);
    }

    public static boolean isNotificationLogEnabled(Context context, String packageName) {
        SharedPreferences prefs = getPrefs(context);
        String logKey = getApplicationLogEnabledKey(packageName);
        Log.w(TAG, "check notification enable for " + logKey);
        return prefs.getBoolean("notification_log_enabled", false)
                && prefs.getBoolean(logKey, false);
    }

    public static String getApplicationLogEnabledKey(String packageName) {
        return "notification_monitor_" + packageName + "_enabled";
    }
}
