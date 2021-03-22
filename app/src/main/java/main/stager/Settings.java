package main.stager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


public class Settings extends AppCompatActivity {
    private static final String _PARENT = "__PARENT_ACTIVITY_CLASS__";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this::bindOptionChanges);
    }

    private void bindOptionChanges(SharedPreferences prefs, String key) {
        if (getString(R.string.Settings__Theme).equals(key)) {
            Boolean dark_theme = prefs.getBoolean(key, false);
            prefs.edit().commit(); // Гарантирия сохранности
            ThemeController.updateTheme();
        }
    }

    /** Открывает окно настроек
     * @param activity куда вернуться
     */
    public static void openSettings(Activity activity) {
        Intent intent = new Intent(activity, Settings.class);
        intent.putExtra(_PARENT, Settings.class.getName());
        activity.startActivity(intent);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return _getParentActivityIntent();
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        // Требуется для работы на старых версиях Android
        return _getParentActivityIntent();
    }

    private Intent _getParentActivityIntent() {
        // Получение активности, вызвавшей данную
        Intent intent = null;
        try {
            intent = new Intent(this,
                    Class.forName(getIntent()
                                 .getExtras()
                                 .getString(_PARENT))
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return intent;
    }
}