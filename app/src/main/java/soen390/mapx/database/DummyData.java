package soen390.mapx.database;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.application.MapXApplication;

/**
 * Class to generate dummy data
 */
public class DummyData {

    /**
     * Load dummy data from local json
     *
     * @return JsonElement
     */
    public static JsonElement loadJSON() {
        JsonParser jsonParser = new JsonParser();

        AssetManager assetManager = MapXApplication.getGlobalContext().getAssets();
        try {

            InputStream input = assetManager.open("demoData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            return jsonParser.parse(br).getAsJsonObject();

        } catch (IOException e) {
            LogUtils.error(DummyData.class, "loadJSON", Log.getStackTraceString(e));
        }

        return null;

    }

    public static String[] dummyImages() {
        String[] imagesPathsDummy = {"old_radio", "moeb_logo", "gramophone_victrola"};
        return imagesPathsDummy;
    }


    public static JSONArray dummyMedia() {
        JSONArray poiArr = new JSONArray();
        JSONObject poisObj;

        try {

            poisObj = new JSONObject();
            poisObj.put("_id", 1);
            poisObj.put("title", "Nipper");
            poisObj.put("description", "Lorem ipsum");
            poisObj.put("path", "moeb_point1");
            poisObj.put("type", "V");
            poiArr.put(poisObj);

            poisObj = new JSONObject();
            poisObj.put("_id", 2);
            poisObj.put("title", "Video 2");
            poisObj.put("description", "Lorem ipsum");
            poisObj.put("path", "moeb_point2");
            poisObj.put("type", "V");
            poiArr.put(poisObj);

            poisObj = new JSONObject();
            poisObj.put("_id", 3);
            poisObj.put("title", "Video 3");
            poisObj.put("description", "Lorem ipsum");
            poisObj.put("path", "moeb_point3");
            poisObj.put("type", "V");
            poiArr.put(poisObj);

            poisObj = new JSONObject();
            poisObj.put("_id", 4);
            poisObj.put("title", "Audio 1");
            poisObj.put("description", "Lorem ipsum");
            poisObj.put("path", "moeb_point4");
            poisObj.put("type", "A");
            poiArr.put(poisObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return poiArr;
    }

}
