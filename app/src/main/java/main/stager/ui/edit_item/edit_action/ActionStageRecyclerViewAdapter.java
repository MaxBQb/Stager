package main.stager.ui.edit_item.edit_action;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import main.stager.R;
import main.stager.linkers.LStatus;
import main.stager.list.StagerListAdapter;
import main.stager.list.StagerViewHolder;
import main.stager.model.Stage;
import main.stager.model.Status;
import main.stager.utils.Utilits;

/**
 * {@link RecyclerView.Adapter} для отображения {@link Stage}.
 */
public class ActionStageRecyclerViewAdapter
        extends StagerListAdapter<Stage, ActionStageRecyclerViewAdapter.ViewHolder> {

    private Animation slowRotateAnimation;
    private Animation quickRotateAnimation;

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

    @Override
    protected Class<ViewHolder> getViewHolderType() {
        return ViewHolder.class;
    }

    @Override
    protected void onViewHolderCreated(View view, ViewHolder holder) {
        super.onViewHolderCreated(view, holder);
        if (slowRotateAnimation == null)
            slowRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotation);

        if (quickRotateAnimation == null) {
            quickRotateAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotation);
            long duration = quickRotateAnimation.getDuration();
            quickRotateAnimation.setDuration(duration/2);
        }
    }

    @Override
    protected void onBindViewHolderInner(final ViewHolder holder, int position) {
        if (Utilits.isNullOrBlank(holder.mItem.getName()))
            holder.mContentView.setText(R.string.EditStageFragment_message_UntitledStage);
        else
            holder.mContentView.setText(holder.mItem.getName());

        holder.mStatusView.setImageResource(LStatus.toIcon(
                holder.mItem.getCurrentStatus()));

        if (holder.mItem.getCurrentStatus() == Status.WAITING) {
            holder.mStatusView.startAnimation(slowRotateAnimation);
            holder.mStatusView.setAlpha(0.7f);
        } else
            holder.mStatusView.setAlpha(1f);

        if (holder.mItem.getCurrentStatus() == Status.EVALUATING)
            holder.mStatusView.startAnimation(quickRotateAnimation);
    }

    public static class ViewHolder extends StagerViewHolder<Stage> {
        public final TextView mContentView;
        public final ImageView mStatusView;

        public ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.item_name);
            mStatusView = view.findViewById(R.id.item_icon);
        }
    }
}