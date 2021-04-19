package main.stager.ui.about_me;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import main.stager.utils.DataProvider;
import main.stager.Base.StagerViewModel;

public class AboutMeViewModel extends StagerViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<String> mName;
    private MutableLiveData<String> mDescription;

    public AboutMeViewModel(@NonNull Application application) {
        super(application);
        mName = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return getData(mName, () -> dataProvider.getUserName().addValueEventListener(
            new DataProvider.ValueEventListener<>(mName, String.class)
        ));
    }

    public LiveData<String> getDescription() {
        return getData(mDescription, () -> dataProvider.getUserDescription().addValueEventListener(
            new DataProvider.ValueEventListener<>(mDescription, String.class)
        ));
    }
}