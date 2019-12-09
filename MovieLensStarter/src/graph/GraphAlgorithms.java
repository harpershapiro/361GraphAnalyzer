package graph;
import util.PriorityQueue;
import util.Pair;
import java.util.HashMap;


public class GraphAlgorithms {
	// FILL IN

    public static int[][] floydWarshall(Graph<Integer> graph){

//        Q // a minimum priority queue
//                dist // distance from s, initialize to infinity
//        prev // previous node in the MST, initialize to null
//
//        dist[r] = 0 //some randomly chosen node in the graph
//        for(v in V){
//            Q.push(v, dist[v])
//         }
//
//        while(Q not empty)
//            u = Q.pop()// Node with the lowest edge cost
//            for v in AdjacencyList(u){
//                alt = w(u,v) // Is this better than what we've found so far?
//                if(v is in Q and alt < dist[v]){
//                    dist[v] = alt
//                    prev[v] = u
//                    Q.changePriority(v, alt)
//                 }
//            }
//        }
//    return dist, prev
//
        return null;
    }

    /**
     * Returns the shortest paths from source to other vertices in graph.
     * @param graph graph to analyze
     * @param source the starting vertex of the path
     * @return mapping of a vertex to its predecessor on shortest path from source
     */
    public static HashMap<Integer, Integer> dijkstrasAlgorithm(Graph<Integer> graph, int source){
        PriorityQueue Q = new PriorityQueue();
        HashMap<Integer,Integer> dist = new HashMap<>(); //stores best distance from s to another node
        HashMap<Integer,Integer> prev = new HashMap<>(); //stores prev node on shortest path
        //int[] dist = new int[graph.numVertices()];
        //int[] prev = new int[graph.numVertices()];

        //Set all dists to infinity, all prevs to null
        for(Integer v : graph.getVertices()){
            dist.put(v,Integer.MAX_VALUE);
            prev.put(v,null); //-1 represents null (no path predecessor for vertex v yet)
        }

        dist.put(source,0); //starting node

        for(Integer v : graph.getVertices()) {
            System.out.println("Adding vertex " + v + " with priority " + dist.get(v));
            Q.push(dist.get(v),v);
        }
//
        Pair u; //stores current vertex
        Integer uElement;

        while(!Q.isEmpty()){
            u = Q.pop();  //shortest distance to u
            uElement = (Integer)u.element;


            try {
                for (Integer v : graph.getNeighbors(uElement)) {
                  Integer alt = dist.get(uElement) + 1; // Is this better than what we've found so far? (weight from u to v is 1)
                  if(alt < dist.get(v)){
                      dist.put(v,alt);
                      prev.put(v,uElement);
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
        Graph dtest = new Graph();
        dtest.addVertex(5);
        dtest.addVertex(16);
        dtest.addVertex(12);
        dtest.addVertex(8);

        dtest.addEdge(5,16);
        dtest.addEdge(16,12);
        dtest.addEdge(12,8);
        dtest.addEdge(5,8);
        //dtest.addEdge(16,8);
        //dtest.addEdge(8,12);

        HashMap<Integer,Integer> prev = dijkstrasAlgorithm(dtest,5);
        for(Integer v : prev.keySet()){
            System.out.println("vertex: " + v + " previous: " + prev.get(v));

        }

    }
}
