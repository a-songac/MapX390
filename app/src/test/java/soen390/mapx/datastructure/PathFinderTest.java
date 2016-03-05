package soen390.mapx.datastructure;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.model.Edge;

public class PathFinderTest {
//1. create a graph
//2. add the edges
//3. compute the shortest path with pathfinder
//4. call the shortestpath
//5. check the answer

    private int[][] edges;
    private int size;
    private WeightedGraph weightedGraph;

    @Before
    public void initialize() {
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge(0l, 7l, 4, 2l));
        edges.add(new Edge(7l, 6l, 4, 2l));
        edges.add(new Edge(0l, 3l, 2, 2l));
        edges.add(new Edge(3l, 4l, 2, 2l));
        edges.add(new Edge(4l, 5l, 2, 2l));
        edges.add(new Edge(5l, 6l, 2, 2l));
        edges.add(new Edge(0l, 2l, 6, 2l));
        edges.add(new Edge(2l, 6l, 1, 2l));
        edges.add(new Edge(0l, 1l, 3, 2l));
        edges.add(new Edge(1l, 6l, 4, 2l));

        weightedGraph = WeightedGraph.getInstance(edges, 8);
    }

    @Test
    public void getWeightIsNotNull(){
        int weight = weightedGraph.getWeight(0,7);
        Assert.assertEquals(4, weight);
    }

    @Test
    public void computeShortestPathIsNotNull(){
        int[] arrayPath;

        ArrayList<Integer> expectedShortestPath = new ArrayList<Integer>();
        arrayPath = PathFinder.computeShortestPath(weightedGraph,0);

        expectedShortestPath.add(0, 0);
        expectedShortestPath.add(1, 3);
        expectedShortestPath.add(2, 4);

        Assert.assertEquals(expectedShortestPath, PathFinder.getShortestPath(arrayPath, 0, 4));
    }

    @Test
    public void computeShortestPathToItSelf(){
        int[] arrayPath;

        ArrayList<Integer> expectedShortestPath = new ArrayList<Integer>();
        arrayPath = PathFinder.computeShortestPath(weightedGraph,2);

        expectedShortestPath.add(0, 2);

        Assert.assertEquals(expectedShortestPath, PathFinder.getShortestPath(arrayPath, 2, 2));
    }

    @Test
    public void computeShortestPathBackward(){
        int[] arrayPathF;
        int[] arrayPathB;

        ArrayList<Integer> expectedShortestPathForward = new ArrayList<Integer>();
        ArrayList<Integer> expectedShortestPathBackward = new ArrayList<Integer>();
        arrayPathF = PathFinder.computeShortestPath(weightedGraph,5);
        arrayPathB = PathFinder.computeShortestPath(weightedGraph,0);

        expectedShortestPathForward.add(0, 5);
        expectedShortestPathForward.add(1, 4);
        expectedShortestPathForward.add(2, 3);
        expectedShortestPathForward.add(3, 0);

        expectedShortestPathBackward.add(0, 0);
        expectedShortestPathBackward.add(1, 3);
        expectedShortestPathBackward.add(2, 4);
        expectedShortestPathBackward.add(3, 5);


        Assert.assertEquals(expectedShortestPathForward, PathFinder.getShortestPath(arrayPathF, 5, 0));
        Assert.assertEquals(expectedShortestPathBackward, PathFinder.getShortestPath(arrayPathB, 0, 5));
    }

    @Test
    public void computeShortestPathWithTwoSolution() {
        int[] arrayPath;

        ArrayList<Integer> expectedShortestPath = new ArrayList<Integer>();
        arrayPath = PathFinder.computeShortestPath(weightedGraph, 7);

        expectedShortestPath.add(0, 7);
        expectedShortestPath.add(1, 0);
        expectedShortestPath.add(2, 3);
        expectedShortestPath.add(3, 4);

        Assert.assertEquals(expectedShortestPath, PathFinder.getShortestPath(arrayPath, 7, 4));
    }
}
