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

	// the node that traversed before this one in the shortest path
//	public GraphNode parentNode;
	
	public String fullPath = "";
	
	private String name = "";
	
	/**
	 * Estimated cost from this node to the goal: h(n)
	 */
	private int heuristic;
	
	/**
	 * Actual cost to move from start to this node: g(n)
	 */
	private int distanceFromStart;
	
	/**
	 * Whether distanceFromStart has been assigned.
	 */
	private boolean distanceSet = false;
	
	/**
	 * Combined score: f(n) = g(n) + h(n)
	 */
	public int pathCost() throws Exception {
		if (!distanceSet) {
			throw new Exception("Can't calculate path cost until distance has been set.");
		}
		return heuristic + distanceFromStart;
	}
	
	/**
	 * String representation of the path from the start node to this node
	 */
	//	private String fullPath;
	
	// neighboring nodes and their distances
	private Map<GraphNode, Integer> edges = new HashMap<>();

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
	
	public int getDistanceFromStart() {
		return distanceFromStart;
	}

	public Map<GraphNode, Integer> getEdges() {
		return edges;
	}
	

//	public String getFullPath() {
//		return fullPath;
//	}
	
	// Setters
	public void setDistanceFromStart(int dist) {
		distanceFromStart = dist;
		distanceSet = true;
	}
	
	// Add bi-directional edge between this node and other
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

//	public void setFullPath(String path) {
//		this.fullPath = path;
//	}

}
