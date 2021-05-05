package main.stager.ui.contact_requests.outgoing;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.Query;
import java.util.List;
import main.stager.list.StagerListViewModel;
import main.stager.model.Contact;
import main.stager.utils.ChangeListeners.firebase.OnError;
import main.stager.utils.ChangeListeners.firebase.ValueJoinedListEventListener;

public class OutgoingContactRequestsViewModel extends StagerListViewModel<Contact> {

    public OutgoingContactRequestsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Query getListPath() {
        return dataProvider.getOutgoingContactRequests();
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
    public void deleteItem(Contact s) {}
}