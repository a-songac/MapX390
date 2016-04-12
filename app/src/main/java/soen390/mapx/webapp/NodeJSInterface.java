package soen390.mapx.webapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.manager.ContentManager;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.Floor;
import soen390.mapx.model.Node;

/**
 * Interface to react to Node-related actions on the map
 */
public class NodeJSInterface {

    private Context context;
    public static final String ANDROID_JAVASCRIPT_OBJECT = "Android";

    public NodeJSInterface() {}

    public NodeJSInterface(Context context) {
        this.context = context;
    }

    /**
     * Receive a call from Javascript stating that the Webview is fully initiliazed
     * @return
     */
    @JavascriptInterface
    public void initialized() {
        MainActivity mainActivity = MainActivity.class.cast(MapXApplication.getGlobalContext());
        if (mainActivity.isPOIReachedFromNotification()) {
            MapManager.displayOnMapPOIReached();
            mainActivity.userPositionDisplayedAfterNotification();
        }
    }

    /**
     * Return JSON that contains information of POIs
     * @return JSON with POIs information
     */
    @JavascriptInterface
    public String getPOIsJSON() {
        List<Node> nodes = Node.listAll(Node.class);
        String str =  buildPOIJSON(nodes).toString();
        return str;
    }

    /**
     * Return JSON that contains information of the museum floors
     * @return JSON string of the floor information
     */
    @JavascriptInterface
    public String getFloorsJSON(){
        List<Floor> floors = Floor.listAll(Floor.class);
        String str = buildFloorJSON(floors).toString();
        return str;
    }

    /**
     * Receive from javascript Node to navigate to
     * @param poiId
     */
    @JavascriptInterface
    public void navigateToPOI(final String poiId) {

        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                MapManager.launchNavigation(Long.parseLong(poiId));
            }
        };
        mainHandler.post(myRunnable);

        //TEST
