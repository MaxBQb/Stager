package main.stager.ui.about_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import main.stager.R;

public class AboutMeFragment extends Fragment {

    private AboutMeViewModel aboutMeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutMeViewModel =
                new ViewModelProvider(this).get(AboutMeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about_me, container, false);
        final TextView textView = root.findViewById(R.id.text_about_me);
        aboutMeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}