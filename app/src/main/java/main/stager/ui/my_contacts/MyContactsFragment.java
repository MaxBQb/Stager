package main.stager.ui.my_contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import main.stager.R;

public class MyContactsFragment extends Fragment {

    private MyContactsViewModel myContactsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myContactsViewModel =
                new ViewModelProvider(this).get(MyContactsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_contacts, container, false);
        final TextView textView = root.findViewById(R.id.text_my_contacts);
        myContactsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}