package main.stager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import main.stager.R;

public class SettingsWrapper {
    private SharedPreferences pref;

    public final String AUTO_TUNE;
    public final String LOCALE;
    public final String THEME;
    public final String HIDE_EMAIL;
    public final String NOTIFY_FRIENDSHIP_REQUEST;
    public final String NOTIFY_FRIENDSHIP_REQUEST_ACCEPTED;

    public SettingsWrapper(Context c) {
        pref = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
        AUTO_TUNE = c.getString(R.string.Settings__AutoTune);
        LOCALE = c.getString(R.string.Settings__Locale);
        THEME = c.getString(R.string.Settings__Theme);
        HIDE_EMAIL = c.getString(R.string.Settings__HideEmail);
        NOTIFY_FRIENDSHIP_REQUEST = c.getString(R.string.Settings__NotifyFriendshipRequest);
        NOTIFY_FRIENDSHIP_REQUEST_ACCEPTED = c.getString(R.string.Settings__NotifyFriendshipRequestAccepted);
    }

    public boolean isAutoTune(boolean unset) {
        return pref.getBoolean(AUTO_TUNE, unset);
    }

    public boolean isDarkTheme(boolean unset) {
        return pref.getBoolean(THEME, unset);
    }

    public boolean isEmailHidden(boolean unset) {
        return pref.getBoolean(HIDE_EMAIL, unset);
    }

    public boolean isNotifyFriendshipRequestAllowed(boolean unset) {
        return pref.getBoolean(NOTIFY_FRIENDSHIP_REQUEST, unset);
    }

    public boolean isNotifyFriendshipRequestAcceptedAllowed(boolean unset) {
        return pref.getBoolean(NOTIFY_FRIENDSHIP_REQUEST_ACCEPTED, unset);
    }

    public String getLocale(String unset) {
        return pref.getString(LOCALE, unset);
    }
}
