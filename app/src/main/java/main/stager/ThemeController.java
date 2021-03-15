package main.stager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
        return Settings.getSettings(activity)
                       .getBoolean(Settings.THEME, false);
    }

    // Устанавливает текущую тему
    public static void setTheme(Activity activity, Boolean darkTheme) {
        Settings.setSettings(activity)
                .putBoolean(Settings.THEME, darkTheme)
                .commit();
        activity.navigateUpTo(new Intent(activity, MainActivity.class));
    }
}
