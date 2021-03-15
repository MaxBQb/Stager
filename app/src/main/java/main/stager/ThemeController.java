package main.stager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class ThemeController {

    /** Меняет тему на указанную в настройках
     * (Обычно вызывается в onCreate)
     * @param activity Активность (обычно это this)
     */
    public static void restoreTheme(Activity activity) {

        // Считаем настройки
        String current_theme = Settings.getSettings(activity)
                                       .getString(Settings.THEME, Settings.THEME_DEFAULT);

        // Применим тему
        switch (current_theme) {
            case Settings.THEME_LIGHT:
                activity.setTheme(R.style.Theme_Stager);
                break;

            case Settings.THEME_DARK:
                activity.setTheme(R.style.Theme_AppCompat);
                break;
        }
    }
}
