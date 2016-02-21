package soen390.mapx.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.PreferenceHelper;

/**
 * Node model.
 */

public class Node extends SugarRecord {

    @Ignore
    public static final String POI_TYPE = "p";
    @Ignore
    public static final String SERVICE_TYPE = "s";
    @Ignore
    public static final String SERVICE_WASHROOM_SUBTYPE = "w";
    @Ignore
    public static final String SERVICE_INFO_SUBTYPE = "info";
    @Ignore
    public static final String SERVICE_EXIT_SUBTYPE = "exit";
    @Ignore
    public static final String SERVICE_EMERGENCY_EXIT_SUBTYPE = "emergency";
    @Ignore
    public static final String SERVICE_ENTRANCE_SUBTYPE = "entrance";
    @Ignore
    public static final String TRANSITION_TYPE = "t";
    @Ignore
    public static final String TRANSITION_STAIR_SUBTYPE = "s";
    @Ignore
    public static final String TRANSITION_INTERSECTION_SUBTYPE = "i";
    @Ignore
    public static final String TRANSITION_ELEVATOR_SUBTYPE = "e";

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

    /**
     * Default constructor
     */
    public Node(){}

    /**
     * Constructor
     * @param id
     * @param xCoord
     * @param yCoord
     * @param type
     * @param subType
     * @param floorId
     * @param iBeaconId
     */
    public Node(Long id, int xCoord, int yCoord, String type, String subType, Long floorId, Long iBeaconId) {
        setId(id);
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.type = type;
        this.subType = subType;
        this.floorId = floorId;
        this.iBeaconId = iBeaconId;
    }


    /**
     * Get title or description of node
     * @param isTitle
     * @return
     */
    private String getDescription(boolean isTitle) {

        Description ndDesc =
                Description.getDescription(
                        PreferenceHelper.getInstance().getLanguagePreference(),
                        Description.POI_DESC,
                        this.getId()
                        );

        if (null != ndDesc) {
            return isTitle? ndDesc.getTitle(): ndDesc.getDescription();
        }

        ndDesc = Description.getDescription(
                        ConstantsHelper.PREF_LANGUAGE_ENGLISH,
                        Description.POI_DESC,
                        this.getId()
                );
        if (null != ndDesc)
            return isTitle? ndDesc.getTitle(): ndDesc.getDescription();

        return null;

    }

    /**
     * Get title of the node based on the language
     * @return
     */
    public String getTitle() {
        return  getDescription(true);
    }

    /**
     * Get description of the node based on the language
     * @return
     */
    public String getDescription() {
        return  getDescription(false);
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
