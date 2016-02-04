package soen390.mapx.webapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import soen390.mapx.model.Floor;
import soen390.mapx.model.POI;

/**
 * Interface to react to POI-related actions on the map
 */
public class POIJSInterface {

    private Context context;


    public POIJSInterface(Context context) {
        this.context = context;
    }

    /**
     * Return JSON that contains information of POIs
     * @return JSON with POIs information
     */
    public String getPOIsJSON() {
        JSONObject jsonObj = new JSONObject();

        JSONArray poiArr = new JSONArray();
        JSONObject poisObj;

        List<POI> pois = POI.listAll(POI.class);

        try{
            for(POI poi: pois) {
                poisObj = new JSONObject();
                poisObj.put("_id", poi.getId());
                poisObj.put("title", poi.getTitle());
                poisObj.put("type", poi.getType());
                poisObj.put("floor", poi);
                poisObj.put("x_coord", poi.getxCoord());
                poisObj.put("y_coord", poi.getyCoord());
                poiArr.put(poisObj);
            }

            jsonObj.put("poi", poiArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObj.toString();
    }
    /**
     * Return JSON that contains information of the museum floors
     * @return JSON string of the floor information
     */
    public String getFloorJSON(){
        JSONObject jsonObj = new JSONObject();

        JSONArray floorArr = new JSONArray();
        JSONObject floorObj;

        List<Floor> floors = Floor.listAll(Floor.class);

        try{
            for(Floor floor: floors) {
                floorObj = new JSONObject();
                floorObj.put("floor_num", floor.getFloorNum());
                floorObj.put("floor_path", floor.getImageFilePath());
                floorArr.put(floorObj);
            }

            jsonObj.put("floor", floorArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObj.toString();
    }

}
