package main.stager.ui.find_new_contacts;

import android.widget.Toast;
import main.stager.R;
import main.stager.list.StagerList;
import main.stager.model.Contact;
import main.stager.ui.my_contacts.ContactRecyclerViewAdapter;
import main.stager.utils.Utilits;

public class FindNewContactsFragment extends
        StagerList<FindNewContactsViewModel, ContactRecyclerViewAdapter, Contact> {

    @Override
    public boolean ALLOW_SEARCH() { return true; }

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
        dataProvider.makeContactRequest(item.getKey()).addOnSuccessListener(e -> {
            Toast.makeText(getContext(),
                    getString(R.string.FindNewContactsFragment_message_success, item.getName()),
                    Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), R.string.FindNewContactsFragment_message_denied,
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onSearchQueryChange(String query) {
        super.onSearchQueryChange(query);
        if (Utilits.isNullOrBlank(query)) return;
        viewModel.setQuery(query.trim());
    }
}