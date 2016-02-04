package soen390.mapx.webapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import soen390.mapx.model.POI;

/**
 * Interface to react to POI-related actions on the map
 */
public class POIJSInterface {

    private Context context;


    public POIJSInterface(Context context) {
        this.context = context;
    }


    public String getPOIsJSON() {
        JSONObject jsonObj = new JSONObject();

        JSONArray poiArr = new JSONArray();
        JSONObject poisObj = new JSONObject();

        JSONArray floorArr = new JSONArray();
        JSONObject floorObj = new JSONObject();

        List<POI> pois = POI.listAll(POI.class);
        List<Floor> floors = Floor.listAll(Floor.class);


        try{
            for(POI poi: pois) {
                poisObj = new JSONObject();
                poisObj.put("_id", poi.id);
                poisObj.put("title", poi.title);
                poisObj.put("type", poi.type);
                poisObj.put("floor", poi.floor);
                poisObj.put("x_coord", poi.x_coord);
                poisObj.put("y_coord", poi.y_coord);
                poiArr.put(poisObj);
            }

            for(Floor floor: floors) {
                floorObj = new JSONObject();
                floorObj.put("floor_num", floor.num);
                floorObj.put("floor_path", floor.path);
                floorArr.put(poisObj);
            }

            jsonObj.put("floor", floorArr);
            jsonObj.put("poi", poiArr);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObj.toString();
    }


}
