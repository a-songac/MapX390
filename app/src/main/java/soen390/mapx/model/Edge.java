package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Edges model
 */
public class Edge extends SugarRecord{

    private Long p1Id;
    private Long p2Id;
    private int weight;
    private Long floorId;

    /**
     * Default constructor
     */
    public Edge(){}

    /**
     * Constructor
     * @param p1Id
     * @param p2Id
     * @param weight
     * @param floorId
     */
    public Edge(Long p1Id, Long p2Id, int weight, Long floorId) {
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        this.weight = weight;
        this.floorId = floorId;
    }

    public long getP1Id() {
        return p1Id;
    }


    public long getP2Id() {
        return p2Id;
    }

    public int getWeight() {
        return weight;
    }

    public Long getFloorId() {
        return floorId;
    }
}
