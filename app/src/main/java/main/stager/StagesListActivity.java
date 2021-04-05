package main.stager;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import main.stager.adapters.StageItemRecyclerViewAdapter;

public class StagesListActivity extends AuthorizedOnlyActivity {
    static public final String ARG_ACTION_NAME = "Stager.stages_list_activity.param_action_name";
    private StagesListViewModel viewModel;

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

        viewModel = (new ViewModelProvider(this)).get(StagesListViewModel.class);

        StageItemRecyclerViewAdapter adapter = new StageItemRecyclerViewAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stages_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getStages(actionName).observe(this, adapter::setValues);

    }
}