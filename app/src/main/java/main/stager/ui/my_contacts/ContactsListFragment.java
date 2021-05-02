package main.stager.ui.my_contacts;

import android.os.Bundle;
import android.view.View;

import main.stager.R;
import main.stager.list.StagerExtendableList;
import main.stager.model.Contact;
import main.stager.ui.edit_item.edit_action.EditActionFragment;

public class ContactsListFragment extends
        StagerExtendableList<ContactsListViewModel, ContactRecyclerViewAdapter, Contact> {
    @Override
    protected Class<ContactsListViewModel> getViewModelType() {
        return ContactsListViewModel.class;
    }

    @Override
    protected Class<ContactRecyclerViewAdapter> getAdapterType() {
        return ContactRecyclerViewAdapter.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_my_contacts;
    }

    @Override
    protected void onItemClick(Contact item, int pos) {
        super.onItemClick(item, pos);
        Bundle args = new Bundle();
        args.putString(EditActionFragment.ARG_ACTION_NAME, item.getName());
        args.putString(EditActionFragment.ARG_ACTION_KEY, item.getKey());
        navigator.navigate(R.id.transition_actions_list_to_action_stages_list, args);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        navigator.navigate(R.id.transition_actions_list_to_add_action);
    }
}