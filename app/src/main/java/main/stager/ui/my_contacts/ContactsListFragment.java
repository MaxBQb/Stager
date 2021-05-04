package main.stager.ui.my_contacts;

import android.os.Bundle;
import android.view.View;

import main.stager.R;
import main.stager.list.StagerExtendableList;
import main.stager.model.Contact;
import main.stager.ui.contact_info.ContactInfoFragment;
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
        args.putString(ContactInfoFragment.ARG_CONTACT_NAME, item.getName());
        args.putString(ContactInfoFragment.ARG_CONTACT_KEY, item.getKey());
        navigator.navigate(R.id.transition_my_contacts_to_contact_info, args);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        navigator.navigate(R.id.transition_my_contacts_to_find_new_contacts);
    }
}