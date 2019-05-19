package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.NotificationLog;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public class NotificationListFragment extends Fragment {

    private NotificationRecyclerViewAdapter adapter;
    private PackageInfo packageInfo;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NotificationListFragment() {
    }

    public static NotificationListFragment newInstance(PackageInfo info) {
        NotificationListFragment fragment = new NotificationListFragment();
        fragment.packageInfo = info;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new NotificationRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @SuppressLint("StaticFieldLeak")
    private void load() {
        if (packageInfo != null) {
            new AsyncTask<Void, Void, List<NotificationLog>>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    swipeRefreshLayout.setRefreshing(true);
                }

                @Override
                protected List<NotificationLog> doInBackground(Void... voids) {
                    DatabaseHelper instance = DatabaseHelper.getInstance();
                    try {
                        return instance.getNotificationLogForPackage(packageInfo.getPackageName(), 0, 1000);
                    } catch (InvocationTargetException | java.lang.InstantiationException | ParseException | IllegalAccessException e) {
                        return Collections.emptyList();
                    }
                }

                @Override
                protected void onPostExecute(List<NotificationLog> notificationLogs) {
                    super.onPostExecute(notificationLogs);
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setItems(notificationLogs);
                    adapter.notifyDataSetChanged();
                }
            }.execute();
        }
    }
}
