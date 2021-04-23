package main.stager.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import main.stager.model.FBModel;
import main.stager.model.Stage;
import main.stager.model.UserAction;

public class DataProvider {
    private static DataProvider instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    public static synchronized DataProvider getInstance() {
        if (instance == null)
            instance = new DataProvider();
        return instance;
    }

    private DataProvider() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.setPersistenceEnabled(true);
        mRef = db.getReference("stager-main-db");
        keepSynced();
    }

    private void keepSynced() {
        DatabaseReference[] sync = new DatabaseReference[]{
                getActions(),
                getAllStages()
        };

        for (DatabaseReference dr: sync)
            dr.keepSynced(true);
    }

    // User data

    /** Path safe, not null uid
     * @return uid or empty string
     */
    public @NotNull String getUID() {
        String uid = mAuth.getUid();
        return uid == null ? "" : uid;
    }

    public boolean isAuthorized() {
        return mAuth.getCurrentUser() != null;
    }

    public DatabaseReference getUserInfo() {
        return mRef.child("user_info").child(getUID());
    }

    public DatabaseReference getUserName() {
        return getUserInfo().child("name");
    }

    public DatabaseReference getUserDescription() {
        return getUserInfo().child("description");
    }


    // Actions

    public DatabaseReference getActions() {
        return mRef.child("actions").child(getUID());
    }

    public DatabaseReference getAction(@NotNull String key) {
        return getActions().child(key);
    }

    public DatabaseReference getActionName(@NotNull String key) {
        return getActions().child(key).child("name");
    }

    public String addAction(UserAction ua) {
        String key = getActions().push().getKey();
        getAction(key).setValue(ua);
        initPositions(getActions());
        return key;
    }

    public void deleteAction(@NotNull String key) {
        getStages(key).removeValue();
        getAction(key).removeValue();
    }

    // Stages of action

    public String addStage(@NotNull String actionName, Stage stage) {
        String key = getStages(actionName).push().getKey();
        getStage(actionName, key).setValue(stage);
        initPositions(getStages(actionName));
        return key;
    }

    public DatabaseReference getAllStages() {
        return mRef.child("stages").child(getUID());
    }

    public DatabaseReference getStages(@NotNull String key) {
        return getAllStages().child(key);
    }

    public DatabaseReference getStage(@NotNull String actionName, @NotNull String key) {
        return getStages(actionName).child(key);
    }

    public void deleteStage(@NotNull String actionName, @NotNull String key) {
        getStage(actionName, key).removeValue();
    }

    // Other

    public static void resetPositions(@NotNull DatabaseReference ref, List<String> keys) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() != null)
                    for (int i = 0; i < keys.size(); i++)
                        if (currentData.child(keys.get(i)).getValue() != null)
                            currentData.child(keys.get(i)).child("pos").setValue(i+1);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed,
                                   @Nullable DataSnapshot currentData) {}
        });
    }

    public static void initPositions(@NotNull DatabaseReference ref) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null || !currentData.hasChildren())
                    return Transaction.success(currentData);

                int max_pos = 0;
                Integer i;

                for (MutableData item: currentData.getChildren()) {
                    i = item.child("pos").getValue(Integer.class);
                    if (i != null && i != Integer.MAX_VALUE && i > max_pos)
                        max_pos = i;
                }

                for (MutableData item: currentData.getChildren()) {
                    i = item.child("pos").getValue(Integer.class);
                    if (i == null || i == Integer.MAX_VALUE)
                        item.child("pos").setValue(++max_pos);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed,
                                   @Nullable DataSnapshot currentData) {}
        });
    }

    public Query getSorted(@NotNull Query ref) {
        return ref.orderByChild("pos");
    }

    public static <T extends FBModel> List<String> getKeys(List<T> list) {
        ArrayList<String> keys = new ArrayList<>();
        for (T item: list) keys.add(item.getKey());
        return keys;
    }

    @FunctionalInterface
    public interface OnError {
        void react(String reason);
    }

    public static class ValueListEventListener<T> extends AValueEventListener<T> {
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

    public static class ValueEventListener<T> extends AValueEventListener<T> {
        protected MutableLiveData<T> live;
        protected Class<T> className;

        public ValueEventListener(MutableLiveData<T> live, Class<T> className, OnError onError) {
            this.live = live;
            this.className = className;
            this.onError = onError;
        }

        public ValueEventListener(MutableLiveData<T> live, Class<T> className) {
            this(live, className, null);
        }

        protected T modify(T item, DataSnapshot snapshot) {
            if (item instanceof FBModel)
                ((FBModel)item).setKey(snapshot.getKey());
            return item;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            live.postValue(snapshot.exists() ?
                    modify(snapshot.getValue(className), snapshot):
                    null
            );
        }
    }

    public abstract static class AValueEventListener<T> implements com.google.firebase.database.ValueEventListener {
        protected OnError onError;

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            if (onError != null)
                onError.react(error.getMessage());
        }

        protected T modify(T item, DataSnapshot snapshot) {
            if (item instanceof FBModel)
                ((FBModel)item).setKey(snapshot.getKey());
            return item;
        }
    }

    public abstract static class ValueRemovedListener implements com.google.firebase.database.ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.exists()) onValueRemoved();
        }

        protected void onValueRemoved() {}

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    }
}
