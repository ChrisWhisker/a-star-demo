
/**
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Chris Worcester
 *
 */
public class q1 {

	public static void main(String[] args) throws IOException {
		GraphNode startNode = CreateGraph(args[0]);
		String result = Navigate(startNode);
		WriteSolutionFile(result, args[0]);
	}

	// ********** Primary methods **********

	/**
	 * Create and connect the GraphNodes that form a graph based on the provided file's contents.
	 * 
	 * @param fileName Name of the text file (including .txt)
	 * @return The starting node.
	 * @throws IOException If the provided file is not found.
	 */
	private static GraphNode CreateGraph(String fileName) throws IOException {
		List<GraphNode> nodes = new ArrayList<>();
		int step = 0; // Track the current step: Step 1: Create nodes. Step 2: Link nodes to form
						// graph.
		GraphNode sNode = null; // starting node
		GraphNode currentNode = null; // current node being processed

		// Open the text file specified by the command line.
		File textFile = new File(fileName);
		if (!textFile.exists()) {
			throw new IOException("File not found: " + fileName);
		}

//		 Initialize a scanner to read the file.
		Scanner scanner = null;
		try {
			scanner = new Scanner(textFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

//		 Iterate through each line in the file.
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

//			 Check where we are in the file reading process
			if (line.contains("Node, Heuristic")) {
				step = 1; // Node creation will start on the next line.
				continue;
			} else if (line.isBlank()) {
				continue;
			} else if (step == 1 && line.contains("source, target, distance")) {
				step = 2; // Node linking will start on the next line.
				continue;
			}

//			Extract individual parts of the line
			String[] parts = line.split(", ");

			if (step == 1) { // Create node
				GraphNode newNode = new GraphNode(parts[0], Integer.parseInt(parts[1]));
//				 Save the starting node
				if (newNode.getName().equalsIgnoreCase("S")) {
					sNode = newNode;
				}
				nodes.add(newNode);
//				System.out.println("Created node " + nodes.get(nodes.size() - 1));
			} else if (step == 2) { // Create edge
//				 If the source node on this line is NOT the current node
				if (currentNode == null || !parts[0].equals(currentNode.getName())) {
//					 Find the node in the list
					currentNode = FindNode(parts[0], nodes);
				}
				GraphNode target = FindNode(parts[1], nodes);
				currentNode.addEdge(target, Integer.parseInt(parts[2]));
			}
		}
//		for (GraphNode n : nodes) {
//			System.out.println(n.toString());
//		}

//		 Return the starting node to begin navigation.
		return sNode;
	}

	/**
	 * Find the optimal solution from the start node to the nearest goal node.
	 * 
	 * @param start The node to begin navigation from.
	 * @return A string representing the nodes traversed in the optimal path and the distance traveled.
	 */
	private static String Navigate(GraphNode start) {
		ArrayList<GraphNode> frontier = new ArrayList<>(); // open neighbor nodes
		ArrayList<GraphNode> closed = new ArrayList<>(); // nodes that have been fully explored

//		 Initialize start node location and add to frontier.
		start.setDistanceFromStart(0);
		start.setFullPath(start.getName());
		frontier.add(start);

//		 Initialize navigation variables
		GraphNode current = null;
		int distanceTraveled = 0;

//		 Continue until we reach a goal node
		while (current == null || current.getHeuristic() > 0) {
//			 Get the node with the lowest f(n) value.
			current = getPriorityNode(frontier);
//			 Update distance from start to reflect the current node.
			distanceTraveled = current.getDistanceFromStart();
//			 Mode the current node from the frontier to the closed list.
			frontier.remove(current);
			closed.add(current);

//			System.out.println("\n** Current node is " + current.getName() + " with path " + current.getFullPath());

//			 Process neighbors of the current node.
			for (Map.Entry<GraphNode, Integer> edge : current.getEdges().entrySet()) {
				GraphNode neighbor = edge.getKey();
				int distFromStart = distanceTraveled + edge.getValue();

//				 Skip if the neighbor has already been explored.
				if (closed.contains(neighbor)) {
					continue;
				}

//				 Update neighbor's g(n) & path and add to the frontier.
				if (!frontier.contains(neighbor) || distFromStart < neighbor.getDistanceFromStart()) {
					neighbor.setDistanceFromStart(distFromStart);
					neighbor.setFullPath(current.getFullPath() + neighbor.getName());

					if (!frontier.contains(neighbor)) {
						frontier.add(neighbor);
					}
				}
			}
		}
//		 Return the full path from the start node to the current node and the total distance traveled.
		return "['" + current.getFullPath() + "', " + distanceTraveled + "]";
	}

	/**
	 * Write the provided solution text to a file under Q1/Solutions.
	 * 
	 * @param solution The text to write.
	 * @param inputFile The name of the input file that describes the graph.
	 */
	private static void WriteSolutionFile(String solution, String inputFile) {
		String directoryPath = "./Q1/Solutions";

//		 Create the sub-folder if it doesn't exist
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

//		 Define the path to the file
		String filePath = directoryPath + "/" + inputFile.replace(".txt", "_solution.txt");

//		 Write the content to the file
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(solution);
			System.out.println("Content successfully written to " + filePath);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
		
		System.out.println("[SUCCESS]: Solution is written to " + filePath);
	}

	// ********** Helper methods **********

	/**
	 * Find a node by its name.
	 * 
	 * @param name     Name of the node to search for.
	 * @param nodeList List to search in.
	 * @return The node.
	 */
	private static final GraphNode FindNode(String name, List<GraphNode> nodeList) {
		GraphNode found = nodeList.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
		if (found == null) {
			System.err.println("Couldn't find the node named " + name + " in provided list.");
		}
		return found;
	}

	private static GraphNode getPriorityNode(ArrayList<GraphNode> list) {
		int minCost = Integer.MAX_VALUE; // minimum f(n)
		GraphNode idealNextNode = null;

		for (GraphNode node : list) {
			try {
//				System.out.println("Node " + node.getName() + " has f(n) of " + node.pathCost() + "("
//						+ node.getDistanceFromStart() + " + " + node.getHeuristic() + ")");
				if (node.pathCost() < minCost) {
					minCost = node.pathCost();
					idealNextNode = node;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
//		System.out.println("Lowest cost node in the frontier is " + idealNextNode.getName());
		return idealNextNode;
	}
}
