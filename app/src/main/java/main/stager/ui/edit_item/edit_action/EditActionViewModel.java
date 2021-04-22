package main.stager.ui.edit_item.edit_action;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import main.stager.utils.DataProvider;
import main.stager.Base.StagerViewModel;
import main.stager.model.Stage;
import main.stager.model.UserAction;

public class EditActionViewModel extends StagerViewModel {
    private MutableLiveData<List<Stage>> stages;
    private MutableLiveData<String> actionName;

    public EditActionViewModel(@NonNull Application application) {
        super(application);
        stages = new MutableLiveData<>();
        actionName = new MutableLiveData<>();
    }

    public LiveData<List<Stage>> getStages(String key, DataProvider.OnError onError) {
        return getData(stages, () -> dataProvider.getStages(key).addValueEventListener(
            new DataProvider.ValueListEventListener<Stage>(stages, Stage.class, onError) {
                @Override
                protected DatabaseReference backPathModify() {
                    return dataProvider.getStages(key);
                }
            }
        ));
    }

    public void deleteStage(Stage s, String actionKey) {
        dataProvider.deleteStage(actionKey, s.getKey());
    }

    public void sendStagesList(List<Stage> stages, String actionKey) {
        DataProvider.resetPositions(dataProvider.getStages(actionKey), DataProvider.getKeys(stages));
    }

    public LiveData<String> getActionName(String key, String defaultValue) {
        return getData(actionName, () -> dataProvider.getActionName(key).addValueEventListener(
            new DataProvider.ValueEventListener<String>(actionName, String.class) {
                @Override
                protected String modify(String item, DataSnapshot snapshot) {
                    String t = super.modify(item, snapshot);
                    if (t == null || t.trim().isEmpty())
                        return defaultValue;
                    return t.trim();
                }
            }
        ));
    }
}
