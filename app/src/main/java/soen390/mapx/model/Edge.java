package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * Edges model
 */
public class Edge extends SugarRecord{

    private long p1Id;
    private long p2Id;
    private int weight;

    public long getP1Id() {
        return p1Id;
    }

    public void setP1Id(long p1Id) {
        this.p1Id = p1Id;
    }

    public long getP2Id() {
        return p2Id;
    }

    public void setP2Id(long p2Id) {
        this.p2Id = p2Id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
