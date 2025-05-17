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
        // Указываем имя общего файла настроек
        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        // Загружаем из settings.xml
        setPreferencesFromResource(R.xml.settings, rootKey);

        ListPreference ringtonePref = findPreference("notification_ringtone");
        if (ringtonePref != null) {
            ringtonePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        }

        ListPreference volumePref = findPreference("notification_volume");
        if (volumePref != null) {
            volumePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        }

        ListPreference languagePref = findPreference("language");
        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLang = (String) newValue;

                // Сохраняем и применяем язык
                requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
                        .edit().putString("app_language", selectedLang).apply();

                LocaleHelper.setAppLocale(requireActivity(), selectedLang);

                // Перезапуск
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();

                return true;
            });
        }
    }
}

