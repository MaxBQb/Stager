package main.stager.ui.add_item.add_action_stage;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import main.stager.list.feature.AddItemFragment;
import main.stager.utils.DataProvider;
import main.stager.R;
import main.stager.model.Stage;
import main.stager.model.Status;
import main.stager.model.TriggerType;

public class AddStageFragment extends AddItemFragment {
    static public final String ARG_ACTION_KEY = "Stager.add_action_stage.param_action_key";
    private String mActionKey;
    private EditText inputName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionKey = getArguments() != null
                   ? getArguments().getString(ARG_ACTION_KEY)
                   : "";
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_add_action_stage;
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        inputName = view.findViewById(R.id.add_action_stage_input_name);
    }

    @Override
    protected void saveChanges() {
        String name = inputName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getContext(),
                    getString(R.string.AddAction_ErrorMessage,
                            getString(R.string.AddAction_ErrorMessage_ReasonNoName)
                    ), Toast.LENGTH_LONG).show();
            return;
        }
        DataProvider.getInstance().addStage(mActionKey,
                new Stage(Status.WAITING, name, TriggerType.MANUAL));
        navigator.navigateUp();
    }
}