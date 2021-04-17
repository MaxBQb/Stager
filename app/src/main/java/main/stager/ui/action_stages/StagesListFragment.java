package main.stager.ui.action_stages;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import main.stager.R;
import main.stager.SmartActivity;
import main.stager.ui.add_action_stage.AddStageFragment;

public class StagesListFragment extends Fragment {
    static public final String ARG_ACTION_NAME = "Stager.stages_list.param_action_name";
    static public final String ARG_ACTION_KEY = "Stager.stages_list.param_action_key";
    private String mActionName;
    private String mActionKey;
    private StagesListViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mActionName = getArguments().getString(ARG_ACTION_NAME);
            mActionKey = getArguments().getString(ARG_ACTION_KEY);
        } else {
            mActionName = getString(R.string.StagesFragment_ActivityList_UntitledAction);
            mActionKey = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_stages, container, false);
        ((SmartActivity)getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.StagesFragment_label, mActionName));

        viewModel = new ViewModelProvider(this).get(StagesListViewModel.class);
        NavController navController = ((NavHostFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
        StageItemRecyclerViewAdapter adapter = new StageItemRecyclerViewAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getStages(mActionKey).observe(getViewLifecycleOwner(), adapter::setValues);
        viewModel.getActionName(mActionKey, mActionName).observe(getViewLifecycleOwner(),
                (String text) -> ((SmartActivity)getActivity())
                        .getSupportActionBar()
                        .setTitle(getString(R.string.StagesFragment_label, text)));

        view.findViewById(R.id.button_list_add).setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(AddStageFragment.ARG_ACTION_KEY, mActionKey);
            navController.navigate(R.id.transition_action_stages_to_add_stage, args);
        });

        return view;
    }
}