package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Storyline POI association class
 */
public class StorylineNode extends SugarRecord {

    private Long storylineId;
    private Long nodeId;
    private int position;

    /**
     * Constructor
     * @param storylineId
     * @param nodeId
     * @param position
     */
    public StorylineNode(Long storylineId, Long nodeId, int position) {
        this.storylineId = storylineId;
        this.nodeId = nodeId;
        this.position = position;
    }

    public Long getStorylineId() {
        return storylineId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public int getPosition() {
        return position;
    }
}
