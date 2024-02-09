/**
 * 
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris Worcester
 *
 */
public class GraphNode {
	
	// ********** Properties **********
	private String name = "";
	private int heuristic;
	private int distanceFromStart;
	private String fullPath = "";
	private Map<GraphNode, Integer> edges = new HashMap<>();
	
	/**
	 * Whether distanceFromStart has been assigned.
	 */
	private boolean distanceSet = false;

	/**
	 * Constructor initializes the node name and heuristic value.
	 */
	public GraphNode(String name, int heuristic) {
		this.name = name;
		this.heuristic = heuristic;
	}

	// ********** Getters **********
	
	/**
	 * Name of the node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Estimated cost from this node to the goal: h(n)
	 */
	public int getHeuristic() {
		return heuristic;
	}
	
	/**
	 * Actual cost to move from start to this node: g(n)
	 */
	public int getDistanceFromStart() {
		return distanceFromStart;
	}

	/**
	 * Neighboring nodes and their distances
	 */
	public Map<GraphNode, Integer> getEdges() {
		return edges;
	}
	
	/**
	 * String representation of the path from the start node to this node.
	 */
	public String getFullPath() {
		return fullPath;
	}
	
	/**
	 * Combined score: f(n) = g(n) + h(n)
	 */
	public int pathCost() throws Exception {
		if (!distanceSet) {
			throw new Exception("Can't calculate path cost until distance has been set.");
		}
		return heuristic + distanceFromStart;
	}
	
	// ********** Setters **********
	
	/**
	 * Actual cost to move from start to this node: g(n)
	 */
	public void setDistanceFromStart(int dist) {
		distanceFromStart = dist;
		distanceSet = true;
	}
	
	/**
	 * String representation of the path from the start node to this node.
	 */
	public void setFullPath(String path) {
		this.fullPath = path;
	}
	
	/**
	 * Add bi-directional edge between this node and other.
	 * 
	 * @param other The neighbor node.
	 * @param distance Distance between nodes.
	 */
	public void addEdge(GraphNode other, int distance) {
		if (this.edges.containsKey(other)) {
			return;
		}
		this.edges.put(other, distance);
		other.addEdge(this, distance);
	}

	@Override
	public String toString() {
		String s = "Node " + name + ": [h = " + heuristic + ", edges = { ";

		for (Map.Entry<GraphNode, Integer> edge : edges.entrySet()) {
			s = s + "\n\t[" + edge.getKey().name + ", distance = " + edge.getValue() + "]";
		}

		s = s + "\n}]";
		return s;
	}
}
