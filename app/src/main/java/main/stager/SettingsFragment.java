package main.stager;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // Добавляем реакции на изменение различных опций
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this::bindOptionChanges);
    }

    private void bindOptionChanges(SharedPreferences prefs, String key) {
        // Смена темы или языка
        try {
            if (getString(R.string.Settings__Theme).equals(key) ||
                getString(R.string.Settings__Locale).equals(key)) {
                    prefs.edit().commit(); // Гарантирия сохранности
                    getActivity().recreate();
            }
        } catch (Throwable ignore) {}
    }
}