package com.example.university_coursework.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.university_coursework.LocaleHelper;
import com.example.university_coursework.MainActivity;
import com.example.university_coursework.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        ListPreference languagePref = findPreference("language");

        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLang = (String) newValue;

                // Сохраняем язык
                requireActivity()
                        .getSharedPreferences("settings", Context.MODE_PRIVATE)
                        .edit()
                        .putString("app_language", selectedLang)
                        .apply();

                // Применяем язык
                LocaleHelper.setAppLocale(requireActivity(), selectedLang);

                // Перезапуск всего приложения через splash или main
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                requireActivity().finish();

                return true;
            });
        }
    }
}
