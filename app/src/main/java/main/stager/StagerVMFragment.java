package main.stager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public abstract class StagerVMFragment<TVM extends ViewModel> extends Fragment {
    protected TVM viewModel;

    // Требует переопределения
    protected abstract Class<TVM> getViewModelType();
    protected abstract @LayoutRes int getViewBaseLayoutId();

    // Основное
    protected NavController navigator;
    protected View view;

    protected NavController getNavigator() {
        return ((NavHostFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
    }

    protected void setObservers() {}
    protected void setEventListeners() {}
    protected void prepareFragmentComponents() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getViewBaseLayoutId(), container, false);
        navigator = getNavigator();
        viewModel = new ViewModelProvider(this).get(getViewModelType());
        prepareFragmentComponents();
        setObservers();
        setEventListeners();
        return view;
    }
}