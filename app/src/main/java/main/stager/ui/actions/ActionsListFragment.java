package main.stager.ui.actions;

import android.view.View;
import main.stager.R;
import main.stager.StagerExtendableListFragment;

public class ActionsListFragment extends StagerExtendableListFragment<ActionsListViewModel, ActionItemRecyclerViewAdapter> {
    @Override
    protected Class<ActionsListViewModel> getViewModelType() {
        return ActionsListViewModel.class;
    }

    @Override
    protected Class<ActionItemRecyclerViewAdapter> getAdapterType() {
        return ActionItemRecyclerViewAdapter.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_actions;
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        viewModel.getActions().observe(getViewLifecycleOwner(), adapter::setValues);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        navigator.navigate(R.id.transition_actions_list_to_add_action);
    }
}