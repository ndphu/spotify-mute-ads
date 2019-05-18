package vn.com.phudnguyen.tools.autovolumemanager.listener.prefs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import org.apache.commons.lang3.StringUtils;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.PrefUtils;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppPickerPreferenceFragment extends PreferenceFragmentCompat {
    private String TAG = AppPickerPreferenceFragment.class.getName();
    private ProgressDialog loadingDialog;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        new AppLoaderTask(this).execute();
    }

    private static final class AppLoaderTask extends AsyncTask<Void, Void, PreferenceScreen> {
        final WeakReference<AppPickerPreferenceFragment> invoker;
        private WeakReference<PreferenceScreen> screen;
        private WeakReference<Context> context;

        AppLoaderTask(AppPickerPreferenceFragment invoker) {
            this.invoker = new WeakReference<>(invoker);
            context = new WeakReference<>(invoker.getPreferenceManager().getContext());
            screen = new WeakReference<>(invoker.getPreferenceManager().createPreferenceScreen(context.get()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.invoker.get().showLoading();

        }

        @Override
        protected PreferenceScreen doInBackground(Void... voids) {
            final PackageManager pm = context.get().getPackageManager();
            final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            Collections.sort(packages, new Comparator<ApplicationInfo>() {
                @Override
                public int compare(ApplicationInfo o1, ApplicationInfo o2) {
                    String title1 = pm.getApplicationLabel(o1).toString();
                    String title2 = pm.getApplicationLabel(o2).toString();
                    return StringUtils.compare(title1.toLowerCase(), title2.toLowerCase());
                }
            });
            for (ApplicationInfo packageInfo : packages) {
                SwitchPreferenceCompat appPref = new SwitchPreferenceCompat(context.get());
                appPref.setKey(PrefUtils.getApplicationLogEnabledKey(packageInfo.packageName));
                appPref.setTitle(pm.getApplicationLabel(packageInfo));
                appPref.setSummary(packageInfo.packageName);
                appPref.setIcon(packageInfo.loadIcon(pm));
                screen.get().addPreference(appPref);
            }
            return screen.get();
        }

        @Override
        protected void onPostExecute(PreferenceScreen screen) {
            super.onPostExecute(screen);
            if (this.invoker.get() != null) {
                this.invoker.get().setPreferenceScreen(screen);
                this.invoker.get().hideLoading();
            }
        }
    }

    private void showLoading() {
        loadingDialog = ProgressDialog.show(getContext(), "", "Loading installed applications", true, false);
    }

    private void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
