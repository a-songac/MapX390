package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Floor Plan
 */
public class Floor extends SugarRecord {

    private String floorNum;
    private String imageFilePath;
    private int imageWidth;
    private int imageHeight;
    private int order;

    /**
     * Constructor
     * @param floorNum
     * @param imageFilePath
     * @param imageWidth
     * @param imageHeight
     */
    public Floor(String floorNum, String imageFilePath, int imageWidth, int imageHeight) {
        this.floorNum = floorNum;
        this.imageFilePath = imageFilePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
