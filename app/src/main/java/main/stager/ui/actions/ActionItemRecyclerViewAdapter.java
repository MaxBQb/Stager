package main.stager.ui.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import main.stager.R;
import main.stager.StagerListAdapter;
import main.stager.model.UserAction;

/**
 * {@link RecyclerView.Adapter} для отображения {@link UserAction}.
 */
public class ActionItemRecyclerViewAdapter
        extends StagerListAdapter<UserAction, ActionItemRecyclerViewAdapter.ViewHolder> {
    private NavController nav;

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.getName());
        switch (holder.mItem.getStatus()) {
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
        holder.mView.setOnClickListener(v -> onItemClickListener
                .onItemClick(holder.mItem, position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mStatusView;
        public UserAction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.item_name);
            mStatusView = view.findViewById(R.id.item_icon);
        }
    }
}