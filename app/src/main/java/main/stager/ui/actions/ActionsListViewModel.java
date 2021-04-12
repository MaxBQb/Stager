package main.stager.ui.actions;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import java.util.List;
import main.stager.DataProvider;
import main.stager.model.UserAction;

public class ActionsListViewModel extends AndroidViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<List<UserAction>> actions;

    public ActionsListViewModel(@NonNull Application application) {
        super(application);
        actions = new MutableLiveData<>();
    }

    public LiveData<List<UserAction>> getActions() {
        if (actions.getValue() == null)
            dataProvider.getActions().addValueEventListener(
                    DataProvider.getListChangesListener(actions, UserAction.class,
                            (Object ua, DataSnapshot snapshot) -> {
                                ((UserAction)ua).setKey(snapshot.getKey());
                                return ua;
                            }));
        return actions;
    }
}