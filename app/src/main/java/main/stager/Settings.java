package main.stager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {
    // SETTINGS name
    public static final String SETTINGS = "__settings__";

    // Themes
    public static final String THEME = "__theme__";
    public static final String THEME_DARK = "__theme_dark__";
    public static final String THEME_LIGHT = "__theme_light__";
    public static final String THEME_DEFAULT = THEME_LIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}