package main.stager.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.function.Consumer;

import main.stager.model.FBModel;
import main.stager.model.Stage;
import main.stager.model.Status;
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

    public DatabaseReference getActionStatus(@NotNull String key) {
        return getAction(key).child("status");
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

    public DatabaseReference getStageName(@NotNull String actionName, @NotNull String key) {
        return getStage(actionName, key).child("name");
    }

    public DatabaseReference getStageStatus(@NotNull String actionName, @NotNull String key) {
        return getStage(actionName, key).child("currentStatus");
    }

    public void setStageStatusSucceed(@NotNull String actionKey, @NotNull String key) {
        getStages(actionKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (!currentData.hasChildren())
                    return Transaction.success(currentData);

                Stage target = currentData.child(key).getValue(Stage.class);
                if (target == null)
                    return Transaction.abort();

                int pos = target.getPos();
                if (pos == Integer.MAX_VALUE)
                    return Transaction.abort();

                Stage stage;
                for (MutableData item: currentData.getChildren()) {
                    stage = item.getValue(Stage.class);
                    if (stage == null)
                        return Transaction.abort();

                    if (stage.getPos() < pos &&
                        stage.getCurrentStatus() != Status.SUCCEED)
                        return Transaction.abort();
                }

                currentData.child(key).child("currentStatus").setValue(Status.SUCCEED);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed, @Nullable DataSnapshot currentData) {
                if (!committed || !currentData.exists())
                    return;

                for (DataSnapshot item : currentData.getChildren())
                    if (item.child("currentStatus").getValue(Status.class) != Status.SUCCEED)
                        return;

                getActionStatus(actionKey).setValue(Status.SUCCEED);
            }
        });
    }

    public void setStageStatusAborted(@NotNull String actionKey, @NotNull String key) {
        getStages(actionKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (!currentData.hasChildren())
                    return Transaction.success(currentData);

                Stage target = currentData.child(key).getValue(Stage.class);
                if (target == null)
                    return Transaction.abort();

                int pos = target.getPos();
                if (pos == Integer.MAX_VALUE)
                    return Transaction.abort();

                List<String> lockList = new ArrayList<>();

                Stage stage;
                for (MutableData item: currentData.getChildren()) {
                    stage = item.getValue(Stage.class);
                    if (stage == null)
                        return Transaction.abort();

                    if (stage.getPos() < pos &&
                        stage.getCurrentStatus() != Status.SUCCEED)
                        return Transaction.abort();

                    if (stage.getPos() > pos) {
                        if (stage.getCurrentStatus() != Status.WAITING &&
                            stage.getCurrentStatus() != Status.LOCKED)
                            return Transaction.abort();
                        lockList.add(item.getKey());
                    }
                }

                currentData.child(key).child("currentStatus").setValue(Status.ABORTED);

                for (String lock_key: lockList)
                    currentData.child(lock_key).child("currentStatus").setValue(Status.LOCKED);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed, @Nullable DataSnapshot currentData) {
                if (!committed || !currentData.exists())
                    return;

                getActionStatus(actionKey).setValue(Status.ABORTED);
            }
        });
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
}
