package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Point of interest model.
 */

public class POI extends SugarRecord {

    private String name;
    private int xCoord;
    private int yCoord;

    /**
     * Discriminant for type of the POI between Transition point, Service point or Exhibition
     */
    private char type;

    /**
     * Subtype relevant to transition (i (intersection), s (stairs), e (elevator))
     * and to Service point (w (washrooms), i (information service))
     * Null otherwise
     */
    private char subType;


}
