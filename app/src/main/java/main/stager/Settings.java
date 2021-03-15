package main.stager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {

    // Themes
    public static final String THEME = "__theme__";
    public static final String THEME_DARK = "__theme_dark__";
    public static final String THEME_LIGHT = "__theme_light__";
    public static final String THEME_DEFAULT = THEME_LIGHT;

    // Hidden
    private static final String _SETTINGS = "__settings__";
    private static final String _PARENT = "__PARENT_ACTIVITY_CLASS__";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeController.restoreTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static SharedPreferences getSettings(Activity activity) {
        return activity.getSharedPreferences(Settings._SETTINGS, 0);
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