package soen390.mapx.datastructure;

public class PathFinder {

    public static int[] computeShortestPath(WeightedGraph g, long source) {
        final int[] dist = new int[g.getSize()];
        final int[] pred = new int[g.getSize()];
        final boolean[] visited = new boolean[g.getSize()];

        // Set all initial distances from source to a very large number
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        // Set distance from source to source to 0
        dist[(int)source] = 0;

        // Find shortest path from source to every possible node
        for (int i = 0; i < dist.length; i++) {
            int next = shortestDistanceNotVisited(dist, visited);
            if (next == -1) continue;
            visited[next] = true;

            // Find shortest distance from a node to its neighbors
            final int[] neighbors = g.neighbors(next);
            for (int j = 0; j < neighbors.length; j++) {
                int neighbor = neighbors[j];
                int distance = dist[next] + g.getWeight(next, neighbor);
                if (dist[neighbor] > distance) {
                    dist[neighbor] = distance;
                    pred[neighbor] = next;
                }
            }
        }
        return pred;
    }

    // Find which node to visit next
    private static int shortestDistanceNotVisited(int[] dist, boolean[] visited) {
        int shortestDistance = Integer.MAX_VALUE;
        int nodeToVisit = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < shortestDistance) {
                nodeToVisit = i;
                shortestDistance = dist[i];
            }
        }
        return nodeToVisit;
    }
}
