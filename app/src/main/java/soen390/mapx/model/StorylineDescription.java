package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Storyline Description for diff languages
 */
public class StorylineDescription extends SugarRecord {

    /**
     * Language name with two letters abbreviation (see ConstantsHelper class)
     */
    private String language;
    private String title;
    private String description;
    private Long storylineId;

    /**
     * Constructor
     * @param language
     * @param title
     * @param description
     * @param storylineId
     */
    public StorylineDescription(String language, String title, String description, Long storylineId) {
        this.language = language;
        this.title = title;
        this.description = description;
        this.storylineId = storylineId;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getStorylineId() {
        return storylineId;
    }

}
