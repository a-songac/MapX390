package soen390.mapx.helper;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnaud.android.core.application.BaseApplication;

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

        setRegularViewActionBar();

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_museum_map);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));

    }

    /**
     * Action bar for map fragment in navigation mode
     * @param title : Point of interest destination title
     */
    public void setMapFragmentNavigationModeActionBar(String title) {

        setRegularViewActionBar();

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

        setRegularViewActionBar();

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

        setRegularViewActionBar();

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_story_lines);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     * Action bar for settings fragment
     */
    public void setSettingsFragmentActionBar() {

        setRegularViewActionBar();

        int backgroundColor = MapXApplication.getGlobalContext().getResources().getColor(R.color.colorPrimary);

        getActionBar().setTitle(R.string.navigation_drawer_settings);
        getActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     *  Set media content action bar
     * @param title
     */
    public void setMediaContentActionBar(String title) {

        setRegularViewActionBar();

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

    /**
     * Set action bar for POI search fragment
     */
    public void setPOIsSearchFragmentActionBar() {

        getActionBar().setDisplayShowTitleEnabled(false);

        View view =  LayoutInflater.from(BaseApplication.getGlobalContext())
                .inflate(R.layout.poi_search_action_bar_edittext, null);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        getActionBar().setCustomView(view, lp);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().show();

    }

    /**
     * Set regular view common action bar ui elements
     */
    private void setRegularViewActionBar() {

        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }

}
