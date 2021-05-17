package main.stager.ui.find_contacts;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import java.util.List;
import java.util.Set;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.AValueEventListener;
import main.stager.utils.ChangeListeners.firebase.OnError;
import main.stager.utils.ChangeListeners.firebase.ValueListEventListener;

public class FindContactsViewModel extends StagerListViewModel<Contact> {

    private String query = "";
    private ValueListEventListener<Contact> mUserInfoListEventListener;

    public FindContactsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.findUserByName(query);
    }

    @Override
    protected Class<Contact> getItemType() {
        return Contact.class;
    }

    @Override
    public LiveData<List<Contact>> getItems(OnError onError, boolean unused) {
        if (mUserInfoListEventListener == null) {
            mUserInfoListEventListener = new ValueListEventListener<>(
                    mValues, getItemType(), onError);
            mUserInfoListEventListener.setInvertIgnoredKeys(true);
            dataProvider.getContacts().addValueEventListener(new AValueEventListener<String>() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Set<String> ignore = mUserInfoListEventListener
                                         .getIgnoredKeys();
                    ignore.clear();
                    if (!snapshot.exists())
                        return;
                    for (DataSnapshot postSnapshot : snapshot.getChildren())
                        ignore.add(postSnapshot.getKey());
                }
            });
        }
        getListPath().addListenerForSingleValueEvent(mUserInfoListEventListener);
        return mValues;
    }

    public void setQuery(String query) {
        this.query = query;
        getItems(null, false);
    }
}