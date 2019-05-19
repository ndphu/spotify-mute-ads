package vn.com.phudnguyen.tools.autovolumemanager.listener.prefs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.apache.commons.lang3.StringUtils;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.MainActivity;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppPickerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private AppPickerPreferenceFragment appPickerPreferenceFragment;
    private MainActivity mainActivity;
    private String filterText;

    public AppPickerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_picker, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        appPickerPreferenceFragment = (AppPickerPreferenceFragment) getChildFragmentManager()
            .findFragmentById(R.id.app_picker_preference_fragment);
    }


    @Override
    public void onResume() {
        super.onResume();
        synchronized (PackageInfo.CACHES) {
            if (PackageInfo.CACHES.size() == 0) {
                loadAllPackages();
            } else {
                showFilteredPackageList();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllPackages() {
        final PackageManager pm = getActivity().getPackageManager();
        new AsyncTask<Void, Void, List<PackageInfo>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected List<PackageInfo> doInBackground(Void... voids) {
                final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                Collections.sort(packages, new Comparator<ApplicationInfo>() {
                    @Override
                    public int compare(ApplicationInfo o1, ApplicationInfo o2) {
                        String title1 = pm.getApplicationLabel(o1).toString();
                        String title2 = pm.getApplicationLabel(o2).toString();
                        return StringUtils.compare(title1.toLowerCase(), title2.toLowerCase());
                    }
                });

                List<PackageInfo> result = new ArrayList<>();
                for (ApplicationInfo ap : packages) {
                    result.add(PackageInfo.builder()
                        .name(pm.getApplicationLabel(ap).toString())
                        .packageName(ap.packageName)
                        .icon(ap.loadIcon(pm)).build());
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<PackageInfo> packages) {
                super.onPostExecute(packages);
                swipeRefreshLayout.setRefreshing(false);
                synchronized (PackageInfo.CACHES) {
                    PackageInfo.CACHES.clear();
                    PackageInfo.CACHES.addAll(packages);
                }
                showFilteredPackageList();
            }
        }.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainActivity.showSearchView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity.hideSearchView(this);
    }

    @Override
    public void onRefresh() {
        loadAllPackages();
    }

    public void onFilterChanged(String filterText) {
        this.filterText = filterText;
        showFilteredPackageList();
    }

    private void showFilteredPackageList() {
        if (StringUtils.isBlank(this.filterText)) {
            appPickerPreferenceFragment.loadPackages(PackageInfo.CACHES);
        } else {
            List<PackageInfo> filtered = new ArrayList<>();
            for (PackageInfo packageInfo : PackageInfo.CACHES) {
                if (StringUtils.containsIgnoreCase(packageInfo.getName(), this.filterText)
                    || StringUtils.containsIgnoreCase(packageInfo.getPackageName(), this.filterText)) {
                    filtered.add(packageInfo);
                }
            }
            appPickerPreferenceFragment.loadPackages(filtered);
        }
    }
}
