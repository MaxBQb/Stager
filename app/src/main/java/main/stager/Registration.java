package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Registration extends AppCompatActivity {
    private EditText edNickname, edEmail, edPassword, edPasswordCheck;
    private FirebaseAuth fbAuth;
    private Button btnSignIn, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registr_form);
        init();
    }

    private void init() {
        edNickname = findViewById(R.id.nickname);
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        edPasswordCheck = findViewById(R.id.check_password);

        fbAuth = FirebaseAuth.getInstance();

        onClickRegistration();
        onClickSignIn();
    }

    private void onClickRegistration() {
        btnContinue = (Button) findViewById(R.id.success_btn);
//        String id = fbAuth.getAccessToken();

        btnContinue.setOnClickListener(
                v -> {
                    String id = fbAuth.getUid();
                    String name = edNickname.getText().toString();
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();
                    String passwordCheck = edPasswordCheck.getText().toString();

                    if (password.equals(passwordCheck) && !name.isEmpty() && !email.isEmpty()) {
//                        User user = new User(id, name, email, password);
//                        fbAuth.sendSignInLinkToEmail("Forget the password", );
                        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(this, "IF ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickSignIn() {
        btnSignIn = (Button) findViewById(R.id.sign_in);
        btnSignIn.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, Authorization.class);
                    startActivity(intent);
                }
        );
    }
}
