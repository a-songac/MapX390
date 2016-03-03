package soen390.mapx.datastructure;

import java.util.List;

import soen390.mapx.model.Edge;
import soen390.mapx.model.Node;

public class WeightedGraph {

    private int[][] edges;
    private int size;
    private static WeightedGraph instance;

    private WeightedGraph() {
        List<Edge> list = Edge.listAll(Edge.class);
        long numOfNodes = Node.count(Node.class);
        edges = new int[(int)numOfNodes][(int)numOfNodes];
        this.size = (int)numOfNodes;

        for (Edge e : list) {
            addEdge((int)(e.getP1Id()), (int)(e.getP2Id()), e.getWeight());
            addEdge((int)(e.getP2Id()), (int)(e.getP1Id()), e.getWeight());
        }
    }

    public static WeightedGraph getInstance() {
        if (instance == null) {
            instance =  new WeightedGraph();
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
