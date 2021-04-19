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

    public LiveData<List<Stage>> getStages(String key, DataProvider.OnError onError) {
        return getData(stages, () -> dataProvider.getStages(key).addValueEventListener(
            new DataProvider.ValueListEventListener<>(stages, Stage.class, onError)
        ));
    }

    public LiveData<String> getActionName(String key, String defaultValue) {
        return getData(actionName, () -> dataProvider.getAction(key).addValueEventListener(
            new DataProvider.AValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists())
                        return;
                    UserAction ua = snapshot.getValue(UserAction.class);
                    actionName.postValue(ua != null ? ua.getName() : defaultValue);
                }
            }
        ));
    }
}
