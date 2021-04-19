package main.stager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import main.stager.Base.SmartActivity;

public class Authorization extends SmartActivity {

    private EditText edEmail, edPassword;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_form);

        // Устанавливает название активности, вместо названия приложения
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.RegistrationActivity_Button_SignIn);

        init();
    }

    private void init() {
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);

        // Переход на регистрацию
        findViewById(R.id.btn_registration).setOnClickListener(v -> {
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
        });
        findViewById(R.id.success_btn).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) signIn();
        });
    }

    private void signIn() {

        // Вход в аккаунт
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

        // Проверка на интернет-соединение
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(),
                    "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        // Пустое поле email
        if (email.isEmpty()) {
            edEmail.setError("Email is required");
            edEmail.requestFocus();
            return;
        }

        // Некорректный ввод логина
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Please enter a valid email");
            edEmail.requestFocus();
            return;
        }

        // Пустое поле пароля
        if (password.isEmpty()) {
            edPassword.setError("Password is required");
            edPassword.requestFocus();
            return;
        }

        // Некорректная длина пароля
        if (password.length() < 6) {
            edPassword.setError("Minimum length of password should be 6");
            edPassword.requestFocus();
            return;
        }

        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else if (task.getException() != null) // Получение ошибок при входе
                Toast.makeText(getApplicationContext(),
                        task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            });
    }

    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
        return cm.getActiveNetworkInfo() != null;
    }
}
