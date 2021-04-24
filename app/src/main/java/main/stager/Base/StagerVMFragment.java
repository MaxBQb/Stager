package main.stager.Base;

import android.widget.EditText;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.database.DatabaseReference;
import lombok.SneakyThrows;
import main.stager.utils.ChangeListeners.firebase.front.FBFrontListener;
import main.stager.utils.ChangeListeners.firebase.front.OnLostFocusDBUpdater;

public abstract class StagerVMFragment<TVM extends StagerViewModel> extends StagerFragment {
    protected TVM viewModel;

    // Требует переопределения
    protected abstract Class<TVM> getViewModelType();

    protected void setObservers() {}

    @Override
    protected void setEventListeners() {
        setObservers();
        super.setEventListeners();
    }

    protected void setViewModelData() {}

    @Override
    protected void prepareFragmentComponents() {
        viewModel = new ViewModelProvider(this).get(getViewModelType());
        setViewModelData();
    }

    @FunctionalInterface
    public interface BindDataCallback<T> {
        void run(T data);
    }

    @FunctionalInterface
    public interface BindDataTwoWayListenerSetter<T> {
        void setListener(T listener);
    }

    protected <T> void bindData(LiveData<T> data, BindDataCallback<T> callback) {
        data.observe(getViewLifecycleOwner(), callback::run);
    }

    protected <T, L> void bindDataTwoWay(LiveData<T> data, BindDataCallback<T> callback,
                                         BindDataTwoWayListenerSetter<L> listenerSetter,
                                         L listener) {
        bindData(data, callback);
        listenerSetter.setListener(listener);
    }

    @SneakyThrows
    protected <T, L extends FBFrontListener> void bindFBDataTwoWay(LiveData<T> data, BindDataCallback<T> callback,
                                                                   BindDataTwoWayListenerSetter<L> listenerSetter,
                                                                   Class<L> listenerType) {
        bindData(data, callback);
        DatabaseReference ref = viewModel.getBackPathTo(data);
        assert ref != null;
        listenerSetter.setListener(listenerType.getConstructor(DatabaseReference.class)
            .newInstance(ref));
    }

    protected void bindDataTwoWay(LiveData<String> data, EditText edit) {
        bindFBDataTwoWay(data, edit::setText, edit::setOnFocusChangeListener, OnLostFocusDBUpdater.class);
    }
}