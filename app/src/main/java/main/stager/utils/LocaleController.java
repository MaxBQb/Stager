package main.stager.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.yariksoffice.lingver.Lingver;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import main.stager.R;


public class LocaleController {

    /** Обновление языка из сохранённых предпочтнений */
    public static void init(Application context) {
        Lingver.init(context, getLocale(context));
    }

    /** Обновление языка из сохранённых предпочтнений */
    public static void restoreLocale(Context context) {
        setLocale(context, getLocale(context));
    }

    /** Устанавливает язык */
    public static void setLocale(Context context, String language) {
        Lingver.getInstance().setLocale(context, language);
    }

    /** Получить сохранённый язык */
    public static String getLocale(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.Settings__AutoTune),
                        true))
            return getDefaultLocale(context);
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.Settings__Locale), getDefaultLocale(context));
    }

    @NotNull
    public static String getDefaultLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return context.getResources().getConfiguration().locale.getLanguage();
    }
}