package soen390.mapx.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.model.IBeacon;
import soen390.mapx.model.Description;
import soen390.mapx.model.Edge;
import soen390.mapx.model.ExpositionContent;
import soen390.mapx.model.Floor;
import soen390.mapx.model.Node;
import soen390.mapx.model.Storyline;
import soen390.mapx.model.StorylineNode;

/**
 * Method to parse the JSON from map editor
 */
public class MapEditorContentJSONParser {

    /**
     * Parse floor objects
     * @param floorArr
     * @return
     */
    public static List<Floor> parseFloors(JsonArray floorArr) {

        List<Floor> floors = new ArrayList<>();

        JsonObject floorJsonObj;

        for (int i = 0; i < floorArr.size(); i++) {

            floorJsonObj = floorArr.get(i).getAsJsonObject();

            floors.add(new Floor(
                    floorJsonObj.get("floorID").getAsString(),
                    floorJsonObj.get("imagePath").getAsString(),
                    floorJsonObj.get("imageWidth").getAsInt(),
                    floorJsonObj.get("imageHeight").getAsInt()
            ));
            LogUtils.info(
                    MapEditorContentJSONParser.class,
                    "parseFloors",
                    "Parsed floor: "
                            + floorJsonObj.get("floorID").getAsString());
        }

        return floors;
    }

    /**
     * Parse beacons
     * @param poiArr
     * @return
     */
    public static List<IBeacon> parseIBeacons(JsonArray poiArr) {

        List<IBeacon> beacons = new ArrayList<>();
        JsonObject poiJsonObj, beaconJsonObj;

        for (int i = 0; i < poiArr.size(); i++) {

            poiJsonObj = poiArr.get(i).getAsJsonObject();

            beaconJsonObj = poiJsonObj.get("iBeacon").getAsJsonObject();

            beacons.add(new IBeacon(
                    beaconJsonObj.get("uuid").getAsString(),
                    beaconJsonObj.get("minor").getAsInt(),
                    beaconJsonObj.get("major").getAsInt()
            ));
            LogUtils.info(
                    MapEditorContentJSONParser.class,
                    "parseIBeacons",
                    "Parsed iBeacon (uuid - major - minor) : "
                            + beaconJsonObj.get("uuid").getAsString() + " - "
                            + beaconJsonObj.get("major").getAsString() + " - "
                            + beaconJsonObj.get("minor").getAsString());
        }

        return beacons;
    }


    /**
     * PArse POI Nodes
     * @param poiArr
     */
    public static List<Node> parsePOINodes(JsonArray poiArr) {

        List<Node> pois = new ArrayList<>();

        JsonObject poiJsonObj, beaconJsonObj;
        Floor floor;
        List<IBeacon> beacons;
        Long beaconId;

        for (int i = 0; i < poiArr.size(); i++) {

            poiJsonObj = poiArr.get(i).getAsJsonObject();

            floor = Floor.find(Floor.class, "floor_id=?", poiJsonObj.get("floorID").getAsString()).get(0);
            beaconJsonObj = poiJsonObj.get("iBeacon").getAsJsonObject();
            beacons = IBeacon.find(
                    IBeacon.class,
                    "uuid = ? AND minor=? AND major=?",
                    String.valueOf(beaconJsonObj.get("uuid").getAsString()),
                    String.valueOf(beaconJsonObj.get("minor").getAsString()),
                    String.valueOf(beaconJsonObj.get("major").getAsString()));

            beaconId = beacons.isEmpty()? null: beacons.get(0).getId();

            pois.add(new Node(
                    poiJsonObj.get("id").getAsLong(),
                    poiJsonObj.get("x").getAsInt(),
                    poiJsonObj.get("y").getAsInt(),
                    Node.POI_TYPE,
                    null,
                    floor.getId(),
                    beaconId
            ));
            LogUtils.info(
                    MapEditorContentJSONParser.class,
                    "parsePOINodes", "Parsed POI: " + poiJsonObj.get("id").getAsString());
        }
        return pois;
    }

    /**
     * Parse POT nodes
     * @param potArr
     */
    public static List<Node> parsePOTNodes(JsonArray potArr) {

        List<Node> pots = new ArrayList<>();

        JsonObject potJsonObj, labelJsonObject;
        Floor floor;


        for (int i = 0; i < potArr.size(); i++) {

            potJsonObj = potArr.get(i).getAsJsonObject();

            floor = Floor.find(Floor.class, "floor_id=?", potJsonObj.get("floorID").getAsString()).get(0);
            labelJsonObject = potJsonObj.get("label").getAsJsonObject();

            pots.add(new Node(
                    potJsonObj.get("id").getAsLong(),
                    potJsonObj.get("x").getAsInt(),
                    potJsonObj.get("y").getAsInt(),
                    getType(labelJsonObject.get("label").getAsString()),
                    getSubType(labelJsonObject.get("label").getAsString()),
                    floor.getId(),
                    null
            ));
            LogUtils.info(MapEditorContentJSONParser.class, "parsePOTNodes", "Parsed POT: " + potJsonObj.get("id").getAsString());
        }
        return pots;
    }

    /**
     * Get node type
     * @param label
     * @return
     */
    private static String getType(String label) {

        switch (label.toUpperCase()){
            case "INTERSECTION":
            case "STAIRS":
            case "ELEVATOR":
                return Node.TRANSITION_TYPE;
            case "WASHROOM":
            case "EXIT":
            case "ENTRANCE":
            case "EMERGENCY_EXIT":
                return Node.SERVICE_TYPE;

        }
        return Node.POI_TYPE;
    }

