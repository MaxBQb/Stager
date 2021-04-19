package main.stager.Base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import main.stager.R;

public abstract class StagerFragment extends Fragment {
    // Требует переопределения
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

    protected void setEventListeners() {}
    protected void prepareFragmentComponents() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getViewBaseLayoutId(), container, false);
        navigator = getNavigator();
        prepareFragmentComponents();
        setEventListeners();
        return view;
    }
}