package main.stager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import main.stager.R;

public class SettingsWrapper {
    private static SettingsWrapper instance;
    private SharedPreferences pref;

    public final String AUTO_TUNE;
    public final String LOCALE;
    public final String THEME;

    public static synchronized void init(Context context) {
        if (instance != null)
            throw new RuntimeException("SettingsWrapper already exists");
        instance = new SettingsWrapper(context);
    }

    public static SettingsWrapper getInstance() {
        if (instance == null)
            throw new RuntimeException("SettingsWrapper not init yet");
        return instance;
    }

    private SettingsWrapper(Context c) {
        pref = PreferenceManager.getDefaultSharedPreferences(c);
        AUTO_TUNE = c.getString(R.string.Settings__AutoTune);
        LOCALE = c.getString(R.string.Settings__Locale);
        THEME = c.getString(R.string.Settings__Theme);
    }

    public boolean isAutoTune(boolean unset) {
        return pref.getBoolean(AUTO_TUNE, unset);
    }

    public boolean isDarkTheme(boolean unset) {
        return pref.getBoolean(THEME, unset);
    }

    public String getLocale(String unset) {
        return pref.getString(LOCALE, unset);
    }
}
