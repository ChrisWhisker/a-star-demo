/**
 * 
 */
package cdworces_Assign2_q1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Chris Worcester
 *
 */
public class Question1 {

	private static File GetFirstTextFile() {
		File[] files = new File(System.getProperty("user.dir"))
				.listFiles((file) -> file.isFile() && file.getName().endsWith(".txt"));

		// Return null if no text files found
		if (files == null || files.length == 0) {
			System.err.println("No text file found in the project diretory.");
			return null;
		}

		// Return the first text file found
		return files[0];
	}

	private static final GraphNode FindNode(String name, List<GraphNode> nodeList) {
		GraphNode found = nodeList.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
		if (found == null) {
			System.err.println("Couldn't find the node named " + name + " in provided list.");
		}
		return found;
	}

	private static GraphNode CreateGraph() {
		// Step 1: Create nodes. Step 2: Link nodes to form graph
		List<GraphNode> nodes = new ArrayList<>();
		int step = 0;
		GraphNode sNode = null;
		GraphNode currentNode = null;

		File textFile = GetFirstTextFile();
		Scanner scanner = null;
		try {
			scanner = new Scanner(textFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains("Node, Heuristic")) {
				step = 1;
				continue;
			} else if (line.isBlank()) {
				continue;
			} else if (step == 1 && line.contains("source, target, distance")) {
				step = 2;
				continue;
			}

			String[] parts = line.split(", ");

			if (step == 1) {
				// TODO make sure pair[1] is an int
				GraphNode newNode = new GraphNode(parts[0], Integer.parseInt(parts[1]));

				if (newNode.getName().equalsIgnoreCase("S")) {
					sNode = newNode;
				}

				nodes.add(newNode);
//				System.out.println("Created node " + nodes.get(nodes.size() - 1));
			} else if (step == 2) {
				// TODO make sure pair[2] is an int

				// If the source node on this line is NOT the current node
				if (currentNode == null || !parts[0].equals(currentNode.getName())) {
					// Find the node in the list
					currentNode = FindNode(parts[0], nodes);
				}

				GraphNode target = FindNode(parts[1], nodes);
				currentNode.addEdge(target, Integer.parseInt(parts[2]));
			}

		}

//		for (GraphNode n : nodes) {
//			System.out.println(n.toString());
//		}

		return sNode;
	}
	
	private static GraphNode getPriorityNode(ArrayList<GraphNode> list) {
		int minCost = Integer.MAX_VALUE; // minimum f(n)
		GraphNode idealNextNode = null;

		for (GraphNode node : list) {
			try {
				
				System.out.println("Node " + node.getName() + " has f(n) of " + node.pathCost() + "("+node.getDistanceFromStart() +
						" + " + node.getHeuristic() +")");
				
				if (node.pathCost() < minCost)
				{
					minCost = node.pathCost();
					idealNextNode = node;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		System.out.println("Lowest cost node in the frontier is " + idealNextNode.getName());
		return idealNextNode;
	}

	private static String Navigate(GraphNode start) {
		ArrayList<GraphNode> frontier = new ArrayList<>();
		start.setDistanceFromStart(0);
		start.fullPath = start.getName();
		frontier.add(start);
		ArrayList<GraphNode> closed = new ArrayList<>();
		GraphNode current = null;
		int distanceTraveled = 0;

		while (current == null || current.getHeuristic() > 0) {
			current = getPriorityNode(frontier);
			distanceTraveled = current.getDistanceFromStart();
			frontier.remove(current);
			closed.add(current);
			
			System.out.println("\n** Current node is " + current.getName() + " with path " + current.fullPath);

			// Add neighbor nodes to the frontier
			for (Map.Entry<GraphNode, Integer> edge : current.getEdges().entrySet()) {
				GraphNode neighbor = edge.getKey();
				
				int distFromStart = distanceTraveled + edge.getValue();
				
				// already expanded
				if (closed.contains(neighbor))
				{
					continue;
				}
				
				if (frontier.contains(neighbor)) { // found shorter path to node
					if (distFromStart < neighbor.getDistanceFromStart()) {
						neighbor.setDistanceFromStart(distFromStart);
						neighbor.fullPath = current.fullPath + neighbor.getName();
					}
				} else { // add new node to frontier
					neighbor.setDistanceFromStart(distFromStart);
					neighbor.fullPath = current.fullPath + neighbor.getName();
					frontier.add(neighbor);
				}
			}
		}

		return "['" + current.fullPath + "', " + distanceTraveled + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphNode startNode = CreateGraph();
		String result = Navigate(startNode);
		
        Path path = Paths.get("_solution.txt"); // Specify file path
        try {
			Files.write(path, result.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
