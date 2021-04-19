package main.stager;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;
import java.util.List;


public abstract class StagerList<TVM extends ViewModel,
                                 TA extends StagerListAdapter<T,
                                         ? extends RecyclerView.ViewHolder>,
                                 T> extends Fragment {
    protected TVM viewModel;
    protected TA adapter;

    // Требует переопределения
    protected abstract Class<TVM> getViewModelType();
    protected abstract Class<TA> getAdapterType();
    protected abstract @LayoutRes int getViewBaseLayoutId();

    // Основное
    protected NavController navigator;
    protected RecyclerView rv;
    protected LiveData<List<T>> list;
    protected StatesRecyclerViewAdapter srvAdapter;
    protected View view;

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

    protected NavController getNavigator() {
        return ((NavHostFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
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
        setNNText(errorView, R.id.error_list_view_message, getErrorText(reason));
        srvAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
    }

    protected void reactState(List<?> list) {
        if (list == null || list.isEmpty())
            srvAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        else
            srvAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
    }

    protected void setObservers() {
        list.observe(getViewLifecycleOwner(), this::reactState);
    }

    protected void setEventListeners() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getViewBaseLayoutId(), container, false);
        navigator = getNavigator();
        viewModel = new ViewModelProvider(this).get(getViewModelType());
        adapter = createAdapter();
        rv = getRecyclerView();
        prepareViews();
        srvAdapter = new StatesRecyclerViewAdapter(adapter, loadingView, emptyView, errorView);
        rv.setAdapter(srvAdapter);
        list = getList(this::onError);
        setObservers();
        setEventListeners();
        srvAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
        return view;
    }
}