package main.stager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import main.stager.utils.LocaleController;
import main.stager.utils.ThemeController;

public class SettingsFragment extends PreferenceFragmentCompat
implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String S_AUTO_TUNE;
    private String S_LOCALE;
    private String S_THEME;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        defineSettingsNames();
        autoTune();

        // Добавляем реакции на изменение различных опций
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    private void defineSettingsNames() {
        S_AUTO_TUNE = getString(R.string.Settings__AutoTune);
        S_LOCALE = getString(R.string.Settings__Locale);
        S_THEME = getString(R.string.Settings__Theme);
    }

    private void autoTune() {
        if (!PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(S_AUTO_TUNE, true)) return;

        SwitchPreferenceCompat theme = findPreference(S_THEME);
        theme.setChecked(ThemeController.isDarkByDefault(getContext()));

        ListPreference language = findPreference(S_LOCALE);
        language.setValue(LocaleController.getDefaultLocale(getContext()));
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // Смена темы или языка
        try {
            if (S_THEME.equals(key) ||
                S_AUTO_TUNE.equals(key) ||
                S_LOCALE.equals(key)) {

                prefs.edit().commit(); // Гарантирия сохранности
                if (S_AUTO_TUNE.equals(key))
                    ((StagerApplication)getActivity().getApplication()).restart(getActivity());
                else
                    getActivity().recreate();
            }
        } catch (Throwable ignore) {}
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_settings).setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}