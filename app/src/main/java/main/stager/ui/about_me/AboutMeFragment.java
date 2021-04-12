package main.stager.ui.about_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import main.stager.MainActivity;
import main.stager.R;

public class AboutMeFragment extends Fragment {

    private FirebaseAuth fbAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about_me, container, false);
        root.findViewById(R.id.textView);
        ((Button) root.findViewById(R.id.btn_exit_in_acc)).setOnClickListener(
                v -> {
                    fbAuth = FirebaseAuth.getInstance();
                    startActivity(new Intent(this.getActivity(), MainActivity.class));
                    fbAuth.signOut();

                }
        );

        return root;
    }
}