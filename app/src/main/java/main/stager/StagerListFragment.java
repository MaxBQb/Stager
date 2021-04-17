package main.stager;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;
import androidx.recyclerview.widget.RecyclerView.Adapter;


public abstract class StagerListFragment<TVM extends ViewModel, TA extends Adapter> extends Fragment {
    protected TVM viewModel;
    protected TA adapter;

    // Требует переопределения
    protected abstract Class<TVM> getViewModelType();
    protected abstract Class<TA> getAdapterType();
    protected abstract @LayoutRes int getViewBaseLayoutId();

    // Основное
    protected NavController navigator;
    protected RecyclerView rv;
    protected View view;

    // Режимы отображения
    protected View emptyView;
    protected View errorView;
    protected View loadingView;

    protected TA createAdapter() {
        try {
            return getAdapterType().newInstance();
        } catch (IllegalAccessException e) {
            return null;
        } catch (java.lang.InstantiationException e) {
            return null;
        }
    }

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
        return getLayoutInflater().inflate(R.layout.fragment_no_items_filler, rv, false);
    }

    protected View getErrorView() {
        return null;
    }

    protected View getLoadingView() {
        return null;
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
        setNNText(emptyView, R.id.no_items_fragment_message, "Oops");
        setNNText(errorView, R.id.no_items_fragment_message, "Oops");
        setNNText(loadingView, R.id.no_items_fragment_message, "Oops");
    }

    protected void setObservers() {}
    protected void setEventListeners() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getViewBaseLayoutId(), container, false);
        navigator = getNavigator();
        viewModel = new ViewModelProvider(this).get(getViewModelType());
        adapter = createAdapter();
        rv = getRecyclerView();
        prepareViews();
        StatesRecyclerViewAdapter srvAdapter = new StatesRecyclerViewAdapter(
                adapter, loadingView, emptyView, errorView
        );
        rv.setAdapter(srvAdapter);
        setObservers();
        setEventListeners();
        return view;
    }
}