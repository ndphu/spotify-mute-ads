package vn.com.phudnguyen.tools.autovolumemanager.listener;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.*;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Event;
import vn.com.phudnguyen.tools.autovolumemanager.listener.prefs.AppPickerPreferenceFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.prefs.AppPreferenceFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener,
        AppPreferenceFragment.AppPreferenceFragmentListener {
    private String TAG = MainActivity.class.getName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new AppPreferenceFragment();
//                    fragment = new AppPickerPreferenceFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new RuleFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new EventViewerFragment();
                    break;
            }
            if (fragment != null) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Event item) {

    }

    @Override
    public void onOpenApplicationPicker() {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
        trans.replace(R.id.fragment_placeholder, new AppPickerPreferenceFragment());
        trans.addToBackStack("home_fragment");
        trans.commit();
    }
}
