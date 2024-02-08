/**
 * 
 */
package cdworces_Assign2_q1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris Worcester
 *
 */
public class GraphNode {

	String name;
	int heuristic;
	// neighboring cities and their distances
	Map<GraphNode, Integer> neighbors = new HashMap<>();

	/**
	 * 
	 */
	public GraphNode(String name, int heuristic) {
		this.name = name;
		this.heuristic = heuristic;
	}

	public void AddNeighbor(GraphNode neighborNode, int distance) {
		this.neighbors.put(neighborNode, distance);
	}

}
