package main.stager.ui.edit_item.edit_subscribers;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.Query;

import java.util.List;

import lombok.Setter;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.OnError;

public class EditSubscribersViewModel extends StagerListViewModel<Contact> {
    private MutableLiveData<String> actionName;
    @Setter private String actionKey = "";

    public EditSubscribersViewModel(@NonNull Application application) {
        super(application);
        actionName = new MutableLiveData<>();
    }

    public void deleteItem(Contact s) {
        dataProvider.revokeSharedActionAccess(s.getKey(), actionKey);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.getSubscribersOfAction(actionKey);
    }

    @Override
    protected Class<Contact> getItemType() {
        return Contact.class;
    }

    @Override
    public LiveData<List<Contact>> getItems(OnError onError) {
        return getJoinedListData(dataProvider.getAllUserInfo(), onError);
    }

    public LiveData<String> getActionName() {
        return getText(actionName);
    }

    @Override
    public void buildBackPath() {
        super.buildBackPath();
        backPath.put(actionName,
                dataProvider.getActionName(actionKey));
    }
}
