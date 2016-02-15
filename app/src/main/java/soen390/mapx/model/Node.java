package soen390.mapx.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

import soen390.mapx.helper.PreferenceHelper;

/**
 * Node model.
 */

public class Node extends SugarRecord {

    @Ignore
    public static final String POI_TYPE = ".{1}";
    @Ignore
    public static final String SERVICE_TYPE = "s";
    @Ignore
    public static final String TRANSITION_TYPE = "t";
    @Ignore
    public static final String TRANSITION_STAIR_SUBTYPE = "s";
    @Ignore
    public static final String TRANSITION_INTERSECTION_SUBTYPE = "i";
    @Ignore
    public static final String TRANSITION_ELEVATOR_SUBTYPE = "e";

    private String title;
    private int xCoord;
    private int yCoord;

    /**
     * Discriminant for type of the Node between Transition point, Service point or Exhibition
     * Is a String instead of char because JSONObject encodes char to ascii
     */
    private String type;

    /**
     * Subtype relevant to transition (i (intersection), s (stairs), e (elevator))
     * and to Service point (w (washrooms), i (information service))
     * Null otherwise
     */
    private String subType;
    private Long floorId;
    private Long iBeaconId;
    private Long qrId;

    public Node(){}

    public Node(String title, int xCoord, int yCoord, String type, String subType, Long floorId, Long iBeaconId, Long qrId) {
        this.title = title;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.type = type;
        this.subType = subType;
        this.floorId = floorId;
        this.iBeaconId = iBeaconId;
        this.qrId = qrId;
    }

    /**
     * Get node description based on the language
     * @return
     */
    private NodeDescription getNodeDescription() {
        String language = PreferenceHelper.getInstance().getLanguagePreference();

        List<NodeDescription> nodeDescriptionList = NodeDescription.find(
                NodeDescription.class,
                "node_id = ? AND language = ?",
                String.valueOf(this.getId()), language);

        if (!nodeDescriptionList.isEmpty()) {
            NodeDescription nodeDescription = nodeDescriptionList.get(0);
            if (null != nodeDescription) {
                return nodeDescription;
            }
        }
        return null;
    }

    /**
     * Get title of the node based on the language
     * @return
     */
    public String getTitleLanguageSupport() {

        NodeDescription nd = getNodeDescription();
        if (null != nd) {
            return nd.getTitle();
        }
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public long getiBeaconId() {
        return iBeaconId;
    }

    public void setiBeaconId(long iBeaconId) {
        this.iBeaconId = iBeaconId;
    }

    public Long getQrId() {
        return qrId;
    }

    public void setQrId(Long qrId) {
        this.qrId = qrId;
    }

    public boolean isPointOfInterest() {
        return type.equals(POI_TYPE);
    }

    public boolean isTransitionPoint() {
        return type.equals(TRANSITION_TYPE);
    }

    public boolean isServicePoint() {
        return type.equals(SERVICE_TYPE);
    }

    public boolean isStairsTransitionPoint() {
        return type.equals(TRANSITION_TYPE) && subType.equals(TRANSITION_STAIR_SUBTYPE);
    }

    public boolean isElevatorTransitionPoint() {
        return type.equals(TRANSITION_TYPE) && subType.equals(TRANSITION_ELEVATOR_SUBTYPE);
    }

    public boolean isIntersectionTransitionPoint() {
        return type.equals(TRANSITION_TYPE) && subType.equals(TRANSITION_INTERSECTION_SUBTYPE);
    }
}
