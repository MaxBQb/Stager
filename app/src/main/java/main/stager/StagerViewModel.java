package main.stager;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import main.stager.model.Stage;

public abstract class StagerViewModel extends AndroidViewModel {
    protected static DataProvider dataProvider = DataProvider.getInstance();

    public StagerViewModel(@NonNull Application application) {
        super(application);
    }

    @FunctionalInterface
    protected interface EventListenerSetter {
        public void add();
    }

    protected <T> LiveData<T> getData(MutableLiveData<T> data, EventListenerSetter els) {
        if (data.getValue() == null)
            els.add();
        return data;
    }
}