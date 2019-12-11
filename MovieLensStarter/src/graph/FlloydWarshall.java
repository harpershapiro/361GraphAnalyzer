package graph;
import util.PriorityQueue;
import util.Pair;
import java.util.HashMap;

import graph.Graph;

import java.util.Arrays;

public class FlloydWarshall {
	
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
						//dist[dest][src] = 1;
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
	
	public static void main(String[] args) {
		 Graph<Integer> dtest = new Graph<Integer>();
		 	System.out.println("Adding vertices...");
	        dtest.addVertex(5);
	        dtest.addVertex(16);
	        dtest.addVertex(12);
	        dtest.addVertex(8);
	        System.out.println(dtest.getVertices());

	        System.out.println("Adding edges...");
	        dtest.addEdge(5,16);
	        dtest.addEdge(16,12);
	        dtest.addEdge(12,8);
	        dtest.addEdge(5,8);
	        System.out.println("Outgoing Edges (direction is ignored by algorithm):");
		try {
			System.out.println("16:" + dtest.getNeighbors(16));
			System.out.println("5:" + dtest.getNeighbors(5));
			System.out.println("8:" + dtest.getNeighbors(8));
			System.out.println("12:" + dtest.getNeighbors(12));
		} catch (Exception e) {
			e.printStackTrace();
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
