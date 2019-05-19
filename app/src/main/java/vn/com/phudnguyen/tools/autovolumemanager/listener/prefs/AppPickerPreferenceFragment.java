package vn.com.phudnguyen.tools.autovolumemanager.listener.prefs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.PrefUtils;

import java.util.List;

public class AppPickerPreferenceFragment extends PreferenceFragmentCompat {
    private String TAG = AppPickerPreferenceFragment.class.getName();

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext()));
    }

    public void loadPackages(List<PackageInfo> packages) {
        getPreferenceScreen().removeAll();
        for (PackageInfo packageInfo : packages) {
            SwitchPreferenceCompat appPref = new SwitchPreferenceCompat(getPreferenceManager().getContext());
            appPref.setKey(PrefUtils.getApplicationLogEnabledKey(packageInfo.getPackageName()));
            appPref.setTitle(packageInfo.getName());
            appPref.setSummary(packageInfo.getPackageName());
            appPref.setIcon(packageInfo.getIcon());
            getPreferenceScreen().addPreference(appPref);
        }
    }
}
