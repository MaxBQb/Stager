package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AuthorizedOnlyActivity {
    private Button btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogOut = (Button) findViewById(R.id.btn_log_out);

        btnLogOut.setOnClickListener(
                v -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(this, Authorization.class);
                    startActivity(intent);
                }
        );
    }
}