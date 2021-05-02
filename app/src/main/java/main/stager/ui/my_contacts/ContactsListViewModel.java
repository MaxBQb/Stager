package main.stager.ui.my_contacts;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.database.Query;

import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;

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
    public void deleteItem(Contact ua) {
        dataProvider.deleteAction(ua.getKey());
    }
}