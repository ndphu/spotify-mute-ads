package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.event;

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
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Event;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.EventAction;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.GsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public class EventFragment extends Fragment {

    private static final String ARG_ACTIONS = "actions";
    private OnListFragmentInteractionListener mListener;
    private EventRecyclerViewAdapter eventAdapter;
    private EventAction[] actions;

    public EventFragment() {
    }

    public static EventFragment newInstance(EventAction[] actions) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTIONS, GsonUtils.serialize(actions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            actions = GsonUtils.deserizalize(getArguments().getString(ARG_ACTIONS), EventAction[].class);
        }

        eventAdapter = new EventRecyclerViewAdapter();
        eventAdapter.setListener(mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(eventAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new EventLoaderTask(new EventLoaderTask.EventLoaderTaskCallback() {
            @Override
            public void onCompleted(List<Event> events) {
                eventAdapter.setValues(events);
                eventAdapter.notifyDataSetChanged();
            }
        }).execute(actions);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static class EventLoaderTask extends AsyncTask<EventAction[], Void, List<Event>> {
        private final EventLoaderTaskCallback callback;

        public interface EventLoaderTaskCallback {
            void onCompleted(List<Event> rules);
        }

        public static final String TAG = EventLoaderTask.class.getName();

        public EventLoaderTask(EventLoaderTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Event> doInBackground(EventAction[]... actions) {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            try {
                return instance.getAllEventsByTypes(actions[0]);
            } catch (IllegalAccessException | InvocationTargetException | java.lang.InstantiationException | ParseException e) {
                Log.e(TAG, "Fail to get all event", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            Log.i(TAG, "Found " + events.size() + " events");
            this.callback.onCompleted(events);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event item);
    }
}
