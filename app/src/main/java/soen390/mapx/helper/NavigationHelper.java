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
import soen390.mapx.fragment.MediaViewPagerFragment;
import soen390.mapx.fragment.POIsListFragment;
import soen390.mapx.fragment.SettingsFragment;
import soen390.mapx.fragment.StorylineListFragment;
import soen390.mapx.manager.MapManager;

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
                ConstantsHelper.MAP_FRAGMENT_BAC_KSTACK_ENTRY_NAME
        );
    }

    /**
     * Whether the current fragment on top is the map fragment
     * @return
     */
    public boolean isMapFragmentDisplayed() {
        return getContainerFragment().getTag().equals(ConstantsHelper.MAP_FRAGMENT_TAG);
    }

    /**
     * Add settings fragment over current fragment (always map fragment)
     * @param triggerLanguage : whether trigger the language settings upon loading the fragment
     */
    public void navigateToSettingsFragment(boolean triggerLanguage) {

        Fragment settingsFragment = SettingsFragment.newInstance(triggerLanguage);

        addFragment(
                settingsFragment,
                false,
                true,
                ConstantsHelper.SETTINGS_FRAGMENT_TAG,
                null);

    }

    /**
     * Add storyline fragment over current fragment (always map fragment)
     */
    public void navigateToStorylineFragment() {

        addFragment(
                new StorylineListFragment(),
                false,
                true,
                ConstantsHelper.STORYLINE_FRAGMENT_TAG,
                null
        );

    }

    /**
     * Add media pager fragment over current fragment (map fragment)
     */
    public void navigateToMediaPagerFragment(Long poiId){

        addFragment(
                MediaViewPagerFragment.newInstance(poiId),
                false,
                true,
                ConstantsHelper.MEDIA_PAGER_FRAGMENT_TAG,
                null
        );

    }

    /**
     * Add pois search list fragment
     */
    public void navigateToPOIsSearchFragment() {
        addFragment(
                new POIsListFragment(),
                false,
                true,
                ConstantsHelper.POIS_SEARCH_LIST_FRAGMENT_TAG,
                null
        );
    }

    /**
     * Pop fragments in the back stack until the map fragment is reached
     */
    public void popFragmentBackStackToMapFragment() {

        getSupportFragmentManager().popBackStackImmediate(ConstantsHelper.MAP_FRAGMENT_BAC_KSTACK_ENTRY_NAME, 0);
        MapManager.syncActionBarStateWithCurrentMode();
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
     * Replace fragment
     * @param fragment : fragment to display
     * @param withTransition : whether transition is used in the replacement
     * @param addToBackStack : whether the fragment is added to the back stack
     * @param tag : fragment tag
     * @param backStackEntryName : back stack entry name
     */
    private void addFragment(Fragment fragment, boolean withTransition, boolean addToBackStack,
                                 @Nullable String tag, @Nullable String backStackEntryName) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, tag);
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
