package main.stager;

import android.app.Activity;

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
    public static void restoreTheme(Activity activity) {
        activity.setTheme(theme(getTheme(activity)));
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
                .apply();
        activity.setTheme(theme(darkTheme));
        activity.recreate();
    }
}
