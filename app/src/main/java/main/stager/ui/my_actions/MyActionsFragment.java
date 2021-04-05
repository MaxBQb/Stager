package main.stager.ui.my_actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import main.stager.R;

public class MyActionsFragment extends Fragment {

    private MyActionsViewModel myActionsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myActionsViewModel =
                new ViewModelProvider(this).get(MyActionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_actions, container, false);
        final TextView textView = root.findViewById(R.id.text_my_actions);
        myActionsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}