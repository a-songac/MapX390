package soen390.mapx.manager;

import android.content.Context;

import java.util.ArrayList;

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

    private static boolean storylineMode = false;
    private static boolean  navigationMode = false;
    private static Node lastNode = null; //TODO set initial POI as museum info center maybe
    private static Node currentNodeDestination = null;
    private static Storyline currentStoryline = null;
    private static ArrayList<Integer> currentPath = null;
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

    public static Node getLastNode(){
        if (null == lastNode) {
            return Node.findById(Node.class, 0);
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
                            launchStoryline(storyline);
                        }

                        @Override
                        public void onNegativeResponse() {

                        }
                    });
        } else {
            launchStoryline(storyline);
        }

    }

    /**
     * Launch Storyline helper
     * @param storyline
     */
    private static void launchStoryline(Storyline storyline) {

        if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

        }

        navigationMode = false;
        storylineMode = true;
        currentStoryline = storyline;

        currentPath = storyline.getPath();

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

        final Node newNode = Node.findById(Node.class, poiId);

        final Context context = MapXApplication.getGlobalContext();

        if (!NavigationHelper.getInstance().isMapFragmentDisplayed()) {

            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

        }

        if (navigationMode || storylineMode) {

            int messageId = navigationMode?
                    R.string.navigation_change_message_poi:
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

        if(MapManager.getLastNode() == null){
            int[] pathTree = PathFinder.computeShortestPath(WeightedGraph.getInstance(Edge.listAll(Edge.class), Node.count(Node.class)), 0);
            currentPath = PathFinder.getShortestPath(pathTree,0, newNode.getId().intValue());
        }else{
            int[] pathTree = PathFinder.computeShortestPath(WeightedGraph.getInstance(Edge.listAll(Edge.class), Node.count(Node.class)), MapManager.getLastNode().getId());
            currentPath = PathFinder.getShortestPath(pathTree, MapManager.getLastNode().getId().intValue(), newNode.getId().intValue());
        }

        MapJSBridge.getInstance().drawPath();

        String str = context.getResources().getString(
                R.string.poi_selected_as_destination, newNode.getTitle());
        UiUtils.displayToastLong(str);

    }


    /**
     * Reached a POI
     * @param poi
     */
    public static void reachPOI(Node poi) {
        lastNode = poi;
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

    public static void resetState(){
        navigationMode = false;
        storylineMode = false;
        currentStoryline = null;
        currentNodeDestination = null;
        currentPath = null;

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
