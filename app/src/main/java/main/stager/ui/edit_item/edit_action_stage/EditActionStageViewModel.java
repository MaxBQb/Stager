package main.stager.ui.edit_item.edit_action_stage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import lombok.Setter;
import main.stager.Base.StagerViewModel;

public class EditActionStageViewModel extends StagerViewModel {
    private MutableLiveData<String> mName;
    @Setter private String actionKey;
    @Setter private String stageKey;

    public EditActionStageViewModel(@NonNull Application application) {
        super(application);
        mName = new MutableLiveData<>();
    }

    public LiveData<String> getStageName() {
        return getText(mName, dataProvider.getStageName(actionKey, stageKey));
    }
}