package main.stager.ui.activity_stages;

import android.content.Context;
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
    private String mActionName;
    private StagesListViewModel viewModel;

    public StagesListFragment() {
        // Required empty public constructor
    }

    public static StagesListFragment newInstance(String actionName) {
        StagesListFragment fragment = new StagesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION_NAME, actionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionName = getArguments() != null ?
            getArguments().getString(ARG_ACTION_NAME) :
            getString(R.string.stages_list_activity_untitled_action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ((SmartActivity)getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.stages_list_activity_label, mActionName));
        viewModel = (new ViewModelProvider(this)).get(StagesListViewModel.class);
        StageItemRecyclerViewAdapter adapter = new StageItemRecyclerViewAdapter();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getStages(mActionName).observe(getViewLifecycleOwner(), adapter::setValues);
        return view;
    }
}