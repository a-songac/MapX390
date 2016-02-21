package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Floor Plan
 */
public class Floor extends SugarRecord {

    private String floorId;
    private String imageFilePath;
    private int imageWidth;
    private int imageHeight;
    private int floorOrdering;

    /**
     * Default constructor
     */
    public Floor(){}

    /**
     * Constructor
     * @param floorId
     * @param imageFilePath
     * @param imageWidth
     * @param imageHeight
     */
    public Floor(String floorId, String imageFilePath, int imageWidth, int imageHeight) {
        this.floorId = floorId;
        this.imageFilePath = imageFilePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getFloorOrdering() {
        return floorOrdering;
    }

    public void setFloorOrdering(int floorOrdering) {
        this.floorOrdering = floorOrdering;
    }
}
