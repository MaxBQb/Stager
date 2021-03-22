package main.stager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;


public class LocaleController {

    // Обновление языка на уровне приложения
    public static Context onAttach(Context context) {
        return setLocale(context, getLocale(context));
    }

    // Обновление языка на уровне активности
    public static void restoreLocale(Context context) {
        onAttach(context);
    }

    // Выполняет необходимые для применения языка действия
    public static void updateLocale() {
        Runtime.getRuntime().exit(0);
    }

    // Устанавливает язык
    private static Context setLocale(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return updateResources(context, language);
        return updateResourcesLegacy(context, language);
    }

    // Получить сохранённый язык
    private static String getLocale(Context context) {
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.Settings__Locale), Locale.getDefault().getLanguage());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}