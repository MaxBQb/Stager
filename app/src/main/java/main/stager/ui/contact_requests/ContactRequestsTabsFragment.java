package main.stager.ui.contact_requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import main.stager.R;

public class ContactRequestsTabsFragment extends Fragment {
    ContactRequestsTabsAdapter contactRequestsTabsAdapter;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_requests_tabs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        contactRequestsTabsAdapter = new ContactRequestsTabsAdapter(
                getChildFragmentManager(), getContext());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(contactRequestsTabsAdapter);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

