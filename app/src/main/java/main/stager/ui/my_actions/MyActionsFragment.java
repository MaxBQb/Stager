package main.stager.ui.my_actions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import main.stager.R;

public class MyActionsFragment extends Fragment {

    private MyActionsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MyActionsViewModel.class);
        ActionItemRecyclerViewAdapter adapter = new ActionItemRecyclerViewAdapter();

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.getActions().observe(getViewLifecycleOwner(), adapter::setValues);
        return view;
    }
}