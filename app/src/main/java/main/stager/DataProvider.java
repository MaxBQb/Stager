package main.stager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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

    public DataProvider() {
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

    public boolean isAuthorized() {
        return mAuth.getCurrentUser() != null;
    }

    public DatabaseReference getActions() {
        return mRef.child("actions").child(mAuth.getUid());
    }

    public DatabaseReference getAction(String key) {
        return getActions().child(key);
    }

    public String addAction(UserAction ua) {
        String key = getActions().push().getKey();
        getAction(key).setValue(ua);
        return key;
    }

    public String addStage(String actionName, Stage stage) {
        String key = getStages(actionName).push().getKey();
        getStage(actionName, key).setValue(stage);
        return key;
    }

    public DatabaseReference getAllStages() {
        return mRef.child("stages").child(mAuth.getUid());
    }

    public DatabaseReference getStages(String key) {
        return getAllStages().child(key);
    }

    public DatabaseReference getStage(String actionName, String key) {
        return getStages(actionName).child(key);
    }

    public DatabaseReference getUserInfo() {
        return mRef.child("user_info").child(mAuth.getUid());
    }

    public DatabaseReference getUserName() {
        return getUserInfo().child("name");
    }

    public DatabaseReference getUserDescription() {
        return getUserInfo().child("description");
    }

    public interface OnError {
        void react(String reason);
    }

    public static class ValueListEventListener<T> extends AValueEventListener {
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

        protected T modify(T item, DataSnapshot snapshot) { return item; }

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
    };

    public static class ValueEventListener<T> extends AValueEventListener {
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

        protected T modify(T item, DataSnapshot snapshot) { return item; }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            live.postValue(snapshot.exists() ?
                    modify(snapshot.getValue(className), snapshot):
                    null
            );
        }
    };

    public abstract static class AValueEventListener implements com.google.firebase.database.ValueEventListener {
        protected OnError onError;

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            if (onError != null)
                onError.react(error.getMessage());
        }
    };

}
