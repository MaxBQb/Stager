package main.stager.ui.find_new_contacts;

import android.view.View;
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
    protected String getQueryHintText() {
        return getString(R.string.FindNewContactsFragment_message_queryHint);
    }

    @Override
    protected void onItemClick(Contact item, int pos, View view) {
        super.onItemClick(item, pos, view);
        final float selected = 0.8f;
        final float disabled = 0.2f;
        final float reset = 1f;
        if (view.getAlpha() == selected) {
            dataProvider.removeOutgoingContactRequest(item.getKey()).addOnCompleteListener(e -> {
                Toast.makeText(getContext(),
                        getString(R.string.FindNewContactsFragment_message_revoke, item.getName()),
                        Toast.LENGTH_SHORT).show();
                view.setAlpha(reset);
            });
            return;
        }
        dataProvider.makeContactRequest(item.getKey()).addOnSuccessListener(e -> {
            Toast.makeText(getContext(),
                    getString(R.string.FindNewContactsFragment_message_success, item.getName()),
                    Toast.LENGTH_SHORT).show();
            view.setAlpha(selected);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), R.string.FindNewContactsFragment_message_denied,
                    Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
            view.setAlpha(disabled);
        });
    }

    @Override
    public void onSearchQueryChange(String query) {
        super.onSearchQueryChange(query);
        if (Utilits.isNullOrBlank(query)) return;
        onDataLoadingStarted();
        viewModel.setQuery(query.trim());
    }
}