package soen390.mapx.manager;

import android.content.Context;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.callback.IDialogResponseCallBack;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.AlertDialogHelper;
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
     * Launch the storyline mode
     * @param poiId
     */
    public void launchNavigation(Long poiId) {

        if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

        }

        navigationMode = true;
        storylineMode = false;
        currentPOIDestination = POI.findById(POI.class, poiId);

        syncActionBarStateWithCurrentMode();


        //TODO call web client to display storyline path


    }

    public void leaveCurrentMode() {
        if (storylineMode) {
            leaveStorylineMode();
        } else if (navigationMode) {
            leaveNavigationMode();
        }
    }


    /**
     * leave storyline mode
     */
    public void leaveStorylineMode() {

        Context context = MapXApplication.getGlobalContext();

        String title= context.getResources().getString(R.string.storyline_leave);
        String message = context.getResources().getString(R.string.storyline_leave_message);

        AlertDialogHelper.showAlertDialog(title, message, new IDialogResponseCallBack() {
            @Override
            public void onPositiveResponse() {
                navigationMode = false;
                storylineMode = false;
                currentStoryline = null;

                //TODO call web client to erase storyline path
                syncActionBarStateWithCurrentMode();
            }

            @Override
            public void onNegativeResponse() {

            }
        });

    }

    /**
     * leave storyline mode
     */
    public void leaveNavigationMode() {

        Context context = MapXApplication.getGlobalContext();

        String title= context.getResources().getString(R.string.navigation_leave);
        String message = context.getResources().getString(R.string.navigation_leave_message);

        AlertDialogHelper.showAlertDialog(title, message, new IDialogResponseCallBack() {
            @Override
            public void onPositiveResponse() {
                navigationMode = false;
                storylineMode = false;
                currentPOIDestination = null;

                //TODO call web client to erase navigation path

                syncActionBarStateWithCurrentMode();
            }

            @Override
            public void onNegativeResponse() {
            }
        });

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
