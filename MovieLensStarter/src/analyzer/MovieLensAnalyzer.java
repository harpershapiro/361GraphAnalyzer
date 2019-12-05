package analyzer;

/**
 * Please include in this comment you and your partner's name and describe any extra credit that you implement 
 */
public class MovieLensAnalyzer {
	
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}


		//todo:
		//create graph structure adjList or adjMatrix
		//get user input for graph options
		//parse data files, filling graph
		//get user input for analysis
		//possible analyses
//		The number of nodes
//		The number of edges
//		The density of the graph defined as D = E / (V*(V-1)) for a directed graph
//		The maximum degree (i.e. the largest number of outgoing edges of any node)
//		The diameter of the graph (i.e. the longest shortest path)
//		The average length of the shortest paths in the graph

		//for Dijkstra's (option 3)
			//get user input for start and end nodes
			//run Dijkstra's
			//print out entire shortest path

	}
}
