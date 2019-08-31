package net.nullsum.audinaut.activity

import android.os.Bundle
import android.util.Log
import android.text.InputType
import androidx.preference.Preference
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceScreen
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
            val preferenceCategory = findPreference(Constants.PREFERENCES_KEY_SERVER_KEY)
            val activity = getActivity()
            val preferences = Util.getPreferences(activity)
            val serverCount = preferences.getInt(Constants.PREFERENCES_KEY_SERVER_COUNT, 1)

            for (i in 1..serverCount) {
                Log.d("fuck", "$i")
            }

            setPreferenceScreen(expandServerScreen(1))
        }

        fun expandServerScreen(id: Int): PreferenceScreen {
            val activity = getActivity()

            val serverNamePreference = EditTextPreference(activity)
            serverNamePreference.setKey(Constants.PREFERENCES_KEY_SERVER_NAME + id)
            serverNamePreference.setDefaultValue(getResources().getString(R.string.settings_server_unused))
            serverNamePreference.setTitle(R.string.settings_server_name)
            serverNamePreference.setDialogTitle(R.string.settings_server_name)

            val serverUrlPreference = EditTextPreference(activity)
            serverUrlPreference.setKey(Constants.PREFERENCES_KEY_SERVER_URL + id)
            //serverUrlPreference.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_URI)
            serverUrlPreference.setDefaultValue("http://yourhost")
            serverUrlPreference.setTitle(R.string.settings_server_address)
            serverUrlPreference.setDialogTitle(R.string.settings_server_address)

            if (serverUrlPreference.getText() == null) {
                serverUrlPreference.setText("http://yourhost")
            }

            serverUrlPreference.setSummary(serverUrlPreference.getText())

            val screen = getPreferenceManager().createPreferenceScreen(activity)
            screen.setTitle(R.string.settings_server_unused);
            screen.setKey(Constants.PREFERENCES_KEY_SERVER_KEY + id);
            screen.addPreference(serverNamePreference)
            screen.addPreference(serverUrlPreference)
            return screen
        }
//            preferenceCategory.addPreference(serverNamePreference)
            /*

            if (serverNamePreference.getText() == null) {
                serverNamePreference.setText(getResources().getString(R.string.settings_server_unused));
            }

        serverNamePreference.setSummary(serverNamePreference.getText());
            */
            //addPreferencesFromResource(R.xml.settings_servers)

        /*
        private fun addServer: PreferenceScreen(instance: Int) {
            final PreferenceScreen screen = this.getPreferenceManager().createPreferenceScreen(context);
            screen.setKey(Constants.PREFERENCES_KEY_SERVER_KEY + instance);
            screen.setOrder(instance);

            screen.setOnPreferenceClickListener(preference -> {
                SettingsFragment newFragment = new SettingsFragment();

                Bundle args = new Bundle();
                args.putInt(Constants.PREFERENCES_KEY_SERVER_INSTANCE, instance);
                newFragment.setArguments(args);

                replaceFragment(newFragment);
                return false;
            });

            return screen;
        }
        */

    }
}
