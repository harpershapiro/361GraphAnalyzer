package src.graph;
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
				//optimization: ignore nodes that aren't connected by an edge
				if (dist[i][j] == INF) continue;
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
	        dtest.addVertex(5);
	        dtest.addVertex(16);
	        dtest.addVertex(12);
	        dtest.addVertex(8);

	        dtest.addEdge(5,16);
	        dtest.addEdge(16,12);
	        dtest.addEdge(12,8);
	        dtest.addEdge(5,8);
	        
	        int[][] dists = floydWarshall(dtest);
	        System.out.println(Arrays.toString(dists));
	}
}
