package soen390.mapx.model;

import com.orm.SugarRecord;

import java.util.List;

import soen390.mapx.helper.PreferenceHelper;

/**
 * Storyline model
 */
public class Storyline extends SugarRecord {

    private String title;
    private String description;
    private String imagePath;

    /**
     * Color in hex format: #RRGGBB
     */
    private String color;

    /**
     * Get storyline description based on the language
     * @return
     */
    private StorylineDescription getStorylineDescription() {
        String language = PreferenceHelper.getInstance().getLanguagePreference();

        List<StorylineDescription> storylineDescriptionList = StorylineDescription.find(
                StorylineDescription.class,
                "storyline_id = ? AND language = ?",
                String.valueOf(this.getId()), language);

        if (!storylineDescriptionList.isEmpty()) {
            StorylineDescription storylineDescription = storylineDescriptionList.get(0);
            if (null != storylineDescription) {
                return storylineDescription;
            }
        }
        return null;
    }

    /**
     * Get title of the storyline based on the language
     * @return
     */
    public String getTitleLanguageSupport() {

        StorylineDescription sd = getStorylineDescription();
        if (null != sd) {
            return sd.getTitle();
        }
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get Description based on the language
     * @return
     */
    public String getDescriptionLanguageSupport() {

        StorylineDescription sd = getStorylineDescription();
        if (null != sd) {
            return sd.getDescription();
        }
        return description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Node>  getPath() {

        String[] whereArgs = {this.getId().toString()};
//        List<StorylineNode> path = StorylineNode.find(StorylineNode.class, "storyline_id = ?", whereArgs, null, "position ASC", null);
        String query = "SELECT N.* " +
                "FROM Node N, StorylineNode SN " +
                "WHERE N.id = SN.node_id " +
                "AND storyline_id = ? " +
                "ORDER BY SN.position ASC";

        return StorylineNode.findWithQuery(Node.class, query, getId().toString());
    }
}
