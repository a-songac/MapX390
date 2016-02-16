package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Node description for diff languages
 */
public class NodeDescription extends SugarRecord {

    /**
     * Language name with two letters abbreviation (see ConstantsHelper class)
     */
    private String language;
    private String title;
    private Long nodeId;

    public NodeDescription(String language, String title, Long nodeId) {
        this.language = language;
        this.title = title;
        this.nodeId = nodeId;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public Long getNodeId() {
        return nodeId;
    }

}
