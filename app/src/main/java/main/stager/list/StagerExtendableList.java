package main.stager.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import main.stager.R;

public abstract class StagerExtendableList<TVM extends ViewModel,
                                           TA extends StagerListAdapter<T,
                                                                                      ? extends RecyclerView.ViewHolder>,
                                           T> extends StagerList<TVM, TA, T> {

    // Listeners
    protected abstract void onButtonAddClicked(View v);

    protected void onItemMoved(int from, int to) {
        adapter.moveItem(from, to);
    }

    protected void setEventListeners() {
        super.setEventListeners();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        }).attachToRecyclerView(rv);
        view.findViewById(R.id.button_list_add).setOnClickListener(this::onButtonAddClicked);
    }
}