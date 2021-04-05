package main.stager;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Исользуется, как альтернативный вариант отображения списка,
 * в том случе, когда список пуст.
 */
public class NoItemsFillerFragment extends Fragment {

    private static final String ARG_MESSAGE = "Stager.no_items_filler.param_message";
    private String mMessage;

    public NoItemsFillerFragment() {
        // Required empty public constructor
    }

    public static NoItemsFillerFragment newInstance(String message) {
        NoItemsFillerFragment fragment = new NoItemsFillerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mMessage = getArguments().getString(ARG_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_items_filler, container, false);
        ((TextView)view.findViewById(R.id.no_items_fragment_message)).setText(mMessage);
        return view;
    }
}