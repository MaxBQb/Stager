package main.stager.Base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import main.stager.utils.LocaleController;
import main.stager.utils.ThemeController;


/**
 * Activity, но с изменяемыми ресурсами темы и локали
 */
public abstract class SmartActivity extends AppCompatActivity {
    static protected String SHOW_ACTION_BAR = "Stager.SmartActivity.ShowActionBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this, savedInstanceState == null
                || savedInstanceState.getBoolean(SHOW_ACTION_BAR, true));
        LocaleController.restoreLocale(this);
        super.onCreate(savedInstanceState);
    }
}