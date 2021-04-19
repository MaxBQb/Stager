package main.stager.ui.actions;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import main.stager.utils.DataProvider;
import main.stager.R;
import main.stager.list.StagerExtendableList;
import main.stager.model.UserAction;
import main.stager.ui.action_stages.StagesListFragment;

public class ActionsListFragment extends
        StagerExtendableList<ActionsListViewModel, ActionItemRecyclerViewAdapter, UserAction> {
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
    protected LiveData<List<UserAction>> getList(DataProvider.OnError onError) {
        return viewModel.getActions(onError);
    }

    @Override
    public void onItemSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int pos, int direction) {
        viewModel.deleteAction(adapter.get(pos));
    }

    @Override
    protected void onItemClick(UserAction item, int pos) {
        super.onItemClick(item, pos);
        Bundle args = new Bundle();
        args.putString(StagesListFragment.ARG_ACTION_NAME, item.getName());
        args.putString(StagesListFragment.ARG_ACTION_KEY, item.getKey());
        navigator.navigate(R.id.transition_actions_list_to_action_stages_list, args);
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        list.observe(getViewLifecycleOwner(), adapter::setValues);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        navigator.navigate(R.id.transition_actions_list_to_add_action);
    }
}