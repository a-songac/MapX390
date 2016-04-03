package soen390.mapx.fragment;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.arnaud.android.core.fragment.IBaseFragment;

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
     * @return Fragment
     */
    public static SettingsFragment newInstance() {

        return new SettingsFragment();

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
        if (getView() != null)
            getView().setBackgroundColor(Color.WHITE);

        ActionBarHelper.getInstance().setSettingsFragmentActionBar();
        getActivity().invalidateOptionsMenu();

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
            getActivity().recreate();
        }

    }


    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
    }

}
