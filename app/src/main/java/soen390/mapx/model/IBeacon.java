package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * IBeacon model
 */
public class IBeacon extends SugarRecord {

    private String UUID;
    private int minor;
    private int major;

    /**
     * Default constructor
     */
    public IBeacon(){}

    /**
     * Constructor
     * @param UUID
     * @param minor
     * @param major
     */
    public IBeacon(String UUID, int minor, int major) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
    }

    public String getUUID() {
        return UUID;
    }

    public int getMinor() {
        return minor;
    }

    public int getMajor() {
        return major;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setMajor(int major) {
        this.major = major;
    }
}
