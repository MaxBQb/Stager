package main.stager;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class StagerViewModel extends AndroidViewModel {
    protected static DataProvider dataProvider = DataProvider.getInstance();

    public StagerViewModel(@NonNull Application application) {
        super(application);
    }
}