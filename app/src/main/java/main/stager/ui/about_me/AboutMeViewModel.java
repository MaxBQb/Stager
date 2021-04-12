package main.stager.ui.about_me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import main.stager.DataProvider;

public class AboutMeViewModel extends ViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<String> mText;

    public AboutMeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is about me fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}