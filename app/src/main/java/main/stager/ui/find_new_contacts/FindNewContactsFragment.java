package main.stager.ui.find_new_contacts;

import android.os.Bundle;

import main.stager.R;
import main.stager.list.StagerList;
import main.stager.model.Contact;
import main.stager.ui.edit_item.edit_action.EditActionFragment;
import main.stager.ui.my_contacts.ContactRecyclerViewAdapter;

public class FindNewContactsFragment extends
        StagerList<FindNewContactsViewModel, ContactRecyclerViewAdapter, Contact> {
    @Override
    protected Class<FindNewContactsViewModel> getViewModelType() {
        return FindNewContactsViewModel.class;
    }

    @Override
    protected Class<ContactRecyclerViewAdapter> getAdapterType() {
        return ContactRecyclerViewAdapter.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_find_contacts;
    }

    @Override
    protected void onItemClick(Contact item, int pos) {
        super.onItemClick(item, pos);
        Bundle args = new Bundle();
        args.putString(EditActionFragment.ARG_ACTION_NAME, item.getName());
        args.putString(EditActionFragment.ARG_ACTION_KEY, item.getKey());
        navigator.navigate(R.id.transition_actions_list_to_action_stages_list, args);
    }
}