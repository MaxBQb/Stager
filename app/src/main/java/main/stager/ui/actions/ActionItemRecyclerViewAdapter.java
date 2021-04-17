package main.stager.ui.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import main.stager.R;
import main.stager.model.UserAction;
import main.stager.ui.action_stages.StagesListFragment;

/**
 * {@link RecyclerView.Adapter} для отображения {@link UserAction}.
 */
public class ActionItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ActionItemRecyclerViewAdapter.ViewHolder> {
    private List<UserAction> mValues = new ArrayList<>();
    private NavController nav;

    public ActionItemRecyclerViewAdapter(NavController nav) {
        this.nav = nav;
    }

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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAction ua = holder.mItem;
                Bundle args = new Bundle();
                args.putString(StagesListFragment.ARG_ACTION_NAME, ua.getName());
                args.putString(StagesListFragment.ARG_ACTION_KEY, ua.getKey());
                nav.navigate(R.id.transition_actions_list_to_action_stages_list, args);
            }
        });
    }

    public void setValues(List<UserAction> values) {
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
        public UserAction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.item_name);
            mStatusView = view.findViewById(R.id.item_icon);
        }
    }
}