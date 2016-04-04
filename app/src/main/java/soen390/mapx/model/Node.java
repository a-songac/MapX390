package soen390.mapx.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.PreferenceHelper;

/**
 * Node model.
 */

public class Node extends SugarRecord implements Comparable<Node> {

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
    private String floorId;
    private Long iBeaconId;
    private Long qrId;

    @Ignore
    private Description description = null;

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
    public Node(Long id, int xCoord, int yCoord, String type, String subType, String floorId, Long iBeaconId) {
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

        if (null == description || !description.getLanguage().equals(PreferenceHelper.getInstance().getLanguagePreference())) {
            description =
                    Description.getDescription(
                            PreferenceHelper.getInstance().getLanguagePreference(),
                            Description.POI_DESC,
                            this.getId()
                    );

            if (null == description) {

                description = Description.getDescription(
                        ConstantsHelper.PREF_LANGUAGE_ENGLISH,
                        Description.POI_DESC,
                        this.getId()
                );
            }
        }

        if (null != description)
            return isTitle? description.getTitle(): description.getDescription();

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

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
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

    /**
     * Get exposition contents
     * @param storylineId -1 if no storyline (generic content)
     * @param type Between the constants: ExpositionContent.AUDIO_TYPE, ExpositionContent.VIDEO_TYPE,
     *             ExpositionContent.IMAGE_TYPE and ExpositionContent.TEXT_TYPE
     * @return
     */
    public List<ExpositionContent> getContent(Long storylineId, String type) {

        String language = PreferenceHelper.getInstance().getLanguagePreference();
        List<ExpositionContent> expositionContents = new ArrayList<>();

        if (this.type.equals(POI_TYPE)) {

            expositionContents = ExpositionContent.find(
                    ExpositionContent.class,
                    "node_id=? AND language=? AND type=? AND storyline_id=?",
                    String.valueOf(this.getId()),
                    language,
                    type,
                    String.valueOf(storylineId)
                    );
            if (expositionContents.isEmpty() && !language.equals(ConstantsHelper.PREF_LANGUAGE_ENGLISH)) {
                expositionContents = ExpositionContent.find(
                        ExpositionContent.class,
                        "node_id=? AND language=? AND type=? AND storyline_id=?",
                        String.valueOf(this.getId()),
                        ConstantsHelper.PREF_LANGUAGE_ENGLISH,
                        type,
                        String.valueOf(storylineId)
                );
            }
        }
        return expositionContents;

    }

    @Override
    public int compareTo(Node another) {
        return this.getTitle().compareTo(another.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return this.getTitle().equals(((Node) o).getTitle());
        }
        return false;
    }
}
