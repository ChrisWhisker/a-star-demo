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

	private String name;
	private int heuristic;
	// neighboring cities and their distances
	private Map<GraphNode, Integer> neighbors = new HashMap<>();

	/**
	 * Constructor
	 */
	public GraphNode(String name, int heuristic) {
		this.name = name;
		this.heuristic = heuristic;
	}

	// Getters
	public String getName() {
		return name;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public Map<GraphNode, Integer> getNeighbors() {
		return neighbors;
	}

	// Add bi-directional edge between this node and other
	public void addNeighbor(GraphNode other, int distance) {
		if (this.neighbors.containsKey(other)) {
			return;
		}
		this.neighbors.put(other, distance);
		other.addNeighbor(this, distance);
	}

	@Override
	public String toString() {
		String s = "GraphNode [name=" + name + ", heuristic=" + heuristic + ", neighbors={ ";

		for (Map.Entry<GraphNode, Integer> neighbor : neighbors.entrySet()) {
			s = s + "\n\t[name=" + neighbor.getKey().name + ", distance=" + neighbor.getValue() + "]";
		}

		s = s + "\n}]";
		return s;
	}

}
