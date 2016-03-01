package soen390.mapx.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Node description for diff languages
 */
public class Description extends SugarRecord {

    @Ignore
    public static final String POI_DESC = "p";
    @Ignore
    public static final String STORYLINE_DESC = "s";

    /**
     * Language name with two letters abbreviation (see ConstantsHelper class)
     */
    private String language;
    private String title;
    private String description;
    private Long ownerId;
    /**
     * Either storyline or POI description
     */
    private String type;

    public Description(){}

    /**
     * Constructor
     * @param language
     * @param title
     * @param description
     * @param ownerId
     * @param type
     */
    public Description(String language, String title, String description, Long ownerId, String type) {
        this.language = language;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get description
     * @param language
     * @param type : storyline or poi
     * @param id : Owner id, either storyline or node
     * @return
     */
    public static Description getDescription(String language, String type, long id) {

        List<Description> nodeDescriptionList = Description.find(
                Description.class,
                "owner_id = ? AND language = ? AND type = ?",
                String.valueOf(id), language, type);

        if (!nodeDescriptionList.isEmpty()) {
            Description nodeDescription = nodeDescriptionList.get(0);
            if (null != nodeDescription) {
                return nodeDescription;
            }
        }
        return null;
    }
}
