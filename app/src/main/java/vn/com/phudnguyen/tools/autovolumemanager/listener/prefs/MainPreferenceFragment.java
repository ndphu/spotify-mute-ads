package vn.com.phudnguyen.tools.autovolumemanager.listener.prefs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.view.View;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.ListenerService;

public class MainPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private static final String TAG = MainPreferenceFragment.class.getName();
    private AppPreferenceFragmentListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppPreferenceFragmentListener) {
            listener = (AppPreferenceFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.app_preference, rootKey);

        findPreference(getString(R.string.notification_access_permission_granted)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                return false;
            }
        });

        findPreference("volume_control_sleep_interval").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.i(TAG, "" + o);
                try {
                    Integer newValue = Integer.valueOf((String) o);
                    if (newValue >= 0) {
                        preference.setSummary(o + " ms");
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        });

        getPreferenceScreen().findPreference("monitoring_select_app").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (listener != null) {
                    listener.onOpenApplicationPicker();
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SwitchPreferenceCompat preference = (SwitchPreferenceCompat) getPreferenceScreen().findPreference(getString(R.string.notification_access_permission_granted));
        String enabledNotificationListeners =
            android.provider.Settings.Secure.getString(
                getActivity().getContentResolver(),"enabled_notification_listeners");
        if (StringUtils.contains(enabledNotificationListeners, ListenerService.class.getCanonicalName())) {
            preference.setChecked(true);
        } else {
            preference.setChecked(false);
        }
        Preference interval = findPreference("volume_control_sleep_interval");
        interval.setSummary(((EditTextPreference)interval).getText() + " ms");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(((EditTextPreference)preference).getText());
        }
        return true;
    }

    public static interface AppPreferenceFragmentListener {
        void onOpenApplicationPicker();
    }

}
