package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
//    private EditText nickname, email, hash;
//    private FirebaseAuth mAuth;
    private Button btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registr_form);
//        addListenerOnButton();
        init();
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser cUser = mAuth.getCurrentUser();
//        if (cUser != null) {
//            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
//        }
//        else
//            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
//    }

    private void init() {
//        nickname = findViewById(R.id.nickname);
//        email = findViewById(R.id.email);
//        hash = findViewById(R.id.password);
//        mAuth = FirebaseAuth.getInstance();

        btn_sign_in = findViewById(R.id.sign_in);
        btn_sign_in.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, Authorization.class);
                    startActivity(intent);
                }
        );

        //register = findViewById(R.id.success_btn);;
    }

//    public void onClickRegistration(View view) {
//        if (!TextUtils.isEmpty(email.getText().toString()))
//            mAuth.createUserWithEmailAndPassword(email.getText().toString(), hash.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "User SignUp Successful", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                        Toast.makeText(getApplicationContext(), "User SignUp Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.registr_form);
//        init();
//    }
//
//
//    private void init() {
//        nickname = findViewById(R.id.nickname);
//        email = findViewById(R.id.email);
//        hash = findViewById(R.id.password);
//        database = FirebaseDatabase.getInstance().getReference();
//    }
//    public void onClickRegistration(View view) {
//        String id = database.getKey();
//        String name = nickname.getText().toString();
//        String e_mail = email.getText().toString();
//        String hash_pass = hash.getText().toString();
//
//        User user = new User(id, name, e_mail, hash_pass);
//
//        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(e_mail) && !TextUtils.isEmpty(hash_pass)) {
//            database.push().setValue(user);
//            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
//        }
//        else
//            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
//    }
}

