package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.MainActivity;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;

public class NotificationViewerFragment extends Fragment implements PackageListFragment.OnListFragmentInteractionListener {

    private MainActivity context;

    public NotificationViewerFragment() {
    }

    public static NotificationViewerFragment newInstance() {
        NotificationViewerFragment fragment = new NotificationViewerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PackageListFragment packageListFragment = new PackageListFragment();
        packageListFragment.setOnListFragmentInteractionListener(this);
        this.context = (MainActivity) context;
        this.context.getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_placeholder, packageListFragment)
            .commit();
    }

    @Override
    public void onListFragmentInteraction(PackageInfo item) {
        this.context.getSupportFragmentManager()
            .beginTransaction().replace(R.id.fragment_placeholder, NotificationListFragment.newInstance(item))
            .addToBackStack(NotificationListFragment.class.getName())
            .commit();
    }
}
