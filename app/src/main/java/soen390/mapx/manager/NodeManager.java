package soen390.mapx.manager;

import com.estimote.sdk.Beacon;

import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.helper.NotificationHelper;
import soen390.mapx.model.IBeacon;
import soen390.mapx.model.Node;

/**
 * Class to manage all nodes
 */
public class NodeManager {

    private static List<Node> pois = null;

    /**
     * Link detected ibeacon with actualPOI node
     * @param beacon
     */
    public static void reachedPOI(Beacon beacon) {

        List<IBeacon> iBeacons = IBeacon.find(IBeacon.class,
                "major=? AND minor=?",
                String.valueOf(beacon.getMajor()), String.valueOf(beacon.getMinor()));

        if (!iBeacons.isEmpty()) {

            IBeacon reachedBeacon = iBeacons.get(0);
            LogUtils.info(
                    NodeManager.class,
                    "reachedPOI",
                    "iBeacon detected exists in db; ibeacon id: " + reachedBeacon.getId());

            List<Node> nodes = Node.find(Node.class, "type=? AND i_beacon_id=?",
                    Node.POI_TYPE,
                    String.valueOf(reachedBeacon.getId()));

            if (!nodes.isEmpty()) {

                Node reachedPOI = nodes.get(0);
                LogUtils.info(
                        NodeManager.class,
                        "reachedPOI",
                        "Existing iBeacon detected linked to node: "
                                + reachedPOI.getId() + " (" + reachedPOI.getTitle() + ")");

                NotificationHelper.getInstance().showPOIReachedNotification(reachedPOI);
                MapManager.reachPOI(reachedPOI);


            } else {
                LogUtils.error(
                        NodeManager.class,
                        "reachedPOI",
                        "Existing iBeacon detected linked to no node");
            }

        } else {
            LogUtils.warn(
                    NodeManager.class,
                    "reachedPOI",
                    "ibeacon detected does not exist in db; beacon major: " + beacon.getMajor()
                    + " beacon minor: " + beacon.getMinor()
                    + " beacon UUID: " + beacon.getProximityUUID());
        }
    }

    /**
     * Get all poi nodes
     * @return
     */
    public static List<Node> getAllPOIs() {

        if (null == pois) {
            String[] whereArgs = {Node.POI_TYPE};
            pois = Node.find(Node.class, "type = ?", whereArgs, null, "id ASC", null);
        }
        return pois;

    }

    
}
