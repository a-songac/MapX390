package soen390.mapx.manager;

import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.model.POI;
import soen390.mapx.model.Storyline;

/**
 * Manage the state of the map
 */
public class MapManager {
    private static MapManager ourInstance = new MapManager();

    public static MapManager getInstance() {
        return ourInstance;
    }

    private MapManager() {
    }

    private boolean storylineMode = false;
    private boolean  navigationMode = false;
    private POI lastPOI = null;
    private POI currentPOIDestination= null;
    private Storyline currentStoryline = null;


    public boolean isStorylineMode() {
        return storylineMode;
    }

    public boolean isNavigationMode() {
        return navigationMode;
    }

    /**
     * Launch the storyline mode
     * @param storylineId
     */
    public void launchStoryline(Long storylineId) {

        if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

        }

        navigationMode = false;
        storylineMode = true;
        currentStoryline = Storyline.findById(Storyline.class, storylineId);

        syncActionBarStateWithCurrentMode();


        //TODO call web client to display storyline path


    }

    /**
     * Set action bar accordingly to the current map mode (navigation, storyline, normal
     */
    public void syncActionBarStateWithCurrentMode() {

        if (storylineMode) {

            ActionBarHelper.getInstance().setMapFragmentStorylineModeActionBar(currentStoryline.getTitle(), currentStoryline.getColor());

        } else if (navigationMode) {

            ActionBarHelper.getInstance().setMapFragmentNavigationModeActionBar(currentPOIDestination.getTitle());

        } else {
            ActionBarHelper.getInstance().setMapFragmentActionBar();
        }
        MainActivity.class.cast(MapXApplication.getGlobalContext()).invalidateOptionsMenu();

    }
}
