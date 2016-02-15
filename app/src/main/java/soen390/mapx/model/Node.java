package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Point of interest model.
 */

public class Node extends SugarRecord {

    private final char POI_TYPE = 'p';
    private final char SERVICE_TYPE = 's';
    private final char TRANSITION_TYPE = 't';

    private final char TRANSITION_STAIR_SUBTYPE = 's';
    private final char TRANSITION_INTERSECTION_SUBTYPE = 'i';
    private final char TRANSITION_ELEVATOR_SUBTYPE = 'e';

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

    private long floorId;

    private long iBeaconId;

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
}
