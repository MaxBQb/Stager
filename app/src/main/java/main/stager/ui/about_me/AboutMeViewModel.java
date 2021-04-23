package main.stager.ui.about_me;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import main.stager.Base.StagerViewModel;

public class AboutMeViewModel extends StagerViewModel {
    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDescription;

    public AboutMeViewModel(@NonNull Application application) {
        super(application);
        mName = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
    }

    public LiveData<String> getName() {
        return getText(mName, dataProvider.getUserName());
    }

    public LiveData<String> getDescription() {
        return getText(mDescription, dataProvider.getUserDescription());
    }
}