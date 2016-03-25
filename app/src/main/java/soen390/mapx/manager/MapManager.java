package soen390.mapx.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.callback.IDialogResponseCallBack;
import soen390.mapx.datastructure.PathFinder;
import soen390.mapx.datastructure.WeightedGraph;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.AlertDialogHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.model.Edge;
import soen390.mapx.model.Node;
import soen390.mapx.model.Storyline;
import soen390.mapx.webapp.MapJSBridge;

/**
 * Manage the state of the map
 */
public class MapManager {

    private static final long INFO_CENTER_ID = 0L;

    private static boolean storylineMode = false;
    private static boolean  navigationMode = false;
    private static Node lastNode = null;
    private static Node currentNodeDestination = null;
    private static Storyline currentStoryline = null;
    private static ArrayList<Integer> currentPath = null;
    private static int nextPoiCheckpointPositionInPath = -1;
    private static Node nextPoiCheckpointInPath = null;
    private static String currentFloor = null;
    private static String zoomLevel = null;
    private static String[] currentView = new String[2];

    public static boolean isStorylineMode() { return storylineMode; }

    public static Storyline getCurrentStoryline() {return currentStoryline;}

    public static boolean isNavigationMode() { return navigationMode; }

    public static String getCurrentFloor() { return currentFloor; }

    public static String getZoomLevel() { return zoomLevel; }

    public static String[] getCurrentView() { return currentView; }

    /**
     * Update the path when the user progresses
     * @param updatedPath
     */
    public static void setCurrentPath(ArrayList<Integer> updatedPath){
        currentPath = updatedPath;
    }

    /**
     * Keep current view from webview
     * @param currView
     */
    public static void setCurrentView(String[] currView){
        currentView = currView;
    }

    /**
     * Keep zoom level from webview
     * @param zoomLvl
     */
    public static void setZoomLevel(String zoomLvl){
        zoomLevel = zoomLvl;
    }

    /**
     * Keep current floor viewed instance from web view
     * @param cFloor
     */
    public static void setCurrentFloor(String cFloor) {
        currentFloor = cFloor;
    }

    public static Node getLastNodeOrInitial(){
        if (null == lastNode) {
            return Node.findById(Node.class, 0); // Node with id 0 being info center
        }
        return lastNode;
    }

    public static ArrayList<Integer> getCurrentPath(){ return currentPath; }

    /**
     * Launch the storyline mode
     * @param storylineId
     */
    public static void launchStoryline(Long storylineId) {

        final Storyline storyline = Storyline.findById(Storyline.class, storylineId);

        Context context = MapXApplication.getGlobalContext();

        if (navigationMode || storylineMode) {

            int messageId = navigationMode?
                    R.string.storyline_change_message_poi:
                    R.string.storyline_change_message_sl;

            AlertDialogHelper.showAlertDialog(
                    context.getString(R.string.navigation_change),
                    context.getString(messageId, storyline.getTitle()),
                    new IDialogResponseCallBack() {
                        @Override
                        public void onPositiveResponse() {
                            //MapJSBridge.getInstance().leaveNavigation();
                            resetState();
                            launchStorylineStartingPointCheck(storyline);
                        }

                        @Override
                        public void onNegativeResponse() {

                        }
                    });
        } else {
            launchStorylineStartingPointCheck(storyline);
        }

    }

    /**
     * Start a storyline, but verify first the user is at starting point
     * @param storyline
     */
    private static void launchStorylineStartingPointCheck(Storyline storyline) {

        Context context = MapXApplication.getGlobalContext();

        if (INFO_CENTER_ID != getLastNodeOrInitial().getId()) {

            AlertDialogHelper.showAlertDialog(
                    context.getString(R.string.storyline_go_to_starting_point),
                    context.getString(R.string.storyline_go_to_starting_point_message),
                    new IDialogResponseCallBack() {
                @Override
                public void onPositiveResponse() {
                    launchNavigation(INFO_CENTER_ID);
                }

                @Override
                public void onNegativeResponse() {

                }
            });
        } else {
            launchStorylineHelper(storyline);
        }

    }

    /**
     * Launch Storyline helper
     * @param storyline
     */
    private static void launchStorylineHelper(Storyline storyline) {

        if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

        }

        navigationMode = false;
        storylineMode = true;
        currentStoryline = storyline;

        currentPath = storyline.getPath();
        establishNextPoiCheckpoint();
        currentNodeDestination = Node.findById(Node.class,currentPath.get(currentPath.size()-1));

