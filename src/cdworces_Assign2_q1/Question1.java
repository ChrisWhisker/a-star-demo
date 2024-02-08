/**
 * 
 */
package cdworces_Assign2_q1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Chris Worcester
 *
 */
public class Question1 {

	static GraphNode startingNode;

	/**
	 * 
	 */
	public Question1() {
		// TODO Auto-generated constructor stub
	}

	public static File GetFirstTextFile() {
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

	public static final GraphNode FindNode(String name, List<GraphNode> nodeList) {
		GraphNode found = nodeList.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
		if (found == null) {
			System.err.println("Couldn't find the node named " + name + " in provided list.");
//			throw new Exception("Couldn't find the node named " + name + " in provided list.");
		}
		return found;
	}

	public static GraphNode CreateGraph() {
		// for each line after "Node, Heuristic", create the node and assign int value
		// save each node in a list (easily searched by name)
		// for each line after "source, target, distance",
		// find S (save to variable so we don't have to find it unless it changed from
		// prev line)
		// add [target, distance] to neighbors

		// Step 1: Create nodes. Step 2: Link nodes to form graph
		List<GraphNode> nodes = new ArrayList<>();
		int step = 0;
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
			} else if (line.contains("source, target, distance")) {
				step = 2;
				continue;
			}

			String[] parts = line.split(", ");

			if (step == 1) {

				// TODO make sure pair[1] is an int
				nodes.add(new GraphNode(parts[0], Integer.parseInt(parts[1])));
//				System.out.println("Created node " + nodes.get(nodes.size() - 1));
			} else if (step == 2) {
				// TODO make sure pair[2] is an int

				// If the source node on this line is NOT the current node
				if (currentNode == null || !parts[0].equals(currentNode.getName())) {
					// Find the node in the list
					currentNode = FindNode(parts[0], nodes);
				}

				GraphNode target = FindNode(parts[1], nodes);
				currentNode.addNeighbor(target, Integer.parseInt(parts[2]));
			}

		}

		for (GraphNode n : nodes) {
			System.out.println(n.toString());
		}

		// TODO fix this to return S
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startingNode = CreateGraph();

	}

}
