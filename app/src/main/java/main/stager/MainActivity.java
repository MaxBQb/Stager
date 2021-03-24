package main.stager;

import android.os.Bundle;

public class MainActivity extends AuthorizedOnlyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}