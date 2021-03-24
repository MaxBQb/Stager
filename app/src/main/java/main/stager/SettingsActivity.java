package main.stager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;


public class SettingsActivity extends SmartActivity {
    private static final String _PARENT = "__PARENT_ACTIVITY_CLASS__";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Собираем настройки из фрагментов (пока 1)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        // Добавляем реакции на изменение различных опций
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this::bindOptionChanges);
    }

    private void bindOptionChanges(SharedPreferences prefs, String key) {

        // Смена темы
        if (getString(R.string.Settings__Theme).equals(key)) {
            Boolean dark_theme = prefs.getBoolean(key, false);
            prefs.edit().commit(); // Гарантирия сохранности
            ThemeController.updateTheme();
        }

        // Смена языка
        if (getString(R.string.Settings__Locale).equals(key)) {
            String locale = prefs.getString(key, "en");
            prefs.edit().commit(); // Гарантирия сохранности
            LocaleController.updateLocale();
        }
    }

    /** Открывает окно настроек
     * @param activity куда вернуться
     */
    public static void openSettings(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra(_PARENT, SettingsActivity.class.getName());
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