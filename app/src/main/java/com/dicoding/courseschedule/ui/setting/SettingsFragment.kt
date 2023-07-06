package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        findPreference<ListPreference>(getString(R.string.pref_key_dark))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, theme ->
                theme.apply {
                    val result = this as String
                    val modeSelected = when (result.uppercase(Locale.ROOT)) {
                        NightMode.ON.name -> NightMode.ON.value
                        NightMode.OFF.name -> NightMode.OFF.value
                        else -> NightMode.AUTO.value
                    }
                    updateTheme(modeSelected)
                }
                true
            }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        findPreference<SwitchPreference>(getString(R.string.pref_key_notify))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, isEnable ->
                Log.d("SettingsFragment", "onCreatePreferences: $isEnable")
                when (isEnable) {
                    true -> context?.let { DailyReminder().setDailyReminder(it) }
                    false -> context?.let { DailyReminder().cancelAlarm(it) }
                }

                true
            }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}