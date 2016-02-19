package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * IBeacon model
 */
public class IBeacon extends SugarRecord {

    private String UUID;
    private String minor;
    private String major;

    /**
     * Constructor
     * @param UUID
     * @param minor
     * @param major
     */
    public IBeacon(String UUID, String minor, String major) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
    }

    public String getUUID() {
        return UUID;
    }

    public String getMinor() {
        return minor;
    }

    public String getMajor() {
        return major;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
