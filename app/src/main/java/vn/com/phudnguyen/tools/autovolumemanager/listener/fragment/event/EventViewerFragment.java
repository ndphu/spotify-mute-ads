package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.event;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.EventAction;

public class EventViewerFragment extends Fragment {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public EventViewerFragment() {
    }

    public static EventViewerFragment newInstance() {
        EventViewerFragment fragment = new EventViewerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = view.findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        PagerTabStrip pagerTabStrip = mViewPager.findViewById(R.id.pager_title_strip);
        int stripColor = ContextCompat.getColor(view.getContext(), R.color.colorAccent);
        pagerTabStrip.setTabIndicatorColor(stripColor);
        pagerTabStrip.setTextColor(stripColor);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            EventAction[] actions = null;
            switch (position) {
                case 0:
                    actions = new EventAction[]{EventAction.SERVICE_STARTED, EventAction.SERVICE_STOPPED};
                    break;
                case 1:
                    actions = new EventAction[]{EventAction.MUTED, EventAction.RESTORED};
                    break;
            }
            return EventFragment.newInstance(actions);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Service Logs";
                case 1:
                    return "Action Logs";
            }
            return "";
        }
    }
}
