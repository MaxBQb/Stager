package main.stager.ui.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import main.stager.R;

public class ActionsListFragment extends Fragment {
    private ActionsListViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ActionsListViewModel.class);

        View view = inflater.inflate(R.layout.fragment_actions, container, false);
        NavController navController = ((NavHostFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
        ActionItemRecyclerViewAdapter adapter = new ActionItemRecyclerViewAdapter(
                navController
        );
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        StatesRecyclerViewAdapter srvAdapter = new StatesRecyclerViewAdapter(adapter,
                null, null, null
        );
        recyclerView.setAdapter(srvAdapter);

        viewModel.getActions().observe(getViewLifecycleOwner(), adapter::setValues);

        view.findViewById(R.id.button_list_add).setOnClickListener(v ->
            navController.navigate(R.id.transition_actions_list_to_add_action)
        );

        return view;
    }
}