package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import util.DataLoader;
import graph.GraphAlgorithms;

import java.util.HashMap;
import java.util.Scanner;


/**
 * This class takes in a set of movie titles and reviews and can create a graph from the data
 * with multiple possible user-selected adjacency definitions. The user can then view information
 * about the graph they chose to create.
 * EXTRA CREDIT: TBD
 * @author Bret Abel and Harper Shapiro
 * @version 12/11/19
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
		//create graph structure adjList or adjMatrix
		//get user input for graph options - DONE
		//parse data files, filling graph

		String movieFile = "src/ml-latest-small/movies.csv";
		String reviewFile = "src/ml-latest-small/ratings.csv";

		DataLoader loader = new DataLoader();
		loader.loadData(movieFile, reviewFile);

		HashMap<Integer, Movie> movies = (HashMap)loader.getMovies();

		Graph<Integer> movieGraph = new Graph<>();

		///////////ADJACENCY DEF LOOP///////////////////////////////////
		Scanner scan = new Scanner(System.in);
		int option =0;

		while(movieGraph.numVertices() == 0) {
			printAdjOptions();
			option = Integer.parseInt(scan.next());
			if (option > 0 && option < 4) {
				movieGraph = constructGraph(option, loader);
			} else if (option == 4) {
				System.exit(0);
			} else {
				System.out.println("Invalid option.");
			}
		}
		System.out.println("Graph Constructed. It has " + movieGraph.numVertices() + " Vertices and " + movieGraph.numEdges() + " Edges.");

		///////////OPTION LOOP////////////////////////////////////////
		option =0;
		while(option != 4) {
			printOptions();
			option = Integer.parseInt(scan.next());

			switch(option){
				case 1:
					printStats(movieGraph);
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

	private static void printAdjOptions() {
		System.out.println("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies");
		System.out.println("[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)");
		System.out.println("[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v");
		System.out.println("[Option 4] Quit");
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

	/**
	 * Prints statistics about the movie graph.
	 * @param movieGraph the movie graph
	 */
	public static void printStats(Graph<Integer> movieGraph){
		System.out.println("Graph statistics:");

		//  number of nodes
		int nodes = movieGraph.numVertices();
		System.out.println("Number of nodes: " + nodes);

		//  number of edges
		int edges = movieGraph.numEdges();
		System.out.println("Number of edges: " + edges);

		//  density of the graph defined as D = E / (V*(V-1)) for directed graph
		int density = edges / (nodes * (nodes - 1));
		System.out.println("Graph density: " + density);

		// maximum degree
		int maxDegree = 0;
		int curDegree = 0;
		try {
			for (Integer movie : movieGraph.getVertices()){
				curDegree = movieGraph.degree(movie);
				maxDegree = (maxDegree > curDegree) ? maxDegree : curDegree;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Max degree: " + maxDegree);

		// Flloyd-Warshall shortest paths
		int[][] paths = GraphAlgorithms.floydWarshall(movieGraph);

		//  diameter of the graph (longest shortest path)
		//  average length of shortest paths
		//  doing them together to avoid redundant nested for loops
		int diameter = 0;
		int sum = 0;
		int counter = 0;
		// Infinity token to match the Flloyd-Warshall implementation in GraphAlgorithms
		int FW_INF_TOKEN = 99999;
		for (int i = 0; i < nodes; i++){
			for (int j = 0; j < nodes; j++){
				if (paths[i][j] < FW_INF_TOKEN) {
					diameter = (diameter > paths[i][j]) ? diameter : paths[i][j];
					sum += paths[i][j];
					counter++;
				}
			}
		}
		int average = sum / counter;
		System.out.println("Diameter: " + diameter);
		System.out.println("Average shortest path length: " + average);

		return;
	}

	/**
	 * Print shortest path based on dijkstra's output and a destination node
	 * @param paths
	 * @param dest
	 */
	public static void printPath(int[] paths, int dest){
		int prevNode = paths[dest];
		if(prevNode==-1){
			System.out.println("\nNo path to " + dest + " was found.");
			return;
		}
        System.out.print(dest);
        do{
			System.out.print( "<-" + prevNode);
			prevNode = paths[prevNode];
		} while(prevNode>0);
		System.out.println("");

	}
}
