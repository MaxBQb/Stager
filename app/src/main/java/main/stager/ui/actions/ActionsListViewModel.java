package main.stager.ui.actions;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.DataSnapshot;
import java.util.List;
import main.stager.list.StagerListViewModel;
import main.stager.utils.DataProvider;
import main.stager.model.UserAction;

public class ActionsListViewModel extends StagerListViewModel<UserAction> {

    public ActionsListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserAction>> getActions(DataProvider.OnError onError) {
        return getData(mValues, () -> dataProvider.getActionsSorted().addValueEventListener(
            new DataProvider.ValueListEventListener<UserAction>(mValues, UserAction.class, onError) {
                @Override
                public UserAction modify(UserAction ua, DataSnapshot snapshot) {
                    ua = super.modify(ua, snapshot);
                    ua.setKey(snapshot.getKey());
                    return ua;
                }
            }
        ));
    }

    public void deleteAction(UserAction ua) {
        dataProvider.deleteAction(ua.getKey());
    }

    public void sendActionsList(List<UserAction> uas) {
        DataProvider.resetPositions(dataProvider.getActions(), DataProvider.getKeys(uas));
    }
}