package main.stager.ui.my_contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.Query;
import java.util.List;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.OnError;
import main.stager.utils.ChangeListeners.firebase.ValueJoinedListEventListener;

public class ContactsListViewModel extends StagerListViewModel<Contact> {

    public ContactsListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.getContacts();
    }

    @Override
    protected Class<Contact> getItemType() {
        return Contact.class;
    }

    @Override
    public LiveData<List<Contact>> getItems(OnError onError) {
        return getData(mValues, () -> getListPath().addValueEventListener(
                new ValueJoinedListEventListener<>(mValues, getItemType(), onError,
                        dataProvider.getAllUserInfo())));
    }

    @Override
    public void deleteItem(Contact ua) {
        dataProvider.deleteAction(ua.getKey());
    }
}