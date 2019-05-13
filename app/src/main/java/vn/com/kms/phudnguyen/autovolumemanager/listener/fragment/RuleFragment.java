package vn.com.kms.phudnguyen.autovolumemanager.listener.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Rule;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RuleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RuleItemRecyclerViewAdapter ruleAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RuleFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RuleFragment newInstance(int columnCount) {
        RuleFragment fragment = new RuleFragment();
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
        ruleAdapter = new RuleItemRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rule_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(ruleAdapter);
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
        new RuleLoaderTask(new RuleLoaderTask.RuleLoaderTaskCallback() {
            @Override
            public void onCompleted(List<Rule> rules) {
                ruleAdapter.setRules(rules);
                ruleAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {

        // TODO: Update argument type and name
        void onListFragmentInteraction(Rule item);
    }

    private static class RuleLoaderTask extends AsyncTask<Void, Void, List<Rule>> {
        private final RuleLoaderTaskCallback callback;

        public interface RuleLoaderTaskCallback {
            void onCompleted(List<Rule> rules);
        }

        public static final String TAG = RuleLoaderTask.class.getName();

        public RuleLoaderTask(RuleLoaderTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Rule> doInBackground(Void... args) {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            try {
                return instance.getAllRules();
            } catch (IllegalAccessException | InvocationTargetException | java.lang.InstantiationException | ParseException e) {
                Log.e(TAG, "Fail to get all rules", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<Rule> rules) {
            super.onPostExecute(rules);
            Log.i(TAG, "Found number or rules " + rules.size());
            this.callback.onCompleted(rules);
        }
    }

}





















