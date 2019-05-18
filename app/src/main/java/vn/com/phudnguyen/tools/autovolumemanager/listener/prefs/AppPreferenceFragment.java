package vn.com.phudnguyen.tools.autovolumemanager.listener.prefs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import vn.com.phudnguyen.tools.autovolumemanager.R;

public class AppPreferenceFragment extends PreferenceFragmentCompat {

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

    public static interface AppPreferenceFragmentListener {
        void onOpenApplicationPicker();
    }

}
