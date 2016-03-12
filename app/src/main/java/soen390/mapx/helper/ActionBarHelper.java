package soen390.mapx.helper;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;

import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;

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

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_museum_map);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));

    }

    /**
     * Action bar for map fragment in navigation mode
     * @param title : Point of interest destination title
     */
    public void setMapFragmentNavigationModeActionBar(String title) {

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.green_navigation_mode);

        getActionBar().setTitle(title);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     * Action bar for map fragment in storyline mode
     * @param title : Storyline title
     * @param color : Storyline theme color
     */
    public void setMapFragmentStorylineModeActionBar(String title, String color) {

        int backgroundColor = color !=null ?
                Color.parseColor(color):
                MapXApplication.getGlobalContext().getResources().getColor(R.color.blue_storyline_mode);

        getActionBar().setTitle(title);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     * Action bar for story-lines Fragment
     */
    public void setStorylineFragmentActionBar() {

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_story_lines);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     * Action bar for settings fragment
     */
    public void setSettingsFragmentActionBar() {

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_settings);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     *  Set media content action bar
     * @param title
     */
    public void setMediaContentActionBar(String title) {

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
        getActionBar().setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void disableHomeAsUp() {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }


}
