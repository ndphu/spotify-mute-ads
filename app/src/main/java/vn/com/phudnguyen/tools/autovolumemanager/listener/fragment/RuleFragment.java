package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Rule;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public class RuleFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RuleItemRecyclerViewAdapter ruleAdapter;
    private View rootLayout;

    public RuleFragment() {

    }
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
        ruleAdapter.setListener(new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Rule item) {
                FragmentManager fm = RuleFragment.this.getFragmentManager();
                RuleDialogFragment dialog = RuleDialogFragment.newInstance(item);
                dialog.setUpdateListener(new RuleDialogFragment.OnRuleUpdateCompletedListener() {
                    @Override
                    public void onCompleted() {
                        reloadRules();
                        Snackbar.make(rootLayout, "Rule saved successfully", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception ex) {
                        Snackbar.make(rootLayout, "Error occur: " + ex.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
                dialog.show(fm, "edit_rule_fragment");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_rule_list, container, false);

        // Set the adapter
        Context context = rootLayout.getContext();
        RecyclerView recyclerView = rootLayout.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(ruleAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        FloatingActionButton fab = rootLayout.findViewById(R.id.btn_add_rule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = RuleFragment.this.getFragmentManager();
                RuleDialogFragment dialog = RuleDialogFragment.newInstance();
                dialog.setUpdateListener(new RuleDialogFragment.OnRuleUpdateCompletedListener() {
                    @Override
                    public void onCompleted() {
                        reloadRules();
                        Snackbar.make(rootLayout, "Rule saved successfully", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception ex) {
                        Snackbar.make(rootLayout, "Error occur: " + ex.getMessage(), Snackbar.LENGTH_LONG).show();;
                    }
                });
                dialog.show(fm, "new_rule_fragment");
            }
        });


        return rootLayout;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        reloadRules();
    }

    private void reloadRules() {
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
    }

    public interface OnListFragmentInteractionListener {
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





















