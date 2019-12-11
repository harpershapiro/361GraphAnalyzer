package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import util.DataLoader;
import graph.GraphAlgorithms;

import java.util.HashMap;
import java.util.Scanner;


/**
 * Please include in this comment you and your partner's name and describe any extra credit that you implement 
 */
public class MovieLensAnalyzer {
	
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres

		/*
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}*/

		//todo: remove hardcode
		String movieFile = "src/ml-latest-small/movies.csv";
		String reviewFile = "src/ml-latest-small/ratings.csv";

		DataLoader loader = new DataLoader();
		loader.loadData(movieFile, reviewFile);

		HashMap<Integer, Movie> movies = (HashMap)loader.getMovies();


		Graph<Integer> movieGraph = constructGraph(1,loader);
		System.out.println("Graph Consctructed. It has " + movieGraph.numVertices() + " Vertices and " + movieGraph.numEdges() + " Edges.");


		//todo:
		//create graph structure adjList or adjMatrix
		//get user input for graph options
		//parse data files, filling graph
		//get user input for analysis
		//possible analyses:
		//		The number of nodes
		//		The number of edges
		//		The density of the graph defined as D = E / (V*(V-1)) for a directed graph
		//		The maximum degree (i.e. the largest number of outgoing edges of any node)
		//		The diameter of the graph (i.e. the longest shortest path)
		//		The average length of the shortest paths in the graph

		///////////OPTION LOOP////////////////////////////////////////
		Scanner scan = new Scanner(System.in);
		int option =0;
		while(option != 4) {
			printOptions();
			option = Integer.parseInt(scan.next());

			switch(option){
				case 1:
					//print stats
					break;

				case 2:
					//get input for node then print its info
					System.out.println("Which node?");
					int node = Integer.parseInt(scan.next());
					System.out.println(movies.get(node));
					break;

				case 3:
					//get input for 2 nodes then find shortest path
					System.out.println("Which source?");
					int source = Integer.parseInt(scan.next());
					System.out.println("Which destination?");
					int dest = Integer.parseInt(scan.next());
					int[] paths = GraphAlgorithms.dijkstrasAlgorithm(movieGraph,source);
					printPath(paths,dest);
					break;

				case 4:
					break;

				default:
					System.out.println("Invalid option.");
					break;
			}

		}

		//for Dijkstra's (option 3)
			//get user input for start and end nodes
			//run Dijkstra's
			//print out entire shortest path

	}

	private static void printOptions() {
		System.out.println("[Option 1] Print out graph statistics");
		System.out.println("[Option 2] Print node information");
		System.out.println("[Option 3] Display shortest path between two nodes");
		System.out.println("[Option 4] Quit");
		System.out.println("Choose an option (1-4)\n");
	}

	/**
	 * Takes a user choice for adjacency definition and creates a movie graph
	 * @param choice
	 * @return
	 */
	public static Graph<Integer> constructGraph(int choice, DataLoader loader){
		//Load the data

		HashMap<Integer, Movie> movies = (HashMap) loader.getMovies();
		HashMap<Integer, Reviewer> reviewers = (HashMap) loader.getReviewers();

		Graph<Integer> movieGraph = new Graph<>(); //Graph of movie ids

		//add all vertices
		for(Integer u : movies.keySet()){
			movieGraph.addVertex(u);
		}

		//Choice 1: u and v are adjacent if the same 12 users watched both movies
			//get all movie ids and compare each to one another
			for(Integer u : movies.keySet()) {
				for(Integer v : movies.keySet()) {
					//get the sets of reviewer ids for both movies
					HashMap<Integer,Double> uReviews = (HashMap) (movies.get(u).getRatings());
					HashMap<Integer,Double> vReviews = (HashMap) (movies.get(v).getRatings());
					int totalMatches;
					double threshold;

					switch(choice) {
						case 1: //12 users gave same rating to both movies
							//count the number of matching reviewers in both movie's review sets
							totalMatches = 0;
							for (Integer uReviewer : uReviews.keySet()) {
								if (vReviews.containsKey(uReviewer) && v!=u) {
									if(uReviews.get(uReviewer).equals(vReviews.get(uReviewer)))totalMatches++;
								}
							}

							//add an edge if same 12 users rated both movies same
							if (totalMatches >= 12) {
								//System.out.println("Added edge from "+ u + " to "+ v);
								movieGraph.addEdge(u, v);
								movieGraph.addEdge(v, u);
							}
							break;

						case 2: //12 users watched both movies
							//count the number of matching reviewers in both movie's review sets
							totalMatches = 0;
							for (Integer uReviewer : uReviews.keySet()) {
								if (vReviews.containsKey(uReviewer) && v!=u) {
									totalMatches++;
								}
							}

							//add an edge if same 12 users rated both
							if (totalMatches >= 12) {
								movieGraph.addEdge(u, v);
								movieGraph.addEdge(v, u);
							}
							break;

						case 3: //33 percent of the users that watched u also watched v
							//count the number of matching reviewers in both movie's review sets
							totalMatches = 0;
							threshold = 0.33*(uReviews.keySet().size());
							for (Integer uReviewer : uReviews.keySet()) {
								if (vReviews.containsKey(uReviewer) && v!=u) {
									totalMatches++;
								}
							}

							//add an edge if same 12 users rated both
							if (totalMatches >= threshold) {
								movieGraph.addEdge(u, v);
								movieGraph.addEdge(v, u);
							}
							break;
					}
				}
			}

		return movieGraph;
	}

	public static void printStats(){
		return;
	}

	/**
	 * Print shortest path based on dijkstra's output and a destination node
	 * @param paths
	 * @param dest
	 */
	public static void printPath(int[] paths, int dest){
		System.out.print(dest);
		int prevNode = paths[dest];
		if(prevNode==-1){
			System.out.println("No path to " + dest + " was found.");
			return;
		}
		do{
			System.out.print( "<-" + prevNode);
			prevNode = paths[prevNode];
		} while(prevNode>0);
		System.out.println("");

	}
}
