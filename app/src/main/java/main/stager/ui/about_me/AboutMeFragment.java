package main.stager.ui.about_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import main.stager.MainActivity;
import main.stager.R;

public class AboutMeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_me, container, false);
        root.findViewById(R.id.btn_exit_in_acc).setOnClickListener(
                v -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this.getActivity(), MainActivity.class));
                }
        );
        return root;
    }
}