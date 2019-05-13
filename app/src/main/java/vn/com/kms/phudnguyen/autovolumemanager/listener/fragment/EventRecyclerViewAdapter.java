package vn.com.kms.phudnguyen.autovolumemanager.listener.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.com.kms.phudnguyen.autovolumemanager.R;
import vn.com.kms.phudnguyen.autovolumemanager.listener.fragment.EventFragment.OnListFragmentInteractionListener;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Event;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.EventAction;

import java.util.ArrayList;
import java.util.List;

import static vn.com.kms.phudnguyen.autovolumemanager.listener.model.Constants.DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE;
import static vn.com.kms.phudnguyen.autovolumemanager.listener.model.Constants.DATE_FORMAT_WITH_LOCAL_TIMEZONE;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private List<Event> mValues = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;

    public EventRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMainText.setText(mValues.get(position).getAction());
        int textColor = getColorByAction(mValues.get(position).getAction());
        if (textColor >= 0) {
            holder.mMainText.setTextColor(textColor);
        }
        if (DateUtils.isToday(mValues.get(position).getTimestamp().getTime())) {
            holder.mSubText.setText("Today at " + DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE.format(mValues.get(position).getTimestamp()));
        } else {
            holder.mSubText.setText(DATE_FORMAT_WITH_LOCAL_TIMEZONE.format(mValues.get(position).getTimestamp()));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private int getColorByAction(String action) {
        switch (EventAction.valueOf(action)) {
            case SERVICE_STARTED:
                return R.color.serviceStartedColor;
            case SERVICE_STOPPED:
                return R.color.serviceStoppedColor;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Event> getmValues() {
        return mValues;
    }

    public void setValues(List<Event> mValues) {
        this.mValues = mValues;
    }

    public OnListFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setListener(OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMainText;
        public final TextView mSubText;
        public Event mItem;

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
