package main.stager.ui.actions;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import java.util.List;
import main.stager.DataProvider;
import main.stager.StagerViewModel;
import main.stager.model.UserAction;

public class ActionsListViewModel extends StagerViewModel {
    private MutableLiveData<List<UserAction>> actions;

    public ActionsListViewModel(@NonNull Application application) {
        super(application);
        actions = new MutableLiveData<>();
    }

    public LiveData<List<UserAction>> getActions(DataProvider.OnError onError) {
        return getData(actions, () -> dataProvider.getActions().addValueEventListener(
            new DataProvider.ValueListEventListener<UserAction>(actions, UserAction.class, onError) {
                @Override
                public UserAction modify(UserAction ua, DataSnapshot snapshot) {
                    ua = super.modify(ua, snapshot);
                    ua.setKey(snapshot.getKey());
                    return ua;
                }
            }
        ));
    }
}