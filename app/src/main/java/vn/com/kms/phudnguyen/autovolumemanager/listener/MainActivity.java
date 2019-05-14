package vn.com.kms.phudnguyen.autovolumemanager.listener;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import org.apache.commons.lang3.StringUtils;
import vn.com.kms.phudnguyen.autovolumemanager.R;
import vn.com.kms.phudnguyen.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.kms.phudnguyen.autovolumemanager.listener.fragment.EventFragment;
import vn.com.kms.phudnguyen.autovolumemanager.listener.fragment.HomeFragment;
import vn.com.kms.phudnguyen.autovolumemanager.listener.fragment.RuleFragment;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Event;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Rule;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener {
    private String TAG = MainActivity.class.getName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new RuleFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new EventFragment();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Event item) {

    }
}
