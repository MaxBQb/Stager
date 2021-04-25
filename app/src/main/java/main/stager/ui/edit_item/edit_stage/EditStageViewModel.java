package main.stager.ui.edit_item.edit_stage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import lombok.Setter;
import main.stager.Base.StagerViewModel;
import main.stager.model.Status;

public class EditStageViewModel extends StagerViewModel {
    private MutableLiveData<String> mName;
    private MutableLiveData<Status> mStatus;
    @Setter private String actionKey;
    @Setter private String stageKey;

    public EditStageViewModel(@NonNull Application application) {
        super(application);
        mName = new MutableLiveData<>();
        mStatus = new MutableLiveData<>();
    }

    public LiveData<String> getStageName() {
        return getText(mName, dataProvider.getStageName(actionKey, stageKey));
    }

    public LiveData<Status> getStageStatus() {
        return getSimpleFBData(mStatus,
                dataProvider.getStageStatus(actionKey, stageKey), Status.class);
    }
}