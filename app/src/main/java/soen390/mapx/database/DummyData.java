package soen390.mapx.database;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

}
