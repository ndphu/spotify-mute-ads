package vn.com.phudnguyen.tools.autovolumemanager.listener;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.*;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.event.EventFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.event.EventViewerFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification.NotificationViewerFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.rule.RuleFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Event;
import vn.com.phudnguyen.tools.autovolumemanager.listener.prefs.AppPickerFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.prefs.AppPickerPreferenceFragment;
import vn.com.phudnguyen.tools.autovolumemanager.listener.prefs.MainPreferenceFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener,
    MainPreferenceFragment.AppPreferenceFragmentListener {
    private String TAG = MainActivity.class.getName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MainPreferenceFragment();
//                    fragment = new AppPickerFragment();
                    getSupportActionBar().setTitle("Home");
                    break;
                case R.id.navigation_notifications:
                    fragment = new NotificationViewerFragment();
                    getSupportActionBar().setTitle("Notification Log");
                    break;
                case R.id.navigation_dashboard:
                    fragment = new RuleFragment();
                    getSupportActionBar().setTitle("Rule");
                    break;
                case R.id.navigation_log:
                    fragment = new EventViewerFragment();
                    getSupportActionBar().setTitle("Service Log");
                    break;
            }
            if (fragment != null) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
            return false;
        }
    };
    private Menu menu;

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
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.replace(R.id.fragment_placeholder, new AppPickerFragment());
        trans.addToBackStack("home_fragment");
        trans.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public void showSearchView(final AppPickerFragment fragment) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.expandActionView();
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getSupportFragmentManager().popBackStack();
                return true;
            }
        });

        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                fragment.onFilterChanged(s);
                return true;
            }
        });
    }

    public void hideSearchView(AppPickerFragment fragment) {
        this.menu.clear();
    }
}
