package main.stager.ui.my_actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyActionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyActionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my actions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}