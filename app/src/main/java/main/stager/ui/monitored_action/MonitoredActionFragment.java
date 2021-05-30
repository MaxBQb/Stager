package main.stager.ui.monitored_action;

import android.os.Bundle;
import android.widget.Toast;
import main.stager.R;
import main.stager.StagerApplication;
import main.stager.list.StagerList;
import main.stager.model.Stage;
import main.stager.ui.contact_info.ContactInfoFragment;
import main.stager.ui.edit_item.edit_action.ActionStageRecyclerViewAdapter;
import main.stager.utils.Utilits;
import main.stager.utils.pushNotifications.ListenedEventsController;

public class MonitoredActionFragment
        extends StagerList<MonitoredActionViewModel, ActionStageRecyclerViewAdapter, Stage> {
    static public final String ARG_ACTION_NAME = "Stager.monitored_action.param_action_name";
    static public final String ARG_ACTION_KEY = "Stager.monitored_action.param_action_key";
    static public final String ARG_ACTION_OWNER = "Stager.monitored_action.param_action_owner";
    private String mActionName;
    private String mActionKey;
    private String mActionOwner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mActionName = Utilits.getDefaultOnNullOrBlank(getArguments().getString(ARG_ACTION_NAME),
                    getString(R.string.MonitoredActionFragment_message_UntitledAction));
            mActionKey = getArguments().getString(ARG_ACTION_KEY);
            mActionOwner = getArguments().getString(ARG_ACTION_OWNER);
        } else {
            mActionName = getString(R.string.MonitoredActionFragment_message_UntitledAction);
            mActionKey = "";
            mActionOwner = "";
        }
    }

    @Override
    protected Class<MonitoredActionViewModel> getViewModelType() {
        return MonitoredActionViewModel.class;
    }

    @Override
    protected Class<ActionStageRecyclerViewAdapter> getAdapterType() {
        return ActionStageRecyclerViewAdapter.class;
    }

    @Override
    protected void setDependencies() {
        super.setDependencies();
        dependencies.add(dataProvider.getMonitoredAction(mActionOwner,
                                                         mActionKey));
        dependencies.add(dataProvider.getAction(mActionOwner, mActionKey));
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_monitored_action;
    }

    @Override
    protected void setEventListeners() {
        super.setEventListeners();
        view.findViewById(R.id.btn_open_action_owner)
                .setOnClickListener(e -> {
                    Bundle args = new Bundle();
                    args.putString(ContactInfoFragment.ARG_CONTACT_KEY, mActionOwner);
                    navigator.navigate(R.id.transition_monitored_action_to_contact_info, args);
        });
        final ListenedEventsController mEvents =
                StagerApplication.getListenedEventsController();

        view.findViewById(R.id.btn_toggle_onAbortedListen).setOnClickListener(e -> {
            String eventName = dataProvider.getActionCompleteAbortedEventName(
                                            mActionOwner, mActionKey);
            boolean listenOnAborted = mEvents.isEventListened(eventName);
            dataProvider.setSubscribe(eventName, !listenOnAborted);
            Toast.makeText(getContext(), !listenOnAborted ? "+" : "-", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_toggle_onSuccessListen).setOnClickListener(e -> {
            String eventName = dataProvider.getActionCompleteSucceedEventName(
                                            mActionOwner, mActionKey);
            boolean listenOnSucceed = mEvents.isEventListened(eventName);
            dataProvider.setSubscribe(eventName, !listenOnSucceed);
            Toast.makeText(getContext(), !listenOnSucceed ? "+" : "-", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        bindData(viewModel.getActionName(), this::updateTitle);
    }

    private void updateTitle(String text) {
        if (getActionBar() != null)
            getActionBar().setTitle(getString(R.string.MonitoredActionFragment_title,
                Utilits.getDefaultOnNullOrBlank(text,
                getString(R.string.MonitoredActionFragment_message_UntitledAction)
            )));
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        updateTitle(mActionName);
    }

    @Override
    protected void setViewModelData() {
        super.setViewModelData();
        viewModel.setActionKey(mActionKey);
        viewModel.setActionOwner(mActionOwner);
    }
}