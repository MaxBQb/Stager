package main.stager.utils.ChangeListeners.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.stager.utils.DataProvider;

public class ValueJoinedListEventListener<T> extends ValueListEventListener<T> {
    protected DatabaseReference source;
    protected DatabaseReference keysSource;
    protected List<T> tmpList;
    protected Set<String> awaitedKeys = new HashSet<>();
    protected Set<String> listenKeys;
    protected ValueEventListener sourceListener = new SourceValueListener();

    public ValueJoinedListEventListener(MutableLiveData<List<T>> liveList,
                                        Class<T> className, OnError onError,
                                        Query source) {
        super(liveList, className, onError);
        this.source = source.getRef();
    }

    public ValueJoinedListEventListener(MutableLiveData<List<T>> liveList,
                                        Class<T> className, Query source) {
        super(liveList, className);
        this.source = source.getRef();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        keysSource = snapshot.getRef();
        if (!snapshot.exists()) {
            liveList.postValue(null);
            return;
        }
        tmpList = new ArrayList<>();
        awaitedKeys = new HashSet<>();
        for (DataSnapshot postSnapshot: snapshot.getChildren())
            awaitedKeys.add(postSnapshot.getKey());

        if (listenKeys != null) {
            for (String key: listenKeys)
                source.child(key).removeEventListener(sourceListener);
        }

        listenKeys = new HashSet<>(awaitedKeys);

        for (String key: awaitedKeys)
            source.child(key).addValueEventListener(sourceListener);
    }

    protected void onSourceDataChanged(@NotNull DataSnapshot snapshot) {
        if (!awaitedKeys.contains(snapshot.getKey()))
            onExistingSourceDataChanged(snapshot);
        else
            onSourceDataGotInitial(snapshot);
    }

    protected void onExistingSourceDataChanged(@NotNull DataSnapshot snapshot) {
        // Just cause update of list of keys (joined to source)
        if (snapshot.getKey() != null)
            DataProvider.toggle(keysSource.child(snapshot.getKey()));
    }

    protected void onSourceDataGotInitial(@NotNull DataSnapshot snapshot) {
        awaitedKeys.remove(snapshot.getKey());

        T item;
        if (snapshot.exists()) {
            item = snapshot.getValue(className);
            if (!exclude(item)) {
                item = modify(item, snapshot);
                if (!excludeModified(item))
                    tmpList.add(item);
            }
        }

        if (awaitedKeys.isEmpty())
            liveList.postValue(tmpList);
    }

    protected class SourceValueListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            onSourceDataChanged(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            ValueJoinedListEventListener.this.onCancelled(error);
        }
    };
}
