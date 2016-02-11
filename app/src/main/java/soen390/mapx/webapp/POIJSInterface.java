package soen390.mapx.webapp;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.model.Floor;
import soen390.mapx.model.POI;

/**
 * Interface to react to POI-related actions on the map
 */
public class POIJSInterface {

    private Context context;
    public static final String ANDROID_JAVASCRIPT_OBJECT = "Android";

    public POIJSInterface() {}

    public POIJSInterface(Context context) {
        this.context = context;
    }

    /**
     * Return JSON that contains information of POIs
     * @return JSON with POIs information
     */
    @JavascriptInterface
    public String getPOIsJSON() {
        List<POI> pois  = POI.listAll(POI.class);
        return buildPOIJSON(pois).toString();
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
     * Receive from javascript POI to navigate to
     * @param poiId
     */
    @JavascriptInterface
    public void navigateToPOI(String poiId) {
        //TODO Temporary for sprint 1 to show that a destination has been selected
        String str = context.getResources().getString(R.string.poi_selected_as_destination, poiId);
        UiUtils.displayToastLong(str);

    }

    /**
     * Given a list of POIs build a JSON corresponding to it
     * @param pois List of POIs
     * @return JSON corresponding to the lsit of POIs
     */
    public JSONObject buildPOIJSON(List<POI> pois){
        JSONObject jsonObj = new JSONObject();

        JSONArray poiArr = new JSONArray();
        JSONObject poisObj;

        try{
            for(POI poi: pois) {
                poisObj = new JSONObject();
                poisObj.put("_id", poi.getId());
                poisObj.put("title", poi.getTitle());
                poisObj.put("type", poi.getType());
                poisObj.put("floor", poi.getFloorId());
                poisObj.put("x_coord", poi.getxCoord());
                poisObj.put("y_coord", poi.getyCoord());
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
