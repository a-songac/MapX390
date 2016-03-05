package soen390.mapx.webapp;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import soen390.mapx.R;
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
        int[] path = {2,3,4};
        MapJSBridge mapJSBridge = MapJSBridge.getInstance();
        mapJSBridge.drawPath();
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

        Node lastNode = MapManager.getLastNode();

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
    public String getCurrentPOIFloor() {
        Node lastNode = MapManager.getLastNode();
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
        JSONArray pathArr = new JSONArray();

        for(int nodeID : MapManager.getCurrentPath()){
            pathArr.put(nodeID);
        }

        return pathArr.toString();
    }

    /**
     * Given a list of POIs build a JSON corresponding to it
     * @param nodes List of POIs
     * @return JSON corresponding to the lsit of POIs
     */
    public JSONArray buildPOIJSON(List<Node> nodes){
        JSONObject jsonObj = new JSONObject();

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

//            jsonObj.put("poi", poiArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return poiArr;
    }
    /**
     * Given a list of floors build a JSON corresponding to it
     * @param floors List of floors
     * @return JSON corresponding to the list of floors
     */
    public JSONArray buildFloorJSON(List<Floor> floors){
        JSONObject jsonObj = new JSONObject();

        JSONArray floorArr = new JSONArray();
        JSONObject floorObj;

        try{
            for(Floor floor: floors) {
                floorObj = new JSONObject();
                floorObj.put("floor_num", floor.getFloorId());
                floorObj.put("floor_id", String.valueOf(floor.getId()));
                floorObj.put("floor_path", floor.getImageFilePath());
                floorObj.put("floor_width", floor.getImageWidth());
                floorObj.put("floor_height", floor.getImageHeight());
                floorArr.put(floorObj);
            }

//            jsonObj.put("floor", floorArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return floorArr;

    }

}
