package main.stager.utils.ChangeListeners.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ValueListEventListener<T> extends AValueEventListener<T> {
    protected MutableLiveData<List<T>> liveList;
    protected Class<T> className;

    public ValueListEventListener(MutableLiveData<List<T>> liveList, Class<T> className, OnError onError) {
        this.liveList = liveList;
        this.className = className;
        this.onError = onError;
    }

    public ValueListEventListener(MutableLiveData<List<T>> liveList, Class<T> className) {
        this(liveList, className, null);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (!snapshot.exists()) {
            liveList.postValue(null);
            return;
        }
        List<T> lst = new ArrayList<>();
        for (DataSnapshot postSnapshot: snapshot.getChildren())
            lst.add(modify(postSnapshot.getValue(className), postSnapshot));
        liveList.postValue(lst);
    }
}
