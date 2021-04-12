package main.stager.ui.about_me;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import main.stager.DataProvider;

public class AboutMeViewModel extends AndroidViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDescription;

    public AboutMeViewModel(@NonNull Application application) {
        super(application);
        mName = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        if (mName.getValue() == null)
            dataProvider.getUserName().addValueEventListener(
                    DataProvider.getValueChangesListener(mName, String.class)
            );
        return mName;
    }

    public LiveData<String> getDescription() {
        if (mDescription.getValue() == null)
            dataProvider.getUserDescription().addValueEventListener(
                    DataProvider.getValueChangesListener(mDescription, String.class)
            );
        return mDescription;
    }
}