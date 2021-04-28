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
        mRef = db.getReference(PATH.MAIN_DB);
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

    // CONSTANTS
    private static final class PATH {
        // Common
        public static final String MAIN_DB = "stager-main-db";
        public static final String FB_POS = "pos";

        // User
        public static final String USER_INFO = "user_info";
        public static final String USER_NAME = "name";
        public static final String USER_DESCRIPTION = "description";

        // Actions
        public static final String ACTIONS = "actions";
        public static final String ACTION_STATUS = "status";
        public static final String ACTION_NAME = "name";

        // Action stages
        public static final String STAGES = "stages";
        public static final String STAGE_STATUS = "currentStatus";
        public static final String STAGE_NAME = "name";
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
        return mRef.child(PATH.USER_INFO).child(getUID());
    }

    public DatabaseReference getUserName() {
        return getUserInfo().child(PATH.USER_NAME);
    }

    public DatabaseReference getUserDescription() {
        return getUserInfo().child(PATH.USER_DESCRIPTION);
    }


    // Actions

    public DatabaseReference getActions() {
        return mRef.child(PATH.ACTIONS).child(getUID());
    }

    public DatabaseReference getAction(@NotNull String key) {
        return getActions().child(key);
    }

    public DatabaseReference getActionStatus(@NotNull String key) {
        return getAction(key).child(PATH.ACTION_STATUS);
    }

    public DatabaseReference getActionName(@NotNull String key) {
        return getActions().child(key).child(PATH.ACTION_NAME);
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

    public String addStage(@NotNull String actionKey, Stage stage) {
        String key = getStages(actionKey).push().getKey();
        getStage(actionKey, key).setValue(stage);
        initPositions(getStages(actionKey));
        return key;
    }

    public DatabaseReference getAllStages() {
        return mRef.child(PATH.STAGES).child(getUID());
    }

    public DatabaseReference getStages(@NotNull String actionKey) {
        return getAllStages().child(actionKey);
    }

    public DatabaseReference getStage(@NotNull String actionKey, @NotNull String stageKey) {
        return getStages(actionKey).child(stageKey);
    }

    public DatabaseReference getStageName(@NotNull String actionKey, @NotNull String stageKey) {
        return getStage(actionKey, stageKey).child(PATH.STAGE_NAME);
    }

    public DatabaseReference getStageStatus(@NotNull String actionName, @NotNull String stageKey) {
        return getStage(actionName, stageKey).child(PATH.STAGE_STATUS);
    }

    public void setStageStatusSucceed(@NotNull String actionKey, @NotNull String stageKey) {
        getStages(actionKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (!currentData.hasChildren())
                    return Transaction.success(currentData);

                Stage target = currentData.child(stageKey).getValue(Stage.class);
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

                currentData.child(stageKey).child(PATH.STAGE_STATUS).setValue(Status.SUCCEED);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed, @Nullable DataSnapshot currentData) {
                if (!committed || currentData == null || !currentData.exists())
                    return;

                for (DataSnapshot item : currentData.getChildren())
                    if (item.child(PATH.STAGE_STATUS).getValue(Status.class) != Status.SUCCEED)
                        return;

                getActionStatus(actionKey).setValue(Status.SUCCEED);
            }
        });
    }

    public void setStageStatusAborted(@NotNull String actionKey, @NotNull String stageKey) {
        getStages(actionKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (!currentData.hasChildren())
                    return Transaction.success(currentData);

                Stage target = currentData.child(stageKey).getValue(Stage.class);
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

                currentData.child(stageKey).child(PATH.STAGE_STATUS).setValue(Status.ABORTED);

                for (String lock_key: lockList)
                    currentData.child(lock_key).child(PATH.STAGE_STATUS).setValue(Status.LOCKED);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed, @Nullable DataSnapshot currentData) {
                if (!committed || currentData == null || !currentData.exists())
                    return;

                getActionStatus(actionKey).setValue(Status.ABORTED);
            }
        });
    }

    public void deleteStage(@NotNull String actionKey, @NotNull String stageKey) {
        getStage(actionKey, stageKey).removeValue();
    }

    // Other

    public static <T> void trySetValue(@NotNull DatabaseReference ref, T value) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NotNull MutableData currentData) {
                if (currentData.getKey() == null)
                    return Transaction.abort();
                currentData.setValue(value);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error,
                                   boolean committed,
                                   @Nullable DataSnapshot currentData) {}
        });
    }

    public static void resetPositions(@NotNull DatabaseReference ref, List<String> keys) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() != null)
                    for (int i = 0; i < keys.size(); i++)
                        if (currentData.child(keys.get(i)).getValue() != null)
                            currentData.child(keys.get(i)).child(PATH.FB_POS).setValue(i+1);
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
                    i = item.child(PATH.FB_POS).getValue(Integer.class);
                    if (i != null && i != Integer.MAX_VALUE && i > max_pos)
                        max_pos = i;
                }

                for (MutableData item: currentData.getChildren()) {
                    i = item.child(PATH.FB_POS).getValue(Integer.class);
                    if (i == null || i == Integer.MAX_VALUE)
                        item.child(PATH.FB_POS).setValue(++max_pos);
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
        return ref.orderByChild(PATH.FB_POS);
    }

    @NotNull
    public static <T extends FBModel> List<String> getKeys(@NotNull List<T> list) {
        ArrayList<String> keys = new ArrayList<>();
        for (T item: list) keys.add(item.getKey());
        return keys;
    }
}
