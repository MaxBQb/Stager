package main.stager;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class StagerVMFragment<TVM extends ViewModel> extends StagerFragment {
    protected TVM viewModel;

    // Требует переопределения
    protected abstract Class<TVM> getViewModelType();

    protected void setObservers() {}

    @Override
    protected void setEventListeners() {
        setObservers();
        super.setEventListeners();
    }

    @Override
    protected void prepareFragmentComponents() {
        viewModel = new ViewModelProvider(this).get(getViewModelType());
    }
}