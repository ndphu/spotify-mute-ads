package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.GsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public class PackageListFragment extends Fragment {
    private String TAG = PackageListFragment.class.getName();

    private static final String ARG_ACTIONS = "actions";
    private OnListFragmentInteractionListener listener;
    private PackageListRecyclerViewAdapter packageListAdapter;
    private PackageInfo[] packageInfos;

    public PackageListFragment() {
    }

    public static PackageListFragment newInstance(PackageInfo[] packageInfos) {
        PackageListFragment fragment = new PackageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTIONS, GsonUtils.serialize(packageInfos));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            packageInfos = GsonUtils.deserizalize(getArguments().getString(ARG_ACTIONS), PackageInfo[].class);
        }

        packageListAdapter = new PackageListRecyclerViewAdapter();
        packageListAdapter.setListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(packageListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new PackageLoaderTask(new PackageLoaderTask.EventLoaderTaskCallback() {
            @Override
            public void onCompleted(List<PackageInfo> infos) {
                Log.i(TAG, "Infos = " + infos);
                packageListAdapter.setValues(infos);
                packageListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    private static class PackageLoaderTask extends AsyncTask<Void, Void, List<PackageInfo>> {
        private final EventLoaderTaskCallback callback;

        public interface EventLoaderTaskCallback {
            void onCompleted(List<PackageInfo> packageInfos);
        }

        public static final String TAG = PackageLoaderTask.class.getName();

        public PackageLoaderTask(EventLoaderTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<PackageInfo> doInBackground(Void... actions) {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            try {
                return instance.getAllNotificationPackage();
            } catch (InvocationTargetException | java.lang.InstantiationException | ParseException | IllegalAccessException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(List<PackageInfo> packageInfos) {
            super.onPostExecute(packageInfos);
            Log.i(TAG, "Found " + packageInfos.size() + " packageInfos");
            this.callback.onCompleted(packageInfos);
        }
    }

    public void setOnListFragmentInteractionListener(OnListFragmentInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PackageInfo item);
    }
}
