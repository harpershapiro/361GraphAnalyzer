package graph;
import util.PriorityQueue;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class GraphAlgorithms {

	/**
	 * Returns the shortest paths between vertices in a graph.
	 * @param graph The graph to find paths for
	 * @return 2D array holding shortest paths
	 */
    public static int[][] floydWarshall(Graph<Integer> graph){
        int V = graph.numVertices();
        int INF = 99999;
        // did this as opposed to Integer.MAX_VALUE to avoid
        // spilling over to 0 when comparing distance

        //make a 2D array to hold the distances and initialize to infinite
        int[][] dist = new int[V][V];
        for (int[] row : dist) {
            Arrays.fill(row, INF);
        }

        // Since we have no weights, reinitialize distances
        // using existence of edges in graph
        int src = 0;
        int dest = 0;
        try {
            for (Integer v : graph.getVertices()) {
                dest = 0;
                for (Integer u : graph.getVertices()) {
                    if (graph.edgeExists(v,u)) {
                        dist[src][dest] = 1;
                    }
                    dest++;
                }
                src++;
            }
        } catch (Exception e){
            System.out.println("Invalid vertex encountered in Flloyd-Warshall.");
            return null;
        }
        // Generate shortest paths by comparing intermediate vertices
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                for (int k = 0; k < V; k++) {
                    if (dist[i][j] + dist[j][k] < dist[i][k]) {
                        dist[i][k] = dist[i][j] + dist[j][k];
                    }
                }
            }
        }
        return dist;
    }

    /**
     * Returns the shortest paths from source to other vertices in graph.
     * @param graph graph to analyze
     * @param source the starting vertex of the path
     * @return mapping of a vertex to its predecessor on shortest path from source
     */
    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
        int numVertices = graph.numVertices();
        PriorityQueue Q = new PriorityQueue();



        int[] dist = new int[numVertices+1]; //stores best distance from s to another node
        int[] prev = new int[numVertices+1]; //stores prev node on shortest path
        //int[] dist = new int[graph.numVertices()];
        //int[] prev = new int[graph.numVertices()];

        //Set all dists to infinity, all prevs to null
        for(Integer v : graph.getVertices()){
            //3System.out.println(v);
            dist[v]=Integer.MAX_VALUE;
            prev[v]=-1; //-1 represents null (no path predecessor for vertex v yet)
        }

        dist[source]=0; //starting node

        for(Integer v : graph.getVertices()) {
            //System.out.println("Adding vertex " + v + " with priority " + dist[v]);
            Q.push(dist[v],v);
        }
//
        Pair u; //stores current vertex
        Integer uElement;

        while(!Q.isEmpty()){
            u = Q.pop();  //shortest distance to u
            uElement = (Integer)u.element;


            try {
                for (Integer v : graph.getNeighbors(uElement)) {
                    Integer alt;
                    //do some error checking with MAX_VALUE
                    if(dist[uElement]!=Integer.MAX_VALUE){
                        alt = dist[uElement] + 1; // Is this better than what we've found so far? (weight from u to v is 1)
                    } else {
                        alt = dist[uElement];
                    }
                    if(alt < dist[v]){
                      //System.out.println("Alt was better at" + v);
                      dist[v]=alt;
                      prev[v]=uElement;
                      //System.out.println("Changing priority to " + alt);
                      Q.changePriority(alt, v);
                  }
                }
            } catch (Exception e){
                System.out.println("Invalid vertex encountered in Dijkstra's.");
                return null;
            }
        }
        return prev;
    }

    public static void main(String[] args){
        //test algs
        Graph<Integer> dtest = new Graph<Integer>();
        dtest.addVertex(1);
        dtest.addVertex(2);
        dtest.addVertex(3);
        dtest.addVertex(4);

        dtest.addEdge(1,2);
        dtest.addEdge(2,3);
        dtest.addEdge(3,4);
        dtest.addEdge(1,4);
        //dtest.addEdge(16,8);
        //dtest.addEdge(8,12);

        int[] prev = dijkstrasAlgorithm(dtest,1);
        for(Integer v : dtest.getVertices()){
            System.out.println("vertex: " + v + " previous: " + prev[v]);
        }

        int[][] dists = floydWarshall(dtest);
        System.out.println("Flloyd-Warshall shortest paths:");
        for (Integer v : dtest.getVertices()) {
            System.out.print("| " + v + " ");
        } System.out.print("|\n");
        for (int[] row : dists) {
            System.out.println(Arrays.toString(row));
        }
    }
}
