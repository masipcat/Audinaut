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
import net.nullsum.audinaut.util.Util

class SettingsActivity : SubsonicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
    }

    class SettingsFragment: PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            when (preference.getKey()) {
                "appearance" -> return navigateToFragment(AppearanceFragment())
                "cache" -> return navigateToFragment(CacheFragment())
                "playback" -> return navigateToFragment(PlaybackFragment())
                "servers" -> return navigateToFragment(ServerListFragment())
                else -> return super.onPreferenceTreeClick(preference)
            }
        }

        fun navigateToFragment(fragment: PreferenceFragmentCompat): Boolean {
            var fragmentManager = getFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                return true
            } else {
                return false
            }
        }
    }

    class AppearanceFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_appearance)
        }
    }

    class PlaybackFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_playback)
        }
    }

    class CacheFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_cache)
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

        lateinit var serverNamePreference: EditTextPreference
        lateinit var serverUrlPreference: EditTextPreference
        lateinit var serverLocalNetworkSSIDPreference: EditTextPreference
        lateinit var serverInternalUrlPreference: EditTextPreference
        lateinit var serverUsernamePreference: EditTextPreference
        lateinit var serverPasswordPreference: EditTextPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val sharedPreferences = getPreferenceManager().getSharedPreferences()
            var serverCount = sharedPreferences.getInt(Constants.PREFERENCES_KEY_SERVER_COUNT, 1)
            val editor = sharedPreferences.edit()
            if (instance > serverCount) {
                serverCount++
                editor.putInt(Constants.PREFERENCES_KEY_SERVER_COUNT, serverCount)
                editor.apply()
            }

            val context = preferenceManager.context

            serverNamePreference = EditTextPreference(context)
            serverNamePreference.key = Constants.PREFERENCES_KEY_SERVER_NAME + instance
            serverNamePreference.setDefaultValue(getResources().getString(R.string.settings_server_unused))
            serverNamePreference.setTitle(R.string.settings_server_name)
            serverNamePreference.setDialogTitle(R.string.settings_server_name)

            serverUrlPreference = EditTextPreference(context)
            serverUrlPreference.key = Constants.PREFERENCES_KEY_SERVER_URL + instance
            //serverUrlPreference.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_URI)
            serverUrlPreference.setDefaultValue("http://yourhost")
            serverUrlPreference.setTitle(R.string.settings_server_address)
            serverUrlPreference.setDialogTitle(R.string.settings_server_address)

            serverLocalNetworkSSIDPreference = EditTextPreference(context)
            serverLocalNetworkSSIDPreference.key = Constants.PREFERENCES_KEY_SERVER_LOCAL_NETWORK_SSID + instance
            serverLocalNetworkSSIDPreference.setDefaultValue(getResources().getString(R.string.settings_server_unused))
            serverLocalNetworkSSIDPreference.setTitle(R.string.settings_server_local_network_ssid)
            serverLocalNetworkSSIDPreference.setDialogTitle(R.string.settings_server_local_network_ssid)

            serverInternalUrlPreference = EditTextPreference(context)
            serverInternalUrlPreference.key = Constants.PREFERENCES_KEY_SERVER_INTERNAL_URL + instance
            serverInternalUrlPreference.setDefaultValue("")
            serverInternalUrlPreference.setTitle(R.string.settings_server_internal_address)
            serverInternalUrlPreference.setDialogTitle(R.string.settings_server_internal_address)

            serverUsernamePreference = EditTextPreference(context)
            serverUsernamePreference.key = Constants.PREFERENCES_KEY_USERNAME + instance
            serverUsernamePreference.setTitle(R.string.settings_server_username)
            serverUsernamePreference.setDialogTitle(R.string.settings_server_username)

            serverPasswordPreference = EditTextPreference(context)
            serverPasswordPreference.key = Constants.PREFERENCES_KEY_PASSWORD + instance
            serverPasswordPreference.setTitle(R.string.settings_server_password);

            val serverRemoveServerPreference = Preference(context)
            serverRemoveServerPreference.key = Constants.PREFERENCES_KEY_SERVER_REMOVE + instance
            serverRemoveServerPreference.setPersistent(false)
            serverRemoveServerPreference.setTitle(R.string.settings_servers_remove)
            serverRemoveServerPreference.setOnPreferenceClickListener(
                object : Preference.OnPreferenceClickListener {
                    override fun onPreferenceClick(preference: Preference): Boolean {
                        editor.putInt(Constants.PREFERENCES_KEY_SERVER_COUNT, serverCount - 1)
                        editor.remove(Constants.PREFERENCES_KEY_SERVER_INTERNAL_URL + instance)
                        editor.remove(Constants.PREFERENCES_KEY_SERVER_LOCAL_NETWORK_SSID + instance)
                        editor.remove(Constants.PREFERENCES_KEY_USERNAME + instance)
                        editor.remove(Constants.PREFERENCES_KEY_PASSWORD + instance)
                        editor.remove(Constants.PREFERENCES_KEY_SERVER_URL + instance)
                        editor.remove(Constants.PREFERENCES_KEY_SERVER_NAME + instance)
                        editor.apply()
                        /*
                        val activeServer = sharedPreferences.getInt(Constants.PREFERENCES_KEY_SERVER_INSTANCE, 1)
                        for (i in 1..serverCount) {
                            Util.removeInstanceName(context, i, activeServer)
                        }
                        */
                        return true
                    }
                }
            )
            /*
            serverRemoveServerPreference.setOnPreferenceClickListener(preference -> {
                Util.confirmDialog(context, R.string.common_delete, screen.getTitle().toString(), (dialog, which) -> {
                    // Don't use Util.getActiveServer since it is 0 if offline
                    int activeServer = Util.getPreferences(context).getInt(Constants.PREFERENCES_KEY_SERVER_INSTANCE, 1);
                    for (int i = instance; i <= serverCount; i++) {
                        Util.removeInstanceName(context, i, activeServer);
                    }

                    serverCount--;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(Constants.PREFERENCES_KEY_SERVER_COUNT, serverCount);
                    editor.apply();

                    removeCurrent();

                    SubsonicFragment parentFragment = context.getCurrentFragment();
                    if (parentFragment instanceof SettingsFragment) {
                        SettingsFragment serverSelectionFragment = (SettingsFragment) parentFragment;
                        serverSelectionFragment.checkForRemoved();
                    }
                });

                return true;
            });
            */

            val screen = getPreferenceManager().createPreferenceScreen(context)
            screen.setTitle(R.string.settings_server_unused);
            screen.setKey(Constants.PREFERENCES_KEY_SERVER_KEY + instance);
            screen.addPreference(serverNamePreference)
            screen.addPreference(serverUrlPreference)
            screen.addPreference(serverLocalNetworkSSIDPreference)
            screen.addPreference(serverInternalUrlPreference)
            screen.addPreference(serverUsernamePreference)
            screen.addPreference(serverPasswordPreference)
            screen.addPreference(serverRemoveServerPreference)
            preferenceScreen = screen

            sharedPreferences.registerOnSharedPreferenceChangeListener(
                object : SharedPreferences.OnSharedPreferenceChangeListener {
                    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
                        updateSummaries()
                    }
                }
            )

            updateSummaries()
        }

        fun updateSummaries() {
            serverNamePreference.summary = serverNamePreference.text
            serverUrlPreference.summary = serverUrlPreference.text
            serverLocalNetworkSSIDPreference.summary = serverLocalNetworkSSIDPreference.text
            serverInternalUrlPreference.summary = serverInternalUrlPreference.text
            serverUsernamePreference.summary = serverUsernamePreference.text
            serverPasswordPreference.summary = "***"
        }
    }
}
