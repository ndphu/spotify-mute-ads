package vn.com.phudnguyen.tools.autovolumemanager.listener.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public static int getSleepInterval(Context context) {
        return getPrefs(context).getInt("sleep_interval", 0);
    }

    public static void setSleepInterval(Context context, int interval) {
        getPrefs(context).edit().putInt("sleep_interval", interval).apply();
    }
}
