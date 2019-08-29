package net.nullsum.audinaut.activity

import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.nullsum.audinaut.R
import net.nullsum.audinaut.util.Constants

class SettingsActivity : SubsonicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment(R.xml.settings))
                .commit()
    }

    class SettingsFragment(val resourceId: Int): PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(resourceId)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            Log.d("fucking server", preference.getKey())
            when (preference.getKey()) {
                "appearance" -> return navigateToPreferenceScreen(R.xml.settings_appearance)
                "cache" -> return navigateToPreferenceScreen(R.xml.settings_cache)
                "playback" -> return navigateToPreferenceScreen(R.xml.settings_playback)
                "servers" -> {
                    var fragmentManager = getFragmentManager()
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ServerSettingsFragment())
                                .addToBackStack(null)
                                .commit()
                        return true
                    } else {
                        return false
                    }
                }
                else -> {
                    return super.onPreferenceTreeClick(preference)
                }
            }
        }

        fun navigateToPreferenceScreen(resourceId: Int): Boolean {
            var fragmentManager = getFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment(resourceId))
                        .addToBackStack(null)
                        .commit()
                return true
            } else {
                return false
            }
        }
    }

    class ServerSettingsFragment: PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_servers)
        }

    }
}
