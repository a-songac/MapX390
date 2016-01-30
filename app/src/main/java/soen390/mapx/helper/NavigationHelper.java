package soen390.mapx.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.fragment.MapFragment;
import soen390.mapx.fragment.SettingsFragment;

/**
 * Class to implement navigation helper
 */
public class NavigationHelper {

    private static NavigationHelper instance;

    static {
        instance = new NavigationHelper();
    }

    /**
     * Get singleton instance
     * @return NavigationHelper : singleton
     */
    public static NavigationHelper getInstance() {
        return instance;
    }


    /**
     * Get fragment in the main_menu activity frame layout container
     * @return Fragment : fragment currently
     */
    public Fragment getContainerFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    /**
     * Pop back stack if there are currently at least 2 fragments
     */
    public void navigateToLastFragment() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
    }

    /**
     * Navigate to Map Fragment
     */
    public void navigateToMapFragment() {

        Fragment fragment = MapFragment.newInstance();
        replaceFragment(
                fragment,
                false,
                true,
                ConstantsHelper.MAP_FRAGMENT_TAG,
                null
        );
    }

    /**
     * Navigate to Settings Fragment
     * @param triggerLanguage : whether trigger the language settings upon loading the fragment
     */
    public void navigateToSettingsFragment(boolean triggerLanguage) {

        Fragment settingsFragment = SettingsFragment.newInstance(triggerLanguage);

        replaceFragment(
                settingsFragment,
                false,
                true,
                ConstantsHelper.SETTINGS_FRAGMENT_TAG,
                null);

    }

    /**
     * Navigate to main fragment
     */
    public void navigateToMainFragment() {
        navigateToMapFragment();
    }

    /**
     * Replace fragment
     * @param fragment : fragment to display
     * @param withTransition : whether transition is used in the replacement
     * @param addToBackStack : whether the fragment is added to the back stack
     * @param tag : fragment tag
     * @param backStackEntryName : back stack entry name
     */
    private void replaceFragment(Fragment fragment, boolean withTransition, boolean addToBackStack, 
                                @Nullable String tag, @Nullable String backStackEntryName) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(backStackEntryName);
        }
        if (withTransition) {
            fragmentTransaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.slide_out_right);
        }

        fragmentTransaction.commit();

    }

    /**
     * Get fragment manager
     * @return FragmentManager : fragment manager
     */
    private FragmentManager getSupportFragmentManager() {
        
        Context context = MapXApplication.getGlobalContext();
        return MainActivity.class.cast(context).getSupportFragmentManager();
        
    }






}
