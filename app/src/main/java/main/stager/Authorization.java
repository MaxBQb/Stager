package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authorization extends SmartActivity {
    private EditText edEmail, edPassword;
    private FirebaseAuth fbAuth;
    private Button btnRegistration, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_form);

        // App label set static
        // But main activity label must be set dynamically
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.btn_sign_in);

        init();
    }

    private void init() {
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);

        onClickRegistration();
        onClickSignIn();
    }

    public void onClickRegistration() {
        btnRegistration = (Button) findViewById(R.id.btn_registration);

        btnRegistration.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, Registration.class);
                    startActivity(intent);
                }
        );
    }

    private void onClickSignIn() {
        btnContinue = (Button) findViewById(R.id.success_btn);

        btnContinue.setOnClickListener(
                v -> {
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();

                    if (!email.isEmpty() && !password.isEmpty()) {
                        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "SIGN IN ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }
        );
    }

}
