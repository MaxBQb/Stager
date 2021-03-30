package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AuthorizedOnlyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mAuth.getCurrentUser() == null) return;

        ((Button) findViewById(R.id.btn_log_out)).setOnClickListener(
                v -> {
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(this, Authorization.class));
                }
        );
    }
}