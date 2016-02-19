package soen390.mapx.webapp;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
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
        return buildPOIJSON(nodes).toString();
    }

    /**
     * Return JSON that contains information of the museum floors
     * @return JSON string of the floor information
     */
    @JavascriptInterface
    public String getFloorJSON(){
        List<Floor> floors = Floor.listAll(Floor.class);
        return buildFloorJSON(floors).toString();
    }

    /**
     * Receive from javascript Node to navigate to
     * @param poiId
     */
    @JavascriptInterface
    public void navigateToPOI(String poiId) {

        String str = context.getResources().getString(R.string.poi_selected_as_destination, poiId);
        UiUtils.displayToastLong(str);

        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                MapManager.launchNavigation(1L); //TODO hardcoded id temporarily because the javascript does not return a long yet
            }
        };
        mainHandler.post(myRunnable);

        //TEST
        int[] path = {2,3,4};
        MapJSBridge mapJSBridge = MapJSBridge.getInstance();
        mapJSBridge.drawPath(path);
        //TEST END
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
     * Given a list of POIs build a JSON corresponding to it
     * @param nodes List of POIs
     * @return JSON corresponding to the lsit of POIs
     */
    public JSONObject buildPOIJSON(List<Node> nodes){
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

            jsonObj.put("poi", poiArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObj;
    }
    /**
     * Given a list of floors build a JSON corresponding to it
     * @param floors List of floors
     * @return JSON corresponding to the list of floors
     */
    public JSONObject buildFloorJSON(List<Floor> floors){
        JSONObject jsonObj = new JSONObject();

        JSONArray floorArr = new JSONArray();
        JSONObject floorObj;

        try{
            for(Floor floor: floors) {
                floorObj = new JSONObject();
                floorObj.put("floor_num", floor.getFloorNum());
                floorObj.put("floor_path", floor.getImageFilePath());
                floorObj.put("floor_width", floor.getImageWidth());
                floorObj.put("floor_height", floor.getImageHeight());
                floorArr.put(floorObj);
            }

            jsonObj.put("floor", floorArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObj;

    }

}
