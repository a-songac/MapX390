package soen390.mapx.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Exhibition Content model
 */
public class ExpositionContent extends SugarRecord {

    @Ignore
    public static final String AUDIO_TYPE = "a";
    @Ignore
    public static final String VIDEO_TYPE = "v";
    @Ignore
    public static final String TEXT_TYPE = "t";
    @Ignore
    public static final String IMAGE_TYPE = "i";


    private Long nodeId;
    private String language;
    private String type;
    private String title;
    /**
     * When null, it is generic info not associated to any storyline
     */
    private Long storylineId;

    /**
     * Content is relative to type:
     * if TEXT_TYPE it will be text, otherwise a link towards the resource
     */
    private String content;

    /**
     * Default Constructor
     */
    public ExpositionContent() {}


    /**
     * Constructor
     * @param nodeId
     * @param language
     * @param type
     * @param storylineId
     * @param title
     * @param content
     */
    public ExpositionContent(Long nodeId, String language, String type, Long storylineId, String title, String content) {
        this.nodeId = nodeId;
        this.language = language;
        this.type = type;
        this.title = title;
        this.content = content;
        this.storylineId = storylineId;
    }

    public String getContent() {
        return content;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }

    public Long getStorylineId() {
        return storylineId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAudio() {
        return type.equals(AUDIO_TYPE);
    }

    public boolean isVideo() {
        return type.equals(VIDEO_TYPE);
    }

    public boolean isText() {
        return type.equals(TEXT_TYPE);
    }

    public boolean isImage() {
        return type.equals(IMAGE_TYPE);
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStorylineId(Long storylineId) {
        this.storylineId = storylineId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
