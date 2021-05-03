package main.stager.ui.find_new_contacts;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.Query;
import java.util.List;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.OnError;
import main.stager.utils.ChangeListeners.firebase.ValueListEventListener;

public class FindNewContactsViewModel extends StagerListViewModel<Contact> {

    private String query = "";

    public FindNewContactsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.findUserByEmail(query);
    }

    @Override
    protected Class<Contact> getItemType() {
        return Contact.class;
    }

    public LiveData<List<Contact>> getItems(OnError onError) {
        getListPath().addListenerForSingleValueEvent(
                new ValueListEventListener<>(mValues, getItemType(), onError));
        return mValues;
    }

    public void setQuery(String query) {
        this.query = query;
        getItems(null);
    }

    @Override
    public void deleteItem(Contact s) {}
}