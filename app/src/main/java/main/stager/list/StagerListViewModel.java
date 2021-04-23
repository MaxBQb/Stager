package main.stager.list;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import main.stager.Base.StagerViewModel;

public abstract class StagerListViewModel<T> extends StagerViewModel {

    protected MutableLiveData<List<T>> mValues;

    public StagerListViewModel(@NonNull Application application) {
        super(application);
        mValues = new MutableLiveData<>();
    }
}
