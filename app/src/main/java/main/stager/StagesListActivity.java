package main.stager;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import main.stager.adapters.StageItemRecyclerViewAdapter;
import main.stager.model.UserAction;

public class StagesListActivity extends AuthorizedOnlyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages_list);

        if (mAuth.getCurrentUser() == null) return;

        mRef.child("users")
            .child(mAuth.getUid())
            .child("actions").child("first_action").get()
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