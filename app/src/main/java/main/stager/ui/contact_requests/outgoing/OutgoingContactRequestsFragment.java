package main.stager.ui.contact_requests.outgoing;

import android.os.Bundle;
import main.stager.R;
import main.stager.list.StagerList;
import main.stager.model.Contact;
import main.stager.ui.contact_info.ContactInfoFragment;
import main.stager.ui.contact_info.ContactType;
import main.stager.ui.my_contacts.ContactRecyclerViewAdapter;

public class OutgoingContactRequestsFragment extends
        StagerList<OutgoingContactRequestsViewModel, ContactRecyclerViewAdapter, Contact> {

    @Override
    protected Class<OutgoingContactRequestsViewModel> getViewModelType() {
        return OutgoingContactRequestsViewModel.class;
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
        args.putString(ContactInfoFragment.ARG_CONTACT_NAME, item.getName());
        args.putString(ContactInfoFragment.ARG_CONTACT_KEY, item.getKey());
        args.putString(ContactInfoFragment.ARG_CONTACT_TYPE, ContactType.OUTCOME.name());
        navigator.navigate(R.id.transition_contact_requests_to_contact_info, args);
    }
}