package main.stager.ui.actions;

import android.view.View;

import androidx.lifecycle.LiveData;

import java.util.List;

import main.stager.R;
import main.stager.StagerExtendableListFragment;
import main.stager.model.UserAction;

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
        LiveData<List<UserAction>> ld = viewModel.getActions(this::onError);
        ld.observe(getViewLifecycleOwner(), adapter::setValues);
        ld.observe(getViewLifecycleOwner(), this::reactState);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        navigator.navigate(R.id.transition_actions_list_to_add_action);
    }
}