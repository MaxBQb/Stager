package main.stager.ui.monitored_actions.all;

import main.stager.R;
import main.stager.list.StagerList;
import main.stager.model.UserAction;
import main.stager.ui.actions.ActionItemRecyclerViewAdapter;

public class AllMonitoredActionsListFragment extends
        StagerList<AllMonitoredActionsListViewModel, ActionItemRecyclerViewAdapter, UserAction> {
    @Override
    protected Class<AllMonitoredActionsListViewModel> getViewModelType() {
        return AllMonitoredActionsListViewModel.class;
    }

    @Override
    protected Class<ActionItemRecyclerViewAdapter> getAdapterType() {
        return ActionItemRecyclerViewAdapter.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_monitored_actions_all;
    }
}