package main.stager.ui.contact_info;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.google.firebase.database.Query;
import main.stager.Base.StagerVMFragment;
import main.stager.R;
import main.stager.UserAvatar;
import main.stager.utils.Utilits;

public class ContactInfoFragment extends
        StagerVMFragment<ContactInfoViewModel> {
    static public final String ARG_CONTACT_NAME = "Stager.contact_info.param_contact_name";
    static public final String ARG_CONTACT_KEY = "Stager.contact_info.param_contact_key";
    static public final String ARG_CONTACT_TYPE = "Stager.contact_info.param_contact_type";

    private String key;
    private String name;
    private ContactType type;
    private TextView nameView;
    private TextView emailView;
    private TextView descriptionView;
    public UserAvatar mAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = Utilits.getDefaultOnNullOrBlank(
                    getArguments().getString(ARG_CONTACT_NAME),
                    getString(R.string.ContactInfoFragment_message_AnonymousUser));
            type = ContactType.valueOf(Utilits.getDefaultOnNullOrBlank(
                    getArguments().getString(ARG_CONTACT_TYPE),
                    ContactType.ACCEPTED.toString()));
            key = getArguments().getString(ARG_CONTACT_KEY);
        } else {
            name = getString(R.string.ContactInfoFragment_message_AnonymousUser);
            key = "";
            type = ContactType.ACCEPTED;
        }
    }

    private @StringRes int getHeaderTitle() {
        switch (type) {
            case ACCEPTED:
                return R.string.ContactInfoFragment_header_type_accepted;
            case IGNORED:
                return R.string.ContactInfoFragment_header_type_ignored;
            case INCOMING:
                return R.string.ContactInfoFragment_header_type_incoming;
            case OUTGOING:
                return R.string.ContactInfoFragment_header_type_outgoing;
            default:
                throw new IllegalStateException("Unsupported ContactType");
        }
    }

    @Override
    protected Class<ContactInfoViewModel> getViewModelType() {
        return ContactInfoViewModel.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_contact_info;
    }

    @Override
    protected void setEventListeners() {
        super.setEventListeners();
        if (type != ContactType.OUTGOING && type != ContactType.ACCEPTED) {
            view.findViewById(R.id.btn_accept).setOnClickListener((v) ->
                dataProvider.acceptContactRequest(key));
            view.findViewById(R.id.btn_ignore).setOnClickListener(
                    type != ContactType.IGNORED
                    ? (v) -> dataProvider.ignoreContactRequest(key)
                    : (v) -> dataProvider.removeIgnoredContactRequest(key));
        } else if (type == ContactType.OUTGOING)
            view.findViewById(R.id.btn_revoke).setOnClickListener((v) ->
                dataProvider.removeOutgoingContactRequest(key));
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        mAvatar = view.findViewById(R.id.user_avatar);
        mAvatar.setUserName(name);
        nameView = view.findViewById(R.id.personName);
        emailView = view.findViewById(R.id.contact_email);
        descriptionView = view.findViewById(R.id.description);

        View incomingRequestControls = view.findViewById(R.id.incoming_request_controls);

        if (type == ContactType.INCOMING || type == ContactType.IGNORED)
            incomingRequestControls.setVisibility(View.VISIBLE);

        if (type == ContactType.IGNORED)
            incomingRequestControls.setAlpha(0.7f);

        if (type == ContactType.OUTGOING)
            view.findViewById(R.id.outgoing_request_controls).setVisibility(View.VISIBLE);

        ((TextView)view.findViewById(R.id.header_title)).setText(getHeaderTitle());
        updateTitle(name);
    }

    private void updateTitle(String text) {
        getActionBar().setTitle(getString(R.string.ContactInfoFragment_title,
            Utilits.getDefaultOnNullOrBlank(text,
                getString(R.string.ContactInfoFragment_message_AnonymousUser
        ))));
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        bindData(viewModel.getContact(), (contact) -> {
            name = Utilits.getDefaultOnNullOrBlank(contact.getName(),
                    getString(R.string.ContactInfoFragment_message_AnonymousUser));
            nameView.setText(name);

            mAvatar.setUserName(name);
            mAvatar.setEmail(contact.getEmail());

            if (Utilits.isNullOrBlank(contact.getEmail())) {
                view.findViewById(R.id.LinearLayout_email).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.LinearLayout_email).setVisibility(View.VISIBLE);
                emailView.setText(contact.getEmail());
            }

            if (Utilits.isNullOrBlank(contact.getDescription())) {
                view.findViewById(R.id.LinearLayout_description).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.LinearLayout_description).setVisibility(View.VISIBLE);
                descriptionView.setText(contact.getDescription());
                descriptionView.setMovementMethod(new ScrollingMovementMethod());
            }

            updateTitle(name);
        });
    }

    @Override
    protected void setViewModelData() {
        super.setViewModelData();
        viewModel.setKey(key);
    }

    @Override
    protected void setDependencies() {
        super.setDependencies();
        dependencies.add(dataProvider.getUserInfo(key));
        dependencies.add(getDependencyByType());
    }

    private Query getDependencyByType() {
        switch (type) {
            case IGNORED: return dataProvider.getIgnoredContactRequest(key);
            case INCOMING: return dataProvider.getIncomingContactRequest(key);
            case OUTGOING: return dataProvider.getOutgoingContactRequest(key);
            case ACCEPTED: return dataProvider.getContact(key);
        }
        throw new IllegalStateException("ContactType incorrect");
    }
}