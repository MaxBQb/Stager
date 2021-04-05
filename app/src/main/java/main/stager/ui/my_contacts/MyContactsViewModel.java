package main.stager.ui.my_contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyContactsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyContactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my contacts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}