package vn.com.kms.phudnguyen.autovolumemanager.listener.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.kms.phudnguyen.autovolumemanager.R;
import vn.com.kms.phudnguyen.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Event;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private EventRecyclerViewAdapter eventAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        eventAdapter = new EventRecyclerViewAdapter();
        eventAdapter.setListener(mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(eventAdapter);
        }
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

        new EventLoaderTask(new EventLoaderTask.EventLoaderTaskCallback() {
            @Override
            public void onCompleted(List<Event> events) {
                eventAdapter.setValues(events);
                eventAdapter.notifyDataSetChanged();
            }
        }).execute(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static class EventLoaderTask extends AsyncTask<Context, Void, List<Event>> {
        private final EventLoaderTaskCallback callback;

        public interface EventLoaderTaskCallback {
            void onCompleted(List<Event> rules);
        }

        public static final String TAG = EventLoaderTask.class.getName();

        public EventLoaderTask(EventLoaderTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Event> doInBackground(Context... contexts) {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            try {
                return instance.getAllEvents();
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event item);
    }
}
