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

        if (!dataProvider.isAuthorized()) return;

       dataProvider.getAction(actionName).get()
            .addOnCompleteListener((Task<DataSnapshot> task) -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    UserAction ua = task.getResult().getValue(UserAction.class);
                    boolean isStagesListEmpty = (ua == null || ua.getStages() == null || ua.getStages().isEmpty());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.stages_container, isStagesListEmpty ?
                                    NoItemsFillerFragment.newInstance(
                                        getString(R.string.stages_list_activity_no_items_message)
                                    ) : ListFragment.newInstance(
                                        new StageItemRecyclerViewAdapter(ua.getStages()
                                    )
                            )).commit();
                }
        });
    }
}