//        int[] path = {2,3,4};
//        MapJSBridge mapJSBridge = MapJSBridge.getInstance();
//        mapJSBridge.drawPath();
        //TEST END
    }

    /**
     * Returns boolean whether user is in any type of navigation mode
     * @return boolean
     */
    @JavascriptInterface
    public boolean isInMode() {
        return (MapManager.isNavigationMode() || MapManager.isStorylineMode());
    }

    /**
     * Returns boolean whether user is in storyline mode
     * @return boolean
     */
    @JavascriptInterface
    public boolean isInStorylineMode() {
        return MapManager.isStorylineMode();
    }

    /**
     * Returns boolean whether user is in navigation mode
     * @return
     */
    @JavascriptInterface
    public boolean isInNavigationMode() {
        return MapManager.isNavigationMode();
    }

    /**
     * Saves the current zoom level instance on the displayed floor
     */
    @JavascriptInterface
    public void setZoomLevel(String zoomLevel) {
        MapManager.setZoomLevel(zoomLevel);
    }

    /**
     * Provides zoom level data to webview
     */
    @JavascriptInterface
    public String getZoomLevel() {
        return MapManager.getZoomLevel();
    }

    /**
     * Provides the position of where the user is currently looking at
     */
    @JavascriptInterface
    public String getCurrentView() {
        JSONArray currentViewArr = new JSONArray();
        String[] currentView = MapManager.getCurrentView();

        for(int i = 0; i < currentView.length; i++) {
            currentViewArr.put(currentView[i]);
        }

        return currentViewArr.toString();
    }

    /**
     * Provides the position of where the user is currently looking at
     */
    @JavascriptInterface
    public void setCurrentView(String[] currentView) {
        MapManager.setCurrentView(currentView);
    }

    /**
     * Provide content for languages on the web client side
     * @return
     */
    @JavascriptInterface
    public String getLanguageJSON() {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("web_change_destination", context.getString(R.string.web_change_destination));
            jsonObj.put("web_go_to_destination", context.getString(R.string.web_go_to_destination));
            jsonObj.put("web_view_info", context.getString(R.string.web_view_info));

            return jsonObj.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Send id of current node where the user is at
     * @return
     */
    @JavascriptInterface
    public String getUserPosition() {

        Node lastNode = MapManager.getLastNodeOrInitial();

        if (null != lastNode) {
            return String.valueOf(lastNode.getId());
        }

        return null;
    }

    /**
     * Send id of current node where the user is at
     * @return
     */
    @JavascriptInterface
    public void viewInfo(String poiID) {
        LogUtils.info(NodeJSInterface.class, "viewInfo", "View info for poi " + poiID);
        //NavigationHelper.getInstance().navigateToMediaPagerFragment(MapManager.getLastNodeOrInitial().getId());
        NavigationHelper.getInstance().navigateToMediaPagerFragment(Long.parseLong(poiID));
    }

    /**
     * Send id of current node where the user is at
     * @return
     */
    @JavascriptInterface
    public String getCurrentPOIFloor() {
        Node lastNode = MapManager.getLastNodeOrInitial();
        if (null != lastNode) {
            return lastNode.getFloorId();
        }
        return null;

    }

    /**
     * Send id of current node where the user is at
     * @return
     */
    @JavascriptInterface
    public String getPath() {
        try{
            JSONArray pathArr = new JSONArray();

            for(int nodeID : MapManager.getCurrentPath()){
                pathArr.put(nodeID);
            }

            return pathArr.toString();
        }

        catch(Exception e){
            LogUtils.error(this.getClass(), "getPath", e.getMessage());
            return null;
        }
    }

    /**
     * Update the current path after the user progresses
     */
    @JavascriptInterface
    public void setPath(String[] path) {
        try{
            ArrayList<Integer> updatedPath = new ArrayList<Integer>();

            for(int i = 0; i < path.length; i++){
                updatedPath.add(Integer.parseInt(path[i]));
            }

            MapManager.setCurrentPath(updatedPath);
        }

        catch(Exception e){
            LogUtils.error(this.getClass(), "setPath", Log.getStackTraceString(e));
        }
    }

    /**
     * Send current floor shown to map view
     * @return floor
     */
    @JavascriptInterface
    public String getCurrentFloor(){
        return MapManager.getCurrentFloor();
    }

    /**
     * Set current floor show on map view
     */
    @JavascriptInterface
    public void setCurrentFloor(String floor){
        MapManager.setCurrentFloor(floor);
    }

    /**
     * Get id of next POI to visit in a storyline tour
     * @return
     */
    @JavascriptInterface
    public String getNextPOIInStoryline() {
        Node nextPOI = MapManager.getNextPoiCheckpointInPath();

        if (null != nextPOI) {
            return String.valueOf(nextPOI.getId());
        }

        return null;
    }

    /**
     * Given a list of POIs build a JSON corresponding to it
     * @param nodes List of POIs
     * @return JSON corresponding to the lsit of POIs
     */
    public JSONArray buildPOIJSON(List<Node> nodes){

        JSONArray poiArr = new JSONArray();
        JSONObject poisObj;

        try{
            for(Node node : nodes) {
                poisObj = new JSONObject();
                poisObj.put("_id", node.getId());
                poisObj.put("title", node.getTitle());
                poisObj.put("type", node.getType());
                poisObj.put("floor", node.getFloorId());
                poisObj.put("x_coord", node.getxCoord());
                poisObj.put("y_coord", node.getyCoord());
                poiArr.put(poisObj);
            }


        } catch (JSONException e){
            LogUtils.error(this.getClass(), "buildPOIJSON", e.getMessage());
        }

        return poiArr;
    }
    /**
     * Given a list of floors build a JSON corresponding to it
     * @param floors List of floors
     * @return JSON corresponding to the list of floors
     */
    public JSONArray buildFloorJSON(List<Floor> floors){
        JSONArray floorArr = new JSONArray();
        JSONObject floorObj;

        try{
            for(Floor floor: floors) {
                floorObj = new JSONObject();
                floorObj.put("floor_num", floor.getFloorId());
                floorObj.put("floor_id", String.valueOf(floor.getId()));
                floorObj.put("floor_path", "file://" + ContentManager.getMediaDirectoryPath() + floor.getImageFilePath());
//                floorObj.put("floor_path", "tiles/" + floor.getImageFilePath());
                floorObj.put("floor_width", floor.getImageWidth());
                floorObj.put("floor_height", floor.getImageHeight());
                floorArr.put(floorObj);
            }

        } catch (JSONException e){
            LogUtils.error(this.getClass(), "buildFloorJSON", e.getMessage());
        }

        return floorArr;

    }

}
