package main.stager;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import main.stager.adapters.StageItemRecyclerViewAdapter;
import main.stager.model.UserAction;

public class StagesListActivity extends AuthorizedOnlyActivity {
    static public final String ARG_ACTION_NAME = "Stager.stages_list_activity.param_action_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages_list);
        String actionName = getString(R.string.stages_list_activity_untitled_action);
        try {
            actionName = getIntent().getExtras().getString(ARG_ACTION_NAME);
        } catch (NullPointerException ignore) {}
        setTitle(getString(R.string.stages_list_activity_label, actionName));

        if (mAuth.getCurrentUser() == null) return;

        mRef.child("users")
            .child(mAuth.getUid())
            .child("actions").child(actionName).get()
            .addOnCompleteListener((Task<DataSnapshot> task) -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    UserAction ua = task.getResult().getValue(UserAction.class);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.stages_container, ListFragment.newInstance(
                                    new StageItemRecyclerViewAdapter(ua.getStages())
                            )).commit();
                }
        });
    }
}