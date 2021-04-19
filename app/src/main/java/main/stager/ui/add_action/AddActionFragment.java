package main.stager.ui.add_action;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import main.stager.DataProvider;
import main.stager.R;
import main.stager.StagerFragment;
import main.stager.model.Status;
import main.stager.model.UserAction;

public class AddActionFragment extends StagerFragment {

    private EditText inputName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_add_action;
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.ic_close);
        inputName = view.findViewById(R.id.add_action_input_name);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.item_edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_settings).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_changes) {
            save_changes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save_changes() {
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