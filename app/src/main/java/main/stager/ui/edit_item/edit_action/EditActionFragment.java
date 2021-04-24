package main.stager.ui.edit_item.edit_action;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import main.stager.R;
import main.stager.Base.SmartActivity;
import main.stager.list.StagerExtendableList;
import main.stager.model.Stage;
import main.stager.ui.add_item.add_action_stage.AddStageFragment;
import main.stager.utils.Utilits;

public class EditActionFragment
        extends StagerExtendableList<EditActionViewModel, ActionStageRecyclerViewAdapter, Stage> {
    static public final String ARG_ACTION_NAME = "Stager.stages_list.param_action_name";
    static public final String ARG_ACTION_KEY = "Stager.stages_list.param_action_key";
    private String mActionName;
    private String mActionKey;
    private EditText editActionName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mActionName = Utilits.getDefaultOnNullOrBlank(getArguments().getString(ARG_ACTION_NAME),
                    getString(R.string.EditActionFragment_message_UntitledAction));
            mActionKey = getArguments().getString(ARG_ACTION_KEY);
        } else {
            mActionName = getString(R.string.EditActionFragment_message_UntitledAction);
            mActionKey = "";
        }
    }

    @Override
    protected Class<EditActionViewModel> getViewModelType() {
        return EditActionViewModel.class;
    }

    @Override
    protected Class<ActionStageRecyclerViewAdapter> getAdapterType() {
        return ActionStageRecyclerViewAdapter.class;
    }

    @Override
    protected void setDependencies() {
        super.setDependencies();
        dependencies.add(dataProvider.getAction(mActionKey));
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_action_stages;
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        bindData(viewModel.getActionName(),
                (String text) -> ((AppCompatActivity)getActivity())
                                 .getSupportActionBar()
                                 .setTitle(getString(R.string.StagesFragment_label,
                                         Utilits.getDefaultOnNullOrBlank(text,
                                                 getString(R.string
                                                         .EditActionFragment_message_UntitledAction
                                                 )))));
        bindDataTwoWay(viewModel.getActionName(), editActionName);
    }

    @Override
    protected void onButtonAddClicked(View v) {
        Bundle args = new Bundle();
        args.putString(AddStageFragment.ARG_ACTION_KEY, mActionKey);
        navigator.navigate(R.id.transition_action_stages_to_add_stage, args);
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        ((SmartActivity)getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.StagesFragment_label, mActionName));
        editActionName = view.findViewById(R.id.edit_action_input_name);
        editActionName.setText(mActionName);
    }

    @Override
    protected void setViewModelData() {
        super.setViewModelData();
        viewModel.setActionKey(mActionKey);
    }
}