package main.stager.ui.action_stages;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import main.stager.R;
import main.stager.SmartActivity;

public class StagesListFragment extends Fragment {
    static public final String ARG_ACTION_NAME = "Stager.stages_list.param_action_name";
    static public final String ARG_ACTION_KEY = "Stager.stages_list.param_action_key";
    private String mActionName;
    private String mActionKey;
    private StagesListViewModel viewModel;

    public StagesListFragment() {
        // Required empty public constructor
    }

    public static StagesListFragment newInstance(String key, String actionName) {
        StagesListFragment fragment = new StagesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION_NAME, actionName);
        args.putString(ARG_ACTION_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mActionName = getArguments().getString(ARG_ACTION_NAME);
            mActionKey = getArguments().getString(ARG_ACTION_KEY);
        } else {
            mActionName = getString(R.string.stages_list_activity_untitled_action);
            mActionKey = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ((SmartActivity)getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.stages_list_activity_label, mActionName));

        viewModel = new ViewModelProvider(this).get(StagesListViewModel.class);
        StageItemRecyclerViewAdapter adapter = new StageItemRecyclerViewAdapter();

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getStages(mActionKey).observe(getViewLifecycleOwner(), adapter::setValues);
        viewModel.getActionName(mActionKey, mActionName).observe(getViewLifecycleOwner(),
                (String text) -> ((SmartActivity)getActivity())
                        .getSupportActionBar()
                        .setTitle(getString(R.string.stages_list_activity_label, text)));
        return view;
    }
}