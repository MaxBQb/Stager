package main.stager.ui.edit_item.edit_action;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import lombok.Setter;
import main.stager.utils.DataProvider;
import main.stager.list.StagerListViewModel;
import main.stager.model.Stage;

public class EditActionViewModel extends StagerListViewModel<Stage> {
    private MutableLiveData<String> actionName;
    @Setter private String actionKey = "";

    public EditActionViewModel(@NonNull Application application) {
        super(application);
        actionName = new MutableLiveData<>();
    }

    public void deleteItem(Stage s) {
        dataProvider.deleteStage(actionKey, s.getKey());
    }

    @Override
    protected Query getListPath() {
        return dataProvider.getStages(actionKey);
    }

    @Override
    protected Class<Stage> getItemType() {
        return Stage.class;
    }

    public LiveData<String> getActionName(String defaultValue) {
        return getData(actionName, () -> dataProvider.getActionName(actionKey).addValueEventListener(
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
