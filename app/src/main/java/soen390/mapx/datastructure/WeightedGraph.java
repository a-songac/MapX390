package soen390.mapx.datastructure;

public class WeightedGraph {

    private int[][] edges;
    private int size;

    public WeightedGraph(int size) {
        edges = new int[size][size];
        this.size = size;
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
