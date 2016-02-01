package soen390.mapx.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements IBaseFragment,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Create new instance of settings fragment
     * @param triggerLanguageSettings: whether trigger the language settings upon loading the fragment
     * @return Fragment
     */
    public static SettingsFragment newInstance(boolean triggerLanguageSettings) {

        Bundle arguments = new Bundle();
        if (triggerLanguageSettings) {
            arguments.putBoolean(ConstantsHelper.SETTINGS_FRAGMENT_ARG_KEY_TRIGGER_LANGUAGE, true);
        }
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(arguments);
        return settingsFragment;

    }


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBarHelper.getInstance().setSettingsFragmentActionBar();

        if (null != getArguments()) {

            if (getArguments().getBoolean(ConstantsHelper.SETTINGS_FRAGMENT_ARG_KEY_TRIGGER_LANGUAGE)) {

                getPreferenceScreen().findPreference(ConstantsHelper.PREF_LANGUAGE_KEY).performClick(); //TODO performclick not triggered
                LogUtils.info(this.getClass(), "onActivityCreated" , "click perform click");

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(ConstantsHelper.PREF_LANGUAGE_KEY)) {

            Preference languagePref = findPreference(key);
            languagePref.setSummary(sharedPreferences.getString(key, ""));
            getActivity().recreate();

        }

    }


    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().navigateToLastFragment();
    }

}
