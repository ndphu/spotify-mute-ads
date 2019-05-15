package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.EventFragment.OnListFragmentInteractionListener;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Event;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.EventAction;

import java.util.ArrayList;
import java.util.List;

import static vn.com.phudnguyen.tools.autovolumemanager.listener.model.Constants.DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE;
import static vn.com.phudnguyen.tools.autovolumemanager.listener.model.Constants.DATE_FORMAT_WITH_LOCAL_TIMEZONE;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private List<Event> mValues = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private Context context;

    public EventRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Event event = mValues.get(position);
        holder.mItem = event;
        EventAction eventAction = EventAction.valueOf(event.getAction());

        holder.mMainText.setText(eventAction.getDisplayText());

        switch (eventAction) {
            case MUTED:
                holder.mImageView.setImageResource(R.drawable.ic_action_muted);
                holder.mImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorActionMuted));
                break;
            case RESTORED:
                holder.mImageView.setImageResource(R.drawable.ic_action_restored);
                holder.mImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorActionRestored));
                break;
            case SERVICE_STARTED:
                holder.mImageView.setImageResource(R.drawable.ic_service_started);
                holder.mImageView.setColorFilter(ContextCompat.getColor(context, R.color.serviceStartedColor));
                break;
            case SERVICE_STOPPED:
                holder.mImageView.setImageResource(R.drawable.ic_service_stopped);
                holder.mImageView.setColorFilter(ContextCompat.getColor(context, R.color.serviceStoppedColor));
                break;
        }

        if (DateUtils.isToday(event.getTimestamp().getTime())) {
            holder.mSubText.setText("Today at " + DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE.format(event.getTimestamp()));
        } else {
            holder.mSubText.setText(DATE_FORMAT_WITH_LOCAL_TIMEZONE.format(event.getTimestamp()));
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
                return Color.GREEN;
//                return ContextCompat.getColor(context, R.color.serviceStartedColor);
            case SERVICE_STOPPED:
//                return ContextCompat.getColor(context, R.color.serviceStoppedColor);
                return Color.RED;
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
        public final ImageView mImageView;
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMainText = view.findViewById(R.id.main_text);
            mSubText = view.findViewById(R.id.sub_text);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubText.getText() + "'";
        }
    }
}