        syncActionBarStateWithCurrentMode();

        //TEMPORARY
        MapJSBridge.getInstance().drawPath();

        String toast = MapXApplication.getGlobalContext().getResources().getString(
                R.string.storyline_start_toast,
                storyline.getTitle());
        UiUtils.displayToastLong(toast);
    }

    /**
     * Launch the navigation mode
     * @param poiId
     */
    public static void launchNavigation(Long poiId) {

        if (poiId.equals(getLastNodeOrInitial().getId())) {

            UiUtils.displayToastLong(MapXApplication.getGlobalContext().getString(R.string.navigation_already_at_poi));

        } else {

            final Node newNode = Node.findById(Node.class, poiId);

            final Context context = MapXApplication.getGlobalContext();

            if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

                NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

            }

            if (navigationMode || storylineMode) {

                int messageId = navigationMode ?
                        R.string.navigation_change_message_poi :
                        R.string.navigation_change_message_sl;

                AlertDialogHelper.showAlertDialog(
                        context.getString(R.string.navigation_change),
                        context.getString(messageId, newNode.getTitle()),
                        new IDialogResponseCallBack() {
                            @Override
                            public void onPositiveResponse() {
                                //MapJSBridge.getInstance().leaveNavigation();
                                resetState();
                                launchNavigation(newNode, context);
                            }

                            @Override
                            public void onNegativeResponse() {

                            }
                        });
            } else {
                launchNavigation(newNode, context);
            }
        }

    }

    /**
     * Launch navigation mode helper
     * @param newNode
     * @param context
     */
    private static void launchNavigation(Node newNode, Context context){
        navigationMode = true;
        storylineMode = false;
        currentNodeDestination = newNode;

        syncActionBarStateWithCurrentMode();

        computePath();

        MapJSBridge.getInstance().drawPath();

        String str = context.getResources().getString(
                R.string.poi_selected_as_destination, newNode.getTitle());
        UiUtils.displayToastLong(str);

    }

    /**
     * Establish the position in the path where the next POI checkpoint is.
     * This checkpoint corresponds to a POI that will be detected (ibeacon) as the user progresses but
     * that is not the final destination.
     *
     * Keep track of the position of this poi in the path list, plus keep track of the id of the poi node
     *
     */
    private static void establishNextPoiCheckpoint() {
        Node node;
        for (int i = 1; i < currentPath.size(); i++) {
            node = Node.findById(Node.class,currentPath.get(i));
            if (node.isPointOfInterest()) {
                nextPoiCheckpointInPath = node;
                nextPoiCheckpointPositionInPath = i;
                break;
            }
        }

    }

    /**
     * Update path as the user progresses on the right path.
     * IOW, remove the path steps that were reached from the currentPath list and update next checkpoint
     */
    private static void updatePath() {
        trimPath();
        establishNextPoiCheckpoint();
    }

    /**
     * Remove reached Nodes from the path so that the first node in the currentPath list is the current position
     */
    private static void trimPath(){
        for (int i=0; i < nextPoiCheckpointPositionInPath; i++) {
            currentPath.remove(0);
        }
    }

    /**
     * Remove reached nodes until the given poi
     * @param poiId: poi where the user is
     */
    private static void trimPath(long poiId) {
        for (int i=0; i < nextPoiCheckpointPositionInPath; i++) {
            currentPath.remove(0);
            if (currentPath.get(0) == poiId) {
                break;
            }
        }
    }

    /**
     * Compute path and draw it in on map
     */
    private static void computePath() {
        int[] pathTree = PathFinder.computeShortestPath(WeightedGraph.getInstance(Edge.listAll(Edge.class), Node.count(Node.class)), MapManager.getLastNodeOrInitial().getId());
        currentPath = PathFinder.getShortestPath(pathTree, MapManager.getLastNodeOrInitial().getId().intValue(), currentNodeDestination.getId().intValue());
        establishNextPoiCheckpoint();

        MapJSBridge.getInstance().drawPath();
    }

    /**
     * Adjust path of the storyline if user deviated.
     * Add the path that will guide him back on the right track
     */
    private static void adjustPathStoryline() {

        if (!isComingBackOnRightPath()) {

            List<Integer> adjustPath;
            int[] pathTree = PathFinder.computeShortestPath(WeightedGraph.getInstance(Edge.listAll(Edge.class), Node.count(Node.class)), MapManager.getLastNodeOrInitial().getId());
            adjustPath = PathFinder.getShortestPath(pathTree, MapManager.getLastNodeOrInitial().getId().intValue(), nextPoiCheckpointInPath.getId().intValue());
            adjustPath.remove(adjustPath.size() - 1);
            trimPath();
            currentPath.addAll(0, adjustPath);
            for (int i=0; i < currentPath.size(); i++) {
                if (nextPoiCheckpointInPath.getId() == (long)currentPath.get(i)) {
                    nextPoiCheckpointPositionInPath = i;
                    break;
                }
            }

        } else {
            trimPath(getLastNodeOrInitial().getId());
        }

    }

    /**
     * Given that user went wrong way while in storyline, verify if he is currently coming
     * back towards the next POI he is supposed to visit in the storyline tour.
     * @return
     */
    private static boolean isComingBackOnRightPath() {
        for (int i = 0; i < nextPoiCheckpointPositionInPath; i++) {
            if (getLastNodeOrInitial().getId() == (long)currentPath.get(i)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Reached a POI
     * @param poi
     */
    public static void reachPOI(Node poi) {
        lastNode = poi;
        if (navigationMode || storylineMode) {

            if (poi.getId().equals(currentNodeDestination.getId())) {

                resetState();

            } else if (poi.getId().equals(nextPoiCheckpointInPath.getId())) {

                updatePath();

            } else {
                if (navigationMode)
                    computePath();
                else
                    adjustPathStoryline();
            }
        }

        MapJSBridge.getInstance().reachedNode(poi.getId());
    }

    /**
     * When reached a POI and that user clicked the notification,
     * signal the web client to display the floor on which the poi was reached
     */
    public static void displayOnMapPOIReached() {

        MapJSBridge.getInstance().displayCurrentFloor();

    }


    /**
     * Leave current mode (storyline or navigation)
     */
    public static void leaveCurrentMode() {
        if (storylineMode) {
            leaveStorylineMode();
        } else if (navigationMode) {
            leaveNavigationMode();
        }
    }


    /**
     * leave storyline mode
     */
    public static void leaveStorylineMode() {

        Context context = MapXApplication.getGlobalContext();

        String title= context.getResources().getString(R.string.storyline_leave);
        String message = context.getResources().getString(R.string.storyline_leave_message);

        AlertDialogHelper.showAlertDialog(title, message, new IDialogResponseCallBack() {
            @Override
            public void onPositiveResponse() {
                resetState();
            }

            @Override
            public void onNegativeResponse() {

            }
        });

    }

    /**
     * Set the modes to null
     */
    public static void resetState(){
        navigationMode = false;
        storylineMode = false;
        currentStoryline = null;
        currentNodeDestination = null;
        currentPath = null;
        nextPoiCheckpointPositionInPath = -1;
        nextPoiCheckpointInPath = null;

        syncActionBarStateWithCurrentMode();

        MapJSBridge.getInstance().leaveStoryline();
    }

    /**
     * leave storyline mode
     */
    public static void leaveNavigationMode() {

        Context context = MapXApplication.getGlobalContext();

        String title= context.getResources().getString(R.string.navigation_leave);
        String message = context.getResources().getString(R.string.navigation_leave_message);

        AlertDialogHelper.showAlertDialog(title, message, new IDialogResponseCallBack() {
            @Override
            public void onPositiveResponse() {
                resetState();
            }

            @Override
            public void onNegativeResponse() {
            }
        });

    }


    /**
     * Set action bar accordingly to the current map mode (navigation, storyline, normal
     */
    public static void syncActionBarStateWithCurrentMode() {

        if (storylineMode) {

            ActionBarHelper.getInstance().setMapFragmentStorylineModeActionBar(currentStoryline.getTitle(), currentStoryline.getColor());

        } else if (navigationMode) {

            ActionBarHelper.getInstance().setMapFragmentNavigationModeActionBar(currentNodeDestination.getTitle());

        } else {
            ActionBarHelper.getInstance().setMapFragmentActionBar();
        }

        // if drawer is not enabled, then it means the home button was enabled (when in poi info fragment)
        if (!MainActivity.isDrawerEnabled()) {
            ActionBarHelper.getInstance().disableHomeAsUp();
            MainActivity.class.cast(MapXApplication.getGlobalContext()).enableDrawer(true);
        }

        MainActivity.class.cast(MapXApplication.getGlobalContext()).invalidateOptionsMenu();

    }
}
