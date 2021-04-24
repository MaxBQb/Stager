package main.stager.ui.edit_item.edit_action;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import main.stager.R;
import main.stager.list.StagerListAdapter;
import main.stager.model.Stage;
import main.stager.utils.Utilits;

/**
 * {@link RecyclerView.Adapter} для отображения {@link Stage}.
 */
public class ActionStageRecyclerViewAdapter
        extends StagerListAdapter<Stage, ActionStageRecyclerViewAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<Stage> DIFF_CALLBACK = new FBItemCallback<Stage>() {
        @Override
        public boolean areContentsTheSame(@NonNull Stage oldItem, @NonNull Stage newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getCurrentStatus().equals(newItem.getCurrentStatus());
        }
    };

    public ActionStageRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
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
        holder.mItem = getItem(position);

        if (Utilits.isNullOrBlank(holder.mItem.getName()))
            holder.mContentView.setText(R.string.EditActionStage_message_UntitledStage);
        else
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
        bindOnItemClickListener(holder.mView, holder.mItem, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mStatusView;
        public Stage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.item_name);
            mStatusView = view.findViewById(R.id.item_icon);
        }
    }
}