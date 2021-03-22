package main.stager;

import android.app.Activity;
import androidx.preference.PreferenceManager;

public class ThemeController {
    private static int DARK_THEME = R.style.Theme_AppCompat;
    private static int LIGHT_THEME = R.style.Theme_Stager;

    private static int theme(boolean darkTheme) {
        return darkTheme ? DARK_THEME : LIGHT_THEME;
    }

    /** Меняет тему на указанную в настройках
     * (Обычно вызывается в onCreate)
     * @param activity Активность (обычно это this)
     */
    public static Boolean restoreTheme(Activity activity) {
        boolean currentTheme = getTheme(activity);
        activity.setTheme(theme(currentTheme));
        return currentTheme;
    }

    // Возвращает текущую тему
    public static Boolean getTheme(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity)
                                .getBoolean(activity.getString(R.string.Settings__Theme), false);
    }

    // Выполняет необходимые для применения темы действия
    public static void updateTheme() {
        Runtime.getRuntime().exit(0);
    }
}
