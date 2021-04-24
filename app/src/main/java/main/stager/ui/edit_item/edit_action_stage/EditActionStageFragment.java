package main.stager.ui.edit_item.edit_action_stage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import main.stager.Base.SmartActivity;
import main.stager.Base.StagerVMFragment;
import main.stager.R;
import main.stager.utils.Utilits;

public class EditActionStageFragment extends StagerVMFragment<EditActionStageViewModel> {
    static public final String ARG_STAGE_NAME = "Stager.edit_item.stage.param_stage_name";
    static public final String ARG_ACTION_KEY = "Stager.edit_item.stage.param_action_key";
    static public final String ARG_STAGE_KEY = "Stager.edit_item.stage.param_stage_key";
    private String mStageName;
    private String mActionKey;
    private String mStageKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStageName = Utilits.getDefaultOnNullOrBlank(getArguments().getString(ARG_STAGE_NAME),
                    getString(R.string.EditActionStage_message_UntitledStage));
            mActionKey = getArguments().getString(ARG_ACTION_KEY);
            mStageKey = getArguments().getString(ARG_STAGE_KEY);
        } else {
            mStageName = getString(R.string.EditActionStage_message_UntitledStage);
            mActionKey = "";
            mStageKey = "";
        }
    }

    @Override
    protected Class<EditActionStageViewModel> getViewModelType() {
        return EditActionStageViewModel.class;
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_edit_action_stage;
    }

    @Override
    protected void setObservers() {
        super.setObservers();
        bindData(viewModel.getStageName(),
                (String text) -> ((AppCompatActivity)getActivity())
                        .getSupportActionBar()
                        .setTitle(Utilits.getDefaultOnNullOrBlank(text,
                                        getString(R.string.EditActionStage_message_UntitledStage
                                        ))));
        bindDataTwoWay(viewModel.getStageName(),
                view.findViewById(R.id.edit_action_stage_input_name));
    }

    @Override
    protected void setDependencies() {
        super.setDependencies();
        dependencies.add(dataProvider.getAction(mActionKey));
        dependencies.add(dataProvider.getStage(mActionKey, mStageKey));
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        ((SmartActivity)getActivity())
                .getSupportActionBar()
                .setTitle(mStageName);
    }

    @Override
    protected void setViewModelData() {
        super.setViewModelData();
        viewModel.setActionKey(mActionKey);
        viewModel.setStageKey(mStageKey);
    }
}