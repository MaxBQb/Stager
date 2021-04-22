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

    /** Path safe, not null uid
     * @return uid or empty string
     */
    public @NotNull String getUID() {
        String uid = mAuth.getUid();
        return uid == null ? "" : uid;
    }

    private void keepSynced() {
        DatabaseReference[] sync = new DatabaseReference[]{
                getActions(),
                getAllStages()
        };

        for (DatabaseReference dr: sync)
            dr.keepSynced(true);
    }

    public boolean isAuthorized() {
        return mAuth.getCurrentUser() != null;
    }

    public DatabaseReference getActions() {
        return mRef.child("actions").child(getUID());
    }

    public Query getActionsSorted() {
        return getSorted(getActions());
    }

    public Query getStagesSorted(@NotNull String key) {
        return getSorted(getStages(key));
    }

    private Query getSorted(@NotNull DatabaseReference ref) {
        return ref.orderByChild("pos");
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
        return key;
    }

    public String addStage(@NotNull String actionName, Stage stage) {
        String key = getStages(actionName).push().getKey();
        getStage(actionName, key).setValue(stage);
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

    private static DatabaseReference getPosition(@NotNull DatabaseReference ref,
                                                @NotNull String key) {
        return ref.child(key).child("pos");
    }

    public static void setPosition(@NotNull DatabaseReference ref, @NotNull String key,
                                    int pos) {
        getPosition(ref, key).setValue(pos);
    }

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

    public static <T extends FBModel> List<String> getKeys(List<T> list) {
        ArrayList<String> keys = new ArrayList<>();
        for (T item: list) keys.add(item.getKey());
        return keys;
    }

    public void deleteStage(@NotNull String actionName, @NotNull String key) {
        getStage(actionName, key).removeValue();
    }

    public void deleteAction(@NotNull String key) {
        getStages(key).removeValue();
        getAction(key).removeValue();
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
            liveList.postValue(listModify(lst));
        }

        protected List<T> listModify(List<T> list) {
            if (list != null && !list.isEmpty() &&
                list.get(0) instanceof FBModel &&
                backPathModify() != null) {
                int counter = list.size();
                for (T itm : list)
                    if (((FBModel) itm).getPos() == Integer.MAX_VALUE)
                        setPosition(backPathModify(), ((FBModel) itm).getKey(), ++counter);
            }
            return list;
        }

        protected DatabaseReference backPathModify() { return null; }
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
