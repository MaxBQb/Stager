package main.stager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public abstract class SmartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        LocaleController.restoreLocale(this);
        super.onCreate(savedInstanceState);
    }
}