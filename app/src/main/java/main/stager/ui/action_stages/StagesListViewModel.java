package main.stager.ui.action_stages;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import java.util.List;
import main.stager.DataProvider;
import main.stager.StagerViewModel;
import main.stager.model.Stage;
import main.stager.model.UserAction;

public class StagesListViewModel extends StagerViewModel {
    private MutableLiveData<List<Stage>> stages;
    private MutableLiveData<String> actionName;

    public StagesListViewModel(@NonNull Application application) {
        super(application);
        stages = new MutableLiveData<>();
        actionName = new MutableLiveData<>();
    }

    public LiveData<List<Stage>> getStages(String key) {
        return getStages(key, null);
    }

    public LiveData<List<Stage>> getStages(String key, DataProvider.OnError onError) {
        if (stages.getValue() == null)
            dataProvider.getStages(key).addValueEventListener(
                    new DataProvider.ValueListEventListener<Stage>(stages, Stage.class, onError)
            );
        return stages;
    }

    public LiveData<String> getActionName(String key, String defaultValue) {
        if (actionName.getValue() == null)
            dataProvider.getAction(key).addValueEventListener(new DataProvider.AValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists())
                            return;
                        UserAction ua = snapshot.getValue(UserAction.class);
                        actionName.postValue(ua != null ? ua.getName() : defaultValue);
                    }
            });
        return actionName;
    }
}
