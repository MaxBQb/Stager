package main.stager;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AuthorizedOnlyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mAuth.getCurrentUser() == null) return;
    }
}