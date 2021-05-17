package main.stager.ui.contact_requests.incoming;

import android.app.Application;
import androidx.annotation.NonNull;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.OnError;

public class IncomingContactRequestsViewModel extends StagerListViewModel<Contact> {

    public IncomingContactRequestsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.getContactRequests();
    }

    @Override
    protected Class<Contact> getItemType() {
        return Contact.class;
    }

    @Override
    protected ValueEventListener getListEventListener(OnError onError) {
        return getJoinedListEventListener(
                dataProvider.getAllUserInfo(), onError);
    }
}