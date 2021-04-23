package main.stager.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import main.stager.R;
import main.stager.model.FBModel;

import static com.google.android.material.color.MaterialColors.ALPHA_FULL;

public abstract class StagerExtendableList<TVM extends StagerListViewModel<T>,
                                           TA extends StagerListAdapter<T,
                                                                                      ? extends RecyclerView.ViewHolder>,
                                           T extends FBModel> extends StagerList<TVM, TA, T> {

    // Listeners
    protected abstract void onButtonAddClicked(View v);

    protected void onItemDragged(int from, int to) {
        adapter.moveItem(from, to);
    }

    protected void onItemDropped(int from, int to) {
        viewModel.pushItemsPositions(adapter.getCurrentList());
    }

    @Override
    public void onItemSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int pos, int direction) {
        super.onItemSwiped(viewHolder, pos, direction);
        viewModel.deleteItem(adapter.get(pos));
    }

    protected void setEventListeners() {
        super.setEventListeners();
        new ItemTouchHelper(new ItemDragAndDropCallback() {
            @Override
            protected void onDrag(int from, int to) {
                onItemDragged(from, to);
            }

            @Override
            protected void onDrop(int from, int to) {
                onItemDropped(from, to);
            }
        }).attachToRecyclerView(rv);
        view.findViewById(R.id.button_list_add).setOnClickListener(this::onButtonAddClicked);
    }

    protected abstract static class ItemDragAndDropCallback extends ItemTouchHelper.Callback {
        private Integer mFrom = null;
        private Integer mTo = null;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public boolean onMove(@NotNull RecyclerView recyclerView,
                              RecyclerView.ViewHolder source,
                              RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType())
                return false;

            if (mFrom == null)
                mFrom = source.getAdapterPosition();
            mTo = target.getAdapterPosition();

            recyclerView.getAdapter().notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
            onDrag(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        protected abstract void onDrag(int from, int to);
        protected abstract void onDrop(int from, int to);

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            viewHolder.itemView.setAlpha(ALPHA_FULL);

            if (mFrom != null && mTo != null)
                onDrop(mFrom, mTo);

            // clear saved positions
            mFrom = mTo = null;
        }
    }
}