    /**
     * Get node subtype
     * @param label
     * @return
     */
    private static String getSubType(String label) {

        switch (label.toUpperCase()){
            case "INTERSECTION":
                return Node.TRANSITION_INTERSECTION_SUBTYPE;
            case "STAIRS":
                return  Node.TRANSITION_STAIR_SUBTYPE;
            case "ELEVATOR":
                return Node.TRANSITION_ELEVATOR_SUBTYPE;
            case "WASHROOM":
                return Node.SERVICE_WASHROOM_SUBTYPE;
            case "EXIT":
                return Node.SERVICE_EXIT_SUBTYPE;
            case "ENTRANCE":
                return Node.SERVICE_ENTRANCE_SUBTYPE;
            case "EMERGENCY_EXIT":
                return Node.SERVICE_EMERGENCY_EXIT_SUBTYPE;

        }
        return null;
    }

    /**
     * Parse Storyline
     * @param storylineArr
     * @return
     */
    public static List<Storyline> parseStorylines(JsonArray storylineArr) {
        List<Storyline> storylines = new ArrayList<>();

        JsonObject slJsonObj;

        for (int i = 0; i < storylineArr.size(); i++) {

            slJsonObj = storylineArr.get(i).getAsJsonObject();

            storylines.add(new Storyline(
                    slJsonObj.get("id").getAsLong(),
                    slJsonObj.get("thumbnail").getAsString(),
                    slJsonObj.get("walkingTimeInMinutes").getAsInt(), //TODO to verify
                    slJsonObj.get("floorsCovered").getAsInt(), //TODO to verify
                    "#57B657" //TODO no color in json schema
            ));
            LogUtils.info(MapEditorContentJSONParser.class, "parseStorylines", "Parsed storyline: " + slJsonObj.get("id").getAsString());
        }
        return storylines;
    }

    /**
     * Parse Description of a POI or Storyline
     * @param arr
     * @param type Description type; 'p' for poi or 's' for storyline
     * @return
     */
    public static List<Description> parseDescriptions(JsonArray arr, String type) {

        List<Description> nodeDescriptions = new ArrayList<>();

        JsonObject titleJsonObj, descJsonObj, parentJsonObj;
        JsonArray titleJsonArr, descJsonArr;
        HashMap<String, String> titleMap = new HashMap<>();
        HashMap<String, String> descMap = new HashMap<>();

        for (int i = 0; i < arr.size(); i++) {

            parentJsonObj = arr.get(i).getAsJsonObject();

            titleJsonArr = parentJsonObj.get("title").getAsJsonArray();
            descJsonArr = parentJsonObj.get("description").getAsJsonArray();
            int titleArrSize = titleJsonArr.size();
            int descArrSize = descJsonArr.size();

            // Gather all title and descriptions
            for (int j = 0; j < Math.min(titleArrSize, descArrSize); j++) {

                titleJsonObj = titleJsonArr.get(j).getAsJsonObject();
                descJsonObj = descJsonArr.get(j).getAsJsonObject();

                titleMap.put(
                        titleJsonObj.get("language").getAsString(),
                        titleJsonObj.get("title").getAsString());
                descMap.put(
                        descJsonObj.get("language").getAsString(),
                        descJsonObj.get("description").getAsString());

            }

            for (String key: titleMap.keySet() ) {

                nodeDescriptions.add(new Description(
                        key,
                        titleMap.get(key),
                        descMap.get(key),
                        parentJsonObj.get("id").getAsLong(),
                        type
                ));

            }
            LogUtils.info(MapEditorContentJSONParser.class, "parseNodeDescriptions", "Parsed description for " + type + " owner type: " + parentJsonObj.get("id").getAsString());
        }
        return nodeDescriptions;
    }

    /**
     * Parse edges
     * @param edgesArr
     * @return
     */
    public static List<Edge> parseEdges(JsonArray edgesArr){
        List<Edge> edges = new ArrayList<>();

        JsonObject edgeJsonObj;

        for (int i = 0; i < edgesArr.size(); i++) {

            edgeJsonObj = edgesArr.get(i).getAsJsonObject();

            edges.add(new Edge(
                    edgeJsonObj.get("startNode").getAsLong(),
                    edgeJsonObj.get("endNode").getAsLong(),
                    edgeJsonObj.get("distance").getAsInt(),
                    null
            ));

            LogUtils.info(MapEditorContentJSONParser.class, "parseEdges", "Parsed edge: "
                    + edgeJsonObj.get("startNode").getAsString()
                    + " - " + edgeJsonObj.get("endNode").getAsString());
        }

        return edges;
    }

    /**
     * PArse Storyline Nodes
     * @param slArr
     * @return
     */
    public static List<StorylineNode> parseStorylineNodes(JsonArray slArr) {
        List<StorylineNode> nodes = new ArrayList<>();
        // TODO
        return nodes;
    }

    /**
     * Parse media content
     * @param poiArr
     * @return
     */
    public static List<ExpositionContent> parseMediaContent(JsonArray poiArr) {
        List<ExpositionContent> contents = new ArrayList<>();
        //TODO
        return contents;
    }
}
