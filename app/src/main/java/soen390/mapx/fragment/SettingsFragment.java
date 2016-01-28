package soen390.mapx.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements IBaseFragment,
        SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
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

        if (key.equals(ConstantsHelper.PREF_LANGUAGE)) {

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
