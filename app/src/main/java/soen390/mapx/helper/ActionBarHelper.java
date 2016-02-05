package soen390.mapx.helper;


import android.support.v7.app.ActionBar;

import soen390.mapx.R;

/**
 * Class to implement action bar helper
 */
public class ActionBarHelper {

    /**
     * Singleton instance
     */
    private static ActionBarHelper SINGLETON_INSTANCE = new ActionBarHelper();
    public static ActionBarHelper getInstance() {
        return SINGLETON_INSTANCE;
    }

    private ActionBarHelper (){};

    private ActionBar actionBar;

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    /**
     * Action bar for map fragment
     */
    public void setMapFragmentActionBar() {

        getActionBar().setTitle(R.string.navigation_drawer_museum_map);

    }

    /**
     * Action bar for story-lines Fragment
     */
    public void setStorylineFragmentActionBar() {

        getActionBar().setTitle(R.string.navigation_drawer_story_lines);
    }

    /**
     * Action bar for settings fragment
     */
    public void setSettingsFragmentActionBar() {

        getActionBar().setTitle(R.string.navigation_drawer_settings);
    }


}
