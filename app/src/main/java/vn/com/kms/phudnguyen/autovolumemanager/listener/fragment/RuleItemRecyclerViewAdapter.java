package vn.com.kms.phudnguyen.autovolumemanager.listener.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.com.kms.phudnguyen.autovolumemanager.R;
import vn.com.kms.phudnguyen.autovolumemanager.listener.fragment.RuleFragment.OnListFragmentInteractionListener;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Rule;

import java.util.ArrayList;
import java.util.List;

public class RuleItemRecyclerViewAdapter extends RecyclerView.Adapter<RuleItemRecyclerViewAdapter.ViewHolder> {

    private List<Rule> rules = new ArrayList<>();
    private OnListFragmentInteractionListener listener = null;

    RuleItemRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = rules.get(position);
        holder.mIdView.setText(rules.get(position).getText());
        holder.mContentView.setText(rules.get(position).getSubText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public OnListFragmentInteractionListener getListener() {
        return listener;
    }

    public void setListener(OnListFragmentInteractionListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Rule mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
