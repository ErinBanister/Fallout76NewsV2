package com.example.android.fallout76newsv2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            Preference section = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(section);
        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object myPref) {
            String preferenceValue = myPref.toString();
            if (pref instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) pref;
                int prefIndex = listPreference.findIndexOfValue(preferenceValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    pref.setSummary(labels[prefIndex]);
                }
            } else {
                pref.setSummary(preferenceValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences allPrefs =
                    PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String preferenceString = allPrefs.getString(pref.getKey(), "");
            onPreferenceChange(pref, preferenceString);
        }
    }
}