package main.stager.ui.find_new_contacts;

import android.app.Application;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import java.util.Set;
import main.stager.list.feature.StagerSearchResultsListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.AValueEventListener;

public class FindNewContactsViewModel extends StagerSearchResultsListViewModel<Contact> {

    @Override
    protected String getInitialQuery() {
        return " ";
    }

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

    @Override
    protected void setupListEventListener() {
        super.setupListEventListener();
        initIgnoreList();
        dataProvider.getContacts().addValueEventListener(new AValueEventListener<String>() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initIgnoreList();
                if (!snapshot.exists())
                    return;
                Set<String> ignore = mListEventListener.getIgnoredKeys();
                for (DataSnapshot postSnapshot : snapshot.getChildren())
                    ignore.add(postSnapshot.getKey());
            }
        });
    }

    private void initIgnoreList() {
        mListEventListener.getIgnoredKeys().clear();
        mListEventListener.getIgnoredKeys().add(dataProvider.getUID());
    }
}