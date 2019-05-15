package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.RuleFragment.OnListFragmentInteractionListener;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Rule;

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
        holder.mMainText.setText(rules.get(position).getText());
        holder.mSubText.setText(rules.get(position).getSubText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
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
        public final TextView mMainText;
        public final TextView mSubText;
        public Rule mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMainText = (TextView) view.findViewById(R.id.main_text);
            mSubText = (TextView) view.findViewById(R.id.sub_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubText.getText() + "'";
        }
    }
}
