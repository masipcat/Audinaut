package net.nullsum.audinaut.activity

import android.os.Bundle
import android.util.Log
import android.text.InputType
import androidx.preference.Preference
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import android.content.SharedPreferences
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
            when (preference.getKey()) {
                "appearance" -> return navigateToPreferenceScreen(R.xml.settings_appearance)
                "cache" -> return navigateToPreferenceScreen(R.xml.settings_cache)
                "playback" -> return navigateToPreferenceScreen(R.xml.settings_playback)
                "servers" -> {
                    var fragmentManager = getFragmentManager()
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ServerListFragment())
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

    class ServerListFragment: PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val context = preferenceManager.context
            val sharedPreferences = getPreferenceManager().getSharedPreferences()
            var serverCount = sharedPreferences.getInt(Constants.PREFERENCES_KEY_SERVER_COUNT, 1)

            preferenceScreen = getPreferenceManager().createPreferenceScreen(context)

            var serverPreference: Preference
            if (serverCount > 0) {
                for (instance in 1..serverCount) {
                    serverPreference = Preference(context)
                    serverPreference.key = "$instance"
                    serverPreference.title = sharedPreferences.getString(Constants.PREFERENCES_KEY_SERVER_NAME + instance, "error")
                    preferenceScreen.addPreference(serverPreference)
                }
            }

            serverCount++
            serverPreference = Preference(context)
            serverPreference.key = "$serverCount"
            serverPreference.title = "Add Server"
            preferenceScreen.addPreference(serverPreference)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            when (preference.getKey()) {
                else -> {
                    var fragmentManager = getFragmentManager()
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ServerInstanceFragment(preference.getKey().toInt()))
                                .addToBackStack(null)
                                .commit()
                        return true
                    } else {
                        return false
                    }
                }
            }
        }


    }

    class ServerInstanceFragment(val instance: Int): PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val sharedPreferences = getPreferenceManager().getSharedPreferences()
            var serverCount = sharedPreferences.getInt(Constants.PREFERENCES_KEY_SERVER_COUNT, 1)
            if (instance > serverCount) {
                val editor = sharedPreferences.edit()
                serverCount++
                editor.putInt(Constants.PREFERENCES_KEY_SERVER_COUNT, serverCount)
                editor.apply()
            }

            val context = preferenceManager.context
            val serverNamePreference = EditTextPreference(context)
            serverNamePreference.key = Constants.PREFERENCES_KEY_SERVER_NAME + instance
            serverNamePreference.setDefaultValue(getResources().getString(R.string.settings_server_unused))
            serverNamePreference.setTitle(R.string.settings_server_name)
            serverNamePreference.setDialogTitle(R.string.settings_server_name)

            val serverUrlPreference = EditTextPreference(context)
            serverUrlPreference.key = Constants.PREFERENCES_KEY_SERVER_URL + instance
            //serverUrlPreference.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_URI)
            serverUrlPreference.setDefaultValue("http://yourhost")
            serverUrlPreference.setTitle(R.string.settings_server_address)
            serverUrlPreference.setDialogTitle(R.string.settings_server_address)

            if (serverUrlPreference.getText() == null) {
                serverUrlPreference.setText("http://yourhost")
            }
            serverUrlPreference.setSummary(serverUrlPreference.getText())

            val serverLocalNetworkSSIDPreference = EditTextPreference(context)
            serverLocalNetworkSSIDPreference.key = Constants.PREFERENCES_KEY_SERVER_LOCAL_NETWORK_SSID + instance
            serverLocalNetworkSSIDPreference.setDefaultValue(getResources().getString(R.string.settings_server_unused))
            serverLocalNetworkSSIDPreference.setTitle(R.string.settings_server_local_network_ssid)
            serverLocalNetworkSSIDPreference.setDialogTitle(R.string.settings_server_local_network_ssid)

            val serverInternalUrlPreference: EditTextPreference = EditTextPreference(context)
            serverInternalUrlPreference.key = Constants.PREFERENCES_KEY_SERVER_INTERNAL_URL + instance
            serverInternalUrlPreference.setDefaultValue("")
            serverInternalUrlPreference.setTitle(R.string.settings_server_internal_address)
            serverInternalUrlPreference.setDialogTitle(R.string.settings_server_internal_address)
            serverInternalUrlPreference.setSummary(serverInternalUrlPreference.getText())

            val serverUsernamePreference: EditTextPreference = EditTextPreference(context)
            serverUsernamePreference.key = Constants.PREFERENCES_KEY_USERNAME + instance
            serverUsernamePreference.setTitle(R.string.settings_server_username)
            serverUsernamePreference.setDialogTitle(R.string.settings_server_username)

            val serverPasswordPreference: EditTextPreference = EditTextPreference(context)
            serverPasswordPreference.key = Constants.PREFERENCES_KEY_PASSWORD + instance
            serverPasswordPreference.setSummary("***");
            serverPasswordPreference.setTitle(R.string.settings_server_password);

            val screen = getPreferenceManager().createPreferenceScreen(context)
            screen.setTitle(R.string.settings_server_unused);
            screen.setKey(Constants.PREFERENCES_KEY_SERVER_KEY + instance);
            screen.addPreference(serverNamePreference)
            screen.addPreference(serverUrlPreference)
            screen.addPreference(serverLocalNetworkSSIDPreference)
            screen.addPreference(serverInternalUrlPreference)
            screen.addPreference(serverUsernamePreference)
            screen.addPreference(serverPasswordPreference)
            preferenceScreen = screen
        }
    }
}
