package main.stager.utils;

import android.app.Application;
import android.content.Context;
import com.yariksoffice.lingver.Lingver;
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
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.Settings__Locale), getDefaultLocale(context));
    }

    public static String getDefaultLocale(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }
}