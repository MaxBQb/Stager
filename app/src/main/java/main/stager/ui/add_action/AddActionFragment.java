package main.stager.ui.add_action;

import android.widget.EditText;
import android.widget.Toast;
import main.stager.AddItemFragment;
import main.stager.DataProvider;
import main.stager.R;
import main.stager.model.Status;
import main.stager.model.UserAction;

public class AddActionFragment extends AddItemFragment {

    private EditText inputName;

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_add_action;
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        inputName = view.findViewById(R.id.add_action_input_name);
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
        DataProvider.getInstance().addAction(new UserAction(Status.WAITING, name));
        navigator.navigateUp();
    }
}