package soen390.mapx.webapp;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import soen390.mapx.datastructure.PathFinder;
import soen390.mapx.datastructure.WeightedGraph;

public class PathFinderTest {
//1. create a graph
//2. add the edges
//3. compute the shortest path with pathfinder
//4. call the shortestpath
//4. check the answer

    private int[][] edges;
    private int size;
    private WeightedGraph weightedGraph;
    private PathFinder pathFinder;

    @Before
    public void initialize() {

        weightedGraph = new WeightedGraph(8);
        weightedGraph.addEdge(0,7,4);
        weightedGraph.addEdge(7,0,4);

        weightedGraph.addEdge(7,6,4);
        weightedGraph.addEdge(6,7,4);

        weightedGraph.addEdge(0,3,2);
        weightedGraph.addEdge(3,0,2);

        weightedGraph.addEdge(3,4,2);
        weightedGraph.addEdge(4,3,2);

        weightedGraph.addEdge(4,5,2);
        weightedGraph.addEdge(5,4,2);

        weightedGraph.addEdge(5,6,2);
        weightedGraph.addEdge(6,5,2);

        weightedGraph.addEdge(0,2,6);
        weightedGraph.addEdge(2,0,6);


        weightedGraph.addEdge(2,6,1);
        weightedGraph.addEdge(6,2,1);

        weightedGraph.addEdge(0,1,3);
        weightedGraph.addEdge(1,0,3);

        weightedGraph.addEdge(1,6,4);
        weightedGraph.addEdge(6,1,4);


        pathFinder = new PathFinder();


    }

    @Test
    public void getWeightIsNotNull(){
        int weight = weightedGraph.getWeight(0,7);
                 Assert.assertEquals(4, weight);
    }

    @Test
    public void computeShortestPathIsNotNull(){
        int[] arrayPath;
        //int i=0;
        ArrayList<Integer> as = new ArrayList<Integer>();
        arrayPath = PathFinder.computeShortestPath(weightedGraph,0);
//        for(i=0; i<arrayPath.length; i++){
//            System.out.println("index: " + i + " " + arrayPath[i]);
//        }
        as.add(0,0);
        as.add(1,3);
        as.add(2,4);

       Assert.assertEquals(as, pathFinder.getShortestPath(arrayPath, 0, 4));


    }

}
