package main.stager.ui.find_new_contacts;

import android.app.Application;
import androidx.annotation.NonNull;
import com.google.firebase.database.Query;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;

public class FindNewContactsViewModel extends StagerListViewModel<Contact> {

    public FindNewContactsViewModel(@NonNull Application application) {
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
    public void deleteItem(Contact s) {}
}