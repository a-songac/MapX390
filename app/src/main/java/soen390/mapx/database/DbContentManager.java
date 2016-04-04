package soen390.mapx.database;

import android.os.Environment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.orm.SugarRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.helper.PreferenceHelper;
import soen390.mapx.model.Description;
import soen390.mapx.model.Edge;
import soen390.mapx.model.ExpositionContent;
import soen390.mapx.model.Floor;
import soen390.mapx.model.IBeacon;
import soen390.mapx.model.Node;
import soen390.mapx.model.QRCode;
import soen390.mapx.model.Storyline;
import soen390.mapx.model.StorylineNode;

/**
 * Class to manage persistent data
 */
public class DbContentManager {

    private DbContentManager(){}

    public static final String EXTERNAL_STORAGE_MAPX_DIR = "mapx";
    public static final String JSON_FILE_NAME= "mapx.json";

    /**
     * Init database content in first use
     */
    public static void initDatabaseContent(JsonElement root) {

        if (true || !PreferenceHelper.getInstance().isDbInitPreference()) {//TODO TEMP reparse

            if (root ==null)
                root = DummyData.loadJSON(); //TODO temp

            if (null != root) {
                clearDb(); //TODO temp
                persistJsonContent(root);
                PreferenceHelper.getInstance().initDB();
            } else {
                LogUtils.error(DbContentManager.class, "initDatabaseConent", "Null JsonElement data source");
            }
        }

    }

    /**
     * Load JSON file used to seed the database
     * @return
     */
    public static JsonElement prepareJSONSeed() {

        try {
            String dir = Environment.getExternalStorageDirectory()
                    + File.separator + EXTERNAL_STORAGE_MAPX_DIR;
            FileInputStream is = new FileInputStream(dir + File.separator + JSON_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(br).getAsJsonObject();

        } catch (FileNotFoundException e) {
            LogUtils.error(DbContentManager.class, "prepareJSONSeed", e.getMessage() );
        }
        return null;
    }

    /**
     * Parse JSON received from map editor
     * @param root
     */
    public static void persistJsonContent(JsonElement root) {

        JsonArray floorArr = root.getAsJsonObject().get("floorPlan").getAsJsonArray();
        JsonArray storylineArr = root.getAsJsonObject().get("storyline").getAsJsonArray();
        JsonArray edgeArr = root.getAsJsonObject().get("edge").getAsJsonArray();
        JsonArray nodeArr = root.getAsJsonObject().get("node").getAsJsonArray();
        JsonArray poiArr = nodeArr.get(0).getAsJsonObject().get("poi").getAsJsonArray();
        JsonArray potArr = nodeArr.get(0).getAsJsonObject().get("pot").getAsJsonArray();



        List<Floor> floors = MapEditorContentJSONParser.parseFloors(floorArr);
        SugarRecord.saveInTx(floors);

        List<IBeacon> beacons = MapEditorContentJSONParser.parseIBeacons(poiArr);
        SugarRecord.saveInTx(beacons);

        List<Node> pois = MapEditorContentJSONParser.parsePOINodes(poiArr);
        SugarRecord.saveInTx(pois);

        List<Node> pots = MapEditorContentJSONParser.parsePOTNodes(potArr);
        SugarRecord.saveInTx(pots);

        List<Storyline> storylines = MapEditorContentJSONParser.parseStorylines(storylineArr);
        SugarRecord.saveInTx(storylines);

        List<Edge> edges = MapEditorContentJSONParser.parseEdges(edgeArr);
        SugarRecord.saveInTx(edges);

        List<Description> nodeDescriptions = MapEditorContentJSONParser.parseDescriptions(
                poiArr,
                Description.POI_DESC);
        SugarRecord.saveInTx(nodeDescriptions);

        List<Description> storylineDescriptions = MapEditorContentJSONParser.parseDescriptions(
                storylineArr,
                Description.STORYLINE_DESC);
        SugarRecord.saveInTx(storylineDescriptions);

        List<StorylineNode> storylineNodes = MapEditorContentJSONParser.parseStorylineNodes(
                storylineArr
        );
        SugarRecord.saveInTx(storylineNodes);

        List<ExpositionContent> expositionContents = MapEditorContentJSONParser.parseMediaContent(
                poiArr
        );
        SugarRecord.saveInTx(expositionContents);

    }

    /**
     * Clear db tables
     */
    private static void clearDb(){
        SugarRecord.deleteAll(Floor.class);
        SugarRecord.deleteAll(IBeacon.class);
        SugarRecord.deleteAll(Node.class);
        SugarRecord.deleteAll(Storyline.class);
        SugarRecord.deleteAll(Edge.class);
        SugarRecord.deleteAll(Description.class);
        SugarRecord.deleteAll(ExpositionContent.class);
        SugarRecord.deleteAll(StorylineNode.class);
        SugarRecord.deleteAll(QRCode.class);
    }
}
