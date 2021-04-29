package main.stager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // Добавляем реакции на изменение различных опций
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this::bindOptionChanges);
        init();
    }

    private void init() {
        findPreference(getString(R.string.Settings__Theme)).setOnPreferenceClickListener(this::enable);
        findPreference(getString(R.string.Settings__Locale)).setOnPreferenceClickListener(this::enable);
    }

    private void bindOptionChanges(SharedPreferences prefs, String key) {
        // Смена темы или языка
        try {
            if (getString(R.string.Settings__Theme).equals(key) ||
                getString(R.string.Settings__Locale).equals(key)) {
                    findPreference(getString(R.string.Settings__Theme)).setOnPreferenceClickListener(this::disable);
                    findPreference(getString(R.string.Settings__Locale)).setOnPreferenceClickListener(this::disable);
                    prefs.edit().commit(); // Гарантирия сохранности
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

    private boolean enable(Preference p) {
        p.setEnabled(true);
        return false;
    }

    private boolean disable(Preference p) {
        p.setEnabled(false);
        return false;
    }
}