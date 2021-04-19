package main.stager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.InvocationTargetException;

public abstract class StagerExtendableListFragment<TVM extends ViewModel, TA extends StagerListAdapter, T>
        extends StagerListFragment<TVM, TA, T> {

    protected abstract void onButtonAddClicked(View v);

    public void onItemSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int pos, int direction) {}

    @Override
    protected TA createAdapter() {
        try {
            return getAdapterType().getConstructor(NavController.class).newInstance(navigator);
        } catch (NoSuchMethodException e) {
            return super.createAdapter();
        } catch (InvocationTargetException e) {
            return super.createAdapter();
        } catch (IllegalAccessException e) {
            return null;
        } catch (java.lang.InstantiationException e) {
            return null;
        }
    }

    protected void setEventListeners() {
        super.setEventListeners();
        view.findViewById(R.id.button_list_add).setOnClickListener(this::onButtonAddClicked);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                onItemSwiped(viewHolder, viewHolder.getAdapterPosition(), direction);
            }
        }).attachToRecyclerView(rv);
    }
}