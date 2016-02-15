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
    public static String IMAGE_TYPE = "i";


    private Long nodeId;
    private String language;
    private String type;
    private boolean qrTriggered;
    private String title;

    /**
     * Content is relative to type:
     * if TEXT_TYPE it will be text, otherwise a link towards the resource
     */
    private String content;

    public ExpositionContent(Long nodeId, String language, String type, boolean qrTriggered, String title, String content) {
        this.nodeId = nodeId;
        this.language = language;
        this.type = type;
        this.qrTriggered = qrTriggered;
        this.title = title;
        this.content = content;
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

    public boolean isQrTriggered() {
        return qrTriggered;
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
}
