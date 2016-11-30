import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/* Author: Matthew Deng
 * Course: CSC 172
 * Lab Session: MW 1525-1640
 * Project 4
 * Date: April 13, 2016
 */
public class PrimsAlgorithm {
	public Comparator<Node> distComparator = new Comparator<Node>() {

		public int compare(Node node1, Node node2) {
			if (node1.dist - node2.dist < 0) {
				return -1;
			} else if (node1.dist - node2.dist > 0) {
				return 1;
			} else {
				return 0;
			}

		}
	};
	private static ArrayList<Node> nodes = new ArrayList<Node>();
	private static ArrayList<Edge> edges = new ArrayList<Edge>();
	private static ArrayList<Edge> edgePath = new ArrayList<Edge>();
	private static HashMap<ArrayList<Double>, String> coordHash = new HashMap<ArrayList<Double>, String>();

	private static HashMap<Edge, String> edgesHash = new HashMap<Edge, String>();

	private PriorityQueue<Node> priorityQueue = new PriorityQueue<>(distComparator);

	public PrimsAlgorithm(Graph graph) {
		edges = graph.getEdges();
		nodes = graph.getNodes();
		edgesHash = graph.getHashEdges();
		coordHash = graph.getCoordHash();

	}

	public double getDistance(Node node1, Node node2) { //stores the distance (in latitude and longitude degrees)
		double tempWeight = 0;
		double xDifference = 0;
		double yDifference = 0;
		double sum = 0;

		yDifference = Math.abs(node1.latitude - node2.latitude);
		xDifference = Math.abs(node1.longitude - node2.longitude);
		sum = xDifference * xDifference + yDifference * yDifference;
		tempWeight = Math.sqrt(sum);

		return tempWeight;
	}

	public double getMiles(Node node1, Node node2) { //code and formula taken from stackexchange (The Haversine Method) returns a distance converted to miles
		final double R = 3959.0;
		Double latDistance = Math.toRadians(node2.latitude - node1.latitude);
		Double longDistance = Math.toRadians(node2.longitude - node1.longitude);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(node1.latitude)) * Math.cos(Math.toRadians(node2.latitude)) * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance;
	}

	public ArrayList<Edge> start() { //begins the DijkstraAlgorithm
		Node nodestart = nodes.remove(1);

		Node u = new Node(0, 0);
		double miles = 0;
		nodestart.dist = 0;

		for (Node n : nodes) {
			if (!n.equals(nodestart)) {
				n.dist = Integer.MAX_VALUE;

				priorityQueue.add(n);
			}

			priorityQueue.add(nodestart);

		}

		while (!priorityQueue.isEmpty()) {

			u = priorityQueue.remove();
			if (u.prev != null) {

				ArrayList<Double> tmpList1 = new ArrayList<Double>();
				tmpList1.add(u.latitude);
				tmpList1.add(u.longitude);
				tmpList1.add(u.prev.latitude);
				tmpList1.add(u.prev.longitude);
				ArrayList<Double> tmpList2 = new ArrayList<Double>();
				tmpList2.add(u.latitude);
				tmpList2.add(u.longitude);
				tmpList2.add(u.prev.latitude);
				tmpList2.add(u.prev.longitude);

				Edge temp = new Edge(u, u.prev);

				edgePath.add(temp);
				if (coordHash.get(tmpList1) != null) {
					System.out.println(coordHash.get(tmpList1));
				} else if(coordHash.get(tmpList2) != null){
					System.out.println(coordHash.get(tmpList2));
					
				}
				miles += getMiles(u, u.prev);
			}

			for (Node v : u.connectedNodes) {//for every adjacent node to u

				double alt = 0;
				alt = getDistance(u, v);

				if (alt < v.dist) {
					v.dist = alt;
					v.prev = u;
					priorityQueue.remove(v);
					priorityQueue.add(v); //updates v data

				}
			}

		}

		Node temp = u;

		System.out.println("TOTAL MILES: " + miles + " miles"); //TODO
		return edgePath;

	}

}
