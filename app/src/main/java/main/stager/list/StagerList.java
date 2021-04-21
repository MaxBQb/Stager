package main.stager.list;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;
import java.util.List;

import main.stager.utils.DataProvider;
import main.stager.R;
import main.stager.Base.StagerVMFragment;

public abstract class StagerList<TVM extends ViewModel,
                                 TA extends StagerListAdapter<T,
                                                                          ? extends RecyclerView.ViewHolder>,
                                 T> extends StagerVMFragment<TVM> {
    protected TA adapter;

    // Требует переопределения
    protected abstract Class<TA> getAdapterType();

    // Основное
    protected RecyclerView rv;
    protected LiveData<List<T>> list;
    protected StatesRecyclerViewAdapter srvAdapter;

    // Режимы отображения
    protected View emptyView;
    protected View errorView;
    protected View loadingView;

    // Listeners
    protected void onItemClick(T item, int pos) {}
    public void onItemSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int pos, int direction) {}

    protected TA createAdapter() {
        try {
            return getAdapterType().newInstance();
        } catch (IllegalAccessException e) {
            return null;
        } catch (java.lang.InstantiationException e) {
            return null;
        }
    }

    protected abstract LiveData<List<T>> getList(DataProvider.OnError onError);

    protected RecyclerView getRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    protected View getEmptyView() {
        return getLayoutInflater().inflate(R.layout.empty_list_view, rv, false);
    }

    protected View getErrorView() {
        return getLayoutInflater().inflate(R.layout.error_list_view, rv, false);
    }

    protected View getLoadingView() {
        return getLayoutInflater().inflate(R.layout.loading_list_view, rv, false);
    }

    protected String getEmptyText() {
        return getString(R.string.CommonList_EmptyListView_TextView_text);
    }

    protected String getErrorText(String reason) {
        return getString(R.string.CommonList_ErrorListView_TextView_text, reason);
    }

    protected String getLoadingText() {
        return getString(R.string.CommonList_LoadingListView_TextView_text);
    }

    private void setNNText(View v, @IdRes int id, String text) {
        if (v == null) return;
        TextView tv = v.findViewById(id);
        if (tv == null) return;
        tv.setText(text);
    }

    protected void prepareViews() {
        emptyView = getEmptyView();
        errorView = getErrorView();
        loadingView = getLoadingView();
        setNNText(emptyView, R.id.empty_list_view_message, getEmptyText());
        setNNText(errorView, R.id.error_list_view_message, getErrorText(null));
        setNNText(loadingView, R.id.loading_list_view_message, getLoadingText());
    }

    protected void onError(String reason) {
        if (!isSafe()) return;
        setNNText(errorView, R.id.error_list_view_message, getErrorText(reason));
        srvAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
    }

    protected void reactState(List<?> list) {
        if (list == null || list.isEmpty())
            srvAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        else
            srvAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        list.observe(getViewLifecycleOwner(), this::reactState);
        list.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    protected void setEventListeners() {
        super.setEventListeners();
        adapter.setOnItemClickListener(this::onItemClick);
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

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        adapter = createAdapter();
        rv = getRecyclerView();
        prepareViews();
        srvAdapter = new StatesRecyclerViewAdapter(adapter, loadingView, emptyView, errorView);
        rv.setAdapter(srvAdapter);
        list = getList(this::onError);
        srvAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
    }
}