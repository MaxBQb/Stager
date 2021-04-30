package main.stager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import androidx.preference.PreferenceManager;
import org.jetbrains.annotations.NotNull;
import main.stager.R;

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
        boolean currentTheme = isCurrentDark(activity);
        activity.setTheme(theme(currentTheme, showActionBar));
    }

    public static void restoreTheme(Activity activity) {
        restoreTheme(activity, true);
    }

    /** Возвращает текущую тему
     */
    @NotNull
    public static boolean isCurrentDark(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(context.getString(R.string.Settings__Theme),
                                        isDarkByDefault(context));
    }

    public static boolean isDarkByDefault(@NotNull Context context) {
        int nightModeFlags =
                context.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
