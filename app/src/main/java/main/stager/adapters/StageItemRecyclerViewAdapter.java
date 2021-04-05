package main.stager.adapters;

import main.stager.R;
import main.stager.model.Stage;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} для отображения {@link Stage}.
 */
public class StageItemRecyclerViewAdapter
        extends RecyclerView.Adapter<StageItemRecyclerViewAdapter.ViewHolder>
        implements Serializable {

    private List<Stage> mValues = new ArrayList<Stage>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stage_item,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.getName());
        switch (holder.mItem.getCurrentStatus()) {
            case ABORTED:
                holder.mStatusView.setImageResource(
                        R.drawable.ic_stage_status_abort);
                break;

            case SUCCEED:
                holder.mStatusView.setImageResource(
                        R.drawable.ic_stage_status_succes);
                break;

            case WAITING:
                holder.mStatusView.setImageResource(
                        R.drawable.ic_stage_status_wait);
                break;
        }
    }

    public void setValues(List<Stage> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mStatusView;
        public Stage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mStatusView = (ImageView) view.findViewById(R.id.stage_status);
        }
    }
}