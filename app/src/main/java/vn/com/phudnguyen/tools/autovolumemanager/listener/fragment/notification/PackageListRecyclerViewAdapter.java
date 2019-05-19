package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class PackageListRecyclerViewAdapter extends RecyclerView.Adapter<PackageListRecyclerViewAdapter.ViewHolder> {

    private List<PackageInfo> mValues = new ArrayList<>();
    private PackageListFragment.OnListFragmentInteractionListener mListener;
    private Context context;

    public PackageListRecyclerViewAdapter() {
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
        PackageInfo pi = mValues.get(position);
        holder.mItem = pi;
        holder.mMainText.setText(pi.getName());
        holder.mSubText.setText(pi.getPackageName());
        if (pi.getIcon() != null) {
            holder.mImageView.setImageDrawable(pi.getIcon());
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<PackageInfo> getmValues() {
        return mValues;
    }

    public void setValues(List<PackageInfo> mValues) {
        this.mValues = mValues;
    }

    public PackageListFragment.OnListFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setListener(PackageListFragment.OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMainText;
        public final TextView mSubText;
        public final ImageView mImageView;
        public PackageInfo mItem;

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
