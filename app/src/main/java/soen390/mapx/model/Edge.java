package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Edges model
 */
public class Edge extends SugarRecord{

    private long p1Id;
    private long p2Id;
    private int weight;

    /**
     * Constructor
     * @param p1Id : node
     * @param p2Id : node
     * @param weight : weight
     */
    public Edge(long p1Id, long p2Id, int weight) {
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        this.weight = weight;
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

}
