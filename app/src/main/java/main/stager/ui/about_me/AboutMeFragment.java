package main.stager.ui.about_me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;

import main.stager.DataProvider;
import main.stager.MainActivity;
import main.stager.R;

public class AboutMeFragment extends Fragment {
    private AboutMeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AboutMeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about_me, container, false);
        root.findViewById(R.id.btn_exit_in_acc).setOnClickListener(
                v -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this.getActivity(), MainActivity.class));
                }
        );
        EditText input_name = root.findViewById(R.id.personName);
        EditText input_description = root.findViewById(R.id.secondName);
        viewModel.getText().observe(getViewLifecycleOwner(), input_name::setText);
        viewModel.getDescription().observe(getViewLifecycleOwner(), input_description::setText);

        input_name.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (hasFocus) return;
            DataProvider.getInstance().getUserName()
                    .setValue(((EditText)v).getText().toString());
        });

        input_description.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (hasFocus) return;
            DataProvider.getInstance().getUserDescription()
                    .setValue(((EditText)v).getText().toString());
        });

        return root;
    }
}