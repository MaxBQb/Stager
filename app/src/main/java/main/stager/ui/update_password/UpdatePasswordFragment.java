package main.stager.ui.update_password;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import main.stager.Base.StagerFragment;
import main.stager.R;

public class UpdatePasswordFragment extends StagerFragment {

    private static final String TAG = "UpdatePassword";
    private EditText edOldPassword;
    private EditText edNewPassword;
    private EditText edNewPasswordConfirm;

    @Override
    protected void setEventListeners() {
        super.setEventListeners();

        LinearLayout layoutPassword = view.findViewById(R.id.layout_OldPassword);
        LinearLayout layoutUpdatePassword = view.findViewById(R.id.layout_UpdatePassword);

        view.findViewById(R.id.btn_NewPassword).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {

                String oldPassword = edOldPassword.getText().toString();

                if (oldPassword.isEmpty() || oldPassword.length() < 6) {
                    edOldPassword.setError("Minimum length of password should be 6");
                    edOldPassword.requestFocus();
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null || user.getEmail() == null)
                    return;

                showLoadingScreen();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), oldPassword);

                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            hideLoadingScreen();
                            if (task.isSuccessful()) {
                                layoutPassword.setVisibility(View.GONE);
                                layoutUpdatePassword.setVisibility(View.VISIBLE);
                            } else {
                                Log.d(TAG, "Error auth failed");
                                edOldPassword.setError("Password isn`t correct");
                                edOldPassword.requestFocus();
                            }
                        });
            }
        });

        view.findViewById(R.id.btn_ChangePassword).setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) return;

            String newPassword = edNewPassword.getText().toString();
            String newPasswordConfirm = edNewPasswordConfirm.getText().toString();

            if (newPassword.isEmpty() || newPassword.length() < 6) {
                edNewPassword.setError("Minimum length of password should be 6");
                edNewPassword.requestFocus();
                return;
            }

            if (!newPassword.equals(newPasswordConfirm)) {
                edNewPassword.setError("Passwords don't match");
                edNewPasswordConfirm.setError("Passwords don't match");
                edNewPasswordConfirm.requestFocus();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user == null)
                return;

            showLoadingScreen();

            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        hideLoadingScreen();
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Password update", Toast.LENGTH_LONG).show();
                            navigator.navigate(R.id.nav_about_me);
                        } else {
                            Log.d(TAG, "Error new password failed");
                            Toast.makeText(getContext(),
                                    "Password not update", Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    @Override
    protected int getViewBaseLayoutId() {
        return R.layout.fragment_update_password;
    }

    @Override
    protected void prepareFragmentComponents() {
        super.prepareFragmentComponents();
        edOldPassword = view.findViewById(R.id.edit_OldPassword);
        edNewPassword = view.findViewById(R.id.edit_text_new_password);
        edNewPasswordConfirm = view.findViewById(R.id.edit_text_new_password_confirm);
    }
}
