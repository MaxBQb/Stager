package main.stager;

import android.app.Activity;
import androidx.preference.PreferenceManager;

public class ThemeController {
    private static int DARK_THEME = R.style.Theme_AppCompat;
    private static int DARK_THEME_NO_ACTIONBAR = R.style.Theme_AppCompat_NoActionBar;
    private static int LIGHT_THEME = R.style.Theme_Stager_Light_DarkActionBar;
    private static int LIGHT_THEME_NO_ACTIONBAR = R.style.Theme_Stager_Light_NoActionBar;

    private static int theme(boolean darkTheme, boolean showActionBar) {
        return showActionBar ? (darkTheme ? DARK_THEME : LIGHT_THEME) :
                (darkTheme ? DARK_THEME_NO_ACTIONBAR : LIGHT_THEME_NO_ACTIONBAR);
    }

    /** Меняет тему на указанную в настройках
     * @param activity Активность (обычно это this)
     */
    public static void restoreTheme(Activity activity, boolean showActionBar) {
        boolean currentTheme = getTheme(activity);
        activity.setTheme(theme(currentTheme, showActionBar));
    }

    public static void restoreTheme(Activity activity) {
        restoreTheme(activity, true);
    }

    /** Возвращает текущую тему
     */
    public static Boolean getTheme(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity)
                                .getBoolean(activity.getString(R.string.Settings__Theme), false);
    }

}
