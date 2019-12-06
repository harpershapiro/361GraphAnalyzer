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

    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
        PriorityQueue Q = new PriorityQueue();
        HashMap<Pair,Integer> dist = new HashMap<>();
        //Set all dists to infinity

        HashMap<Pair,Pair> prev = new HashMap<>(); //stores prev node on shortest path

//        dist[s] = 0
//        for(v in V) []
//            Q.push(v, dist[v])
//         }
//
//         while(Q not empty)
//            u = Q.pop()// Found shortest path to u
//            for v in AdjacencyList(u){
//                alt = dist[u] + w(u,v) // Is this better than what we've found so far?
//                if(alt < dist[v]){
//                    dist[v] = alt
//                    prev[v] = u
//                    Q.changePriority(v, alt)
//                }
//            }
//        }
    //return dist, prev


        return null;
    }
}
