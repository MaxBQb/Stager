package main.stager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Activity, но с изменяемыми ресурсами темы и локали
 */
public abstract class SmartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        LocaleController.restoreLocale(this);
        super.onCreate(savedInstanceState);
    }
}