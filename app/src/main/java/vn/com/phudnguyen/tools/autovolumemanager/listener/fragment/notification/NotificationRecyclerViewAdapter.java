package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Constants;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.NotificationLog;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    private List<NotificationLog> items = new ArrayList<>();

    public NotificationRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NotificationLog nl = items.get(position);
        holder.mItem = nl;
        holder.mMainText.setText(nl.getTitle());
        holder.mSubText.setText(nl.getContent());
        holder.mTimestamp.setText(Constants.DATE_FORMAT_WITH_LOCAL_TIMEZONE.format(nl.getTimestamp()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<NotificationLog> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMainText;
        public final TextView mSubText;
        public final TextView mTimestamp;
        public NotificationLog mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMainText = view.findViewById(R.id.main_text);
            mSubText = view.findViewById(R.id.sub_text);
            mTimestamp = view.findViewById(R.id.timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubText.getText() + "'";
        }
    }
}
