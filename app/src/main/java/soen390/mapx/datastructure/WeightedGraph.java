package soen390.mapx.datastructure;

import java.util.List;

import soen390.mapx.model.Edge;

public class WeightedGraph {

    private int[][] edges;
    private int size;
    private static WeightedGraph instance;

    private WeightedGraph(List<Edge> edges, long size) {
        this.edges = new int[(int)size][(int)size];
        this.size = (int)size;

        for (Edge e : edges) {
            addEdge((int)(e.getP1Id()), (int)(e.getP2Id()), e.getWeight());
            addEdge((int)(e.getP2Id()), (int)(e.getP1Id()), e.getWeight());
        }
    }

    public static WeightedGraph getInstance(List<Edge> edges, long size) {
        if (instance == null) {
            instance =  new WeightedGraph(edges, size);
        }
        return instance;
    }

    public int getSize() {
        return this.size;
    }

    public void addEdge(int source, int destination, int weight) {
        edges[source][destination] = weight;
    }

    public int getWeight(int source, int destination) {
        return edges[source][destination];
    }

    public int[] neighbors(int POI) {
        int count = 0;
        for (int i = 0; i < edges[POI].length; i++) {
            if (edges[POI][i] > 0) {
                count++;
            }
        }
        int neighbors[] = new int[count];
        count = 0;
        for (int i = 0; i < edges[POI].length; i++) {
            if (edges[POI][i] > 0) {
                neighbors[count++] = i;
            }
        }
        return neighbors;
    }
}
