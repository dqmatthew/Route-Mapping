import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;

/* Author: Matthew Deng
 * Course: CSC 172
 * Lab Session: MW 1525-1640
 * Project 4
 * Date: April 13, 2016
 */
public class DijkstraAlgorithm { //class to implement Dijkstra's Algorithm
	
	public Comparator<Node> distComparator = new Comparator<Node>(){
		
		public int compare(Node node1, Node node2){
			if(node1.dist - node2.dist < 0){
				return -1;
			}else if(node1.dist - node2.dist > 0){
				return 1;
			}else{
				return 0;
			}
			
		}
	};
	
	private static ArrayList<Node> nodes = new ArrayList<Node>();
	private static ArrayList<Edge> edges = new ArrayList<Edge>();
	private static ArrayList<Edge> edgePath = new ArrayList<Edge>();
	
	private static HashMap<String, Node> nodesHash = new HashMap<String, Node>();

	private PriorityQueue<Node> priorityQueue = new PriorityQueue<>(distComparator);
	
	
	
	
	
	public DijkstraAlgorithm(Graph graph){
		nodes = graph.getNodes();
		edges = graph.getEdges();
		nodesHash = graph.getHashNodes();
		
	
	}
	public double getDistance(Node node1, Node node2){ //stores the distance (in latitude and longitude degrees)
		double tempWeight = 0;
		double xDifference = 0;
		double yDifference = 0;
		double sum = 0;
		
		yDifference = Math.abs(node1.latitude - node2.latitude);
		xDifference = Math.abs(node1.longitude - node2.longitude);
		sum = xDifference*xDifference + yDifference*yDifference;
		tempWeight = Math.sqrt(sum);
		
		return tempWeight;
	}
	
	public double getMiles(Node node1, Node node2){ //code and formula taken from stackexchange (The Haversine Method) returns a distance converted to miles
		final double R = 3959.0;
		Double latDistance = Math.toRadians(node2.latitude - node1.latitude);
		Double longDistance = Math.toRadians(node2.longitude - node1.longitude);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(node1.latitude)) * Math.cos(Math.toRadians(node2.latitude)) * Math.sin(longDistance /2) * Math.sin(longDistance / 2);
		Double c = 2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double distance = R * c;
		
		
		return distance;
	}
	

	public ArrayList<Edge> start(String start, String finish){ //begins the DijkstraAlgorithm
		Node nodestart = nodesHash.get(start);
		Node nodefinish = nodesHash.get(finish);
		double miles = 0;
		nodestart.dist = 0;

		for(Node n: nodes){
			if(!n.equals(nodestart)){
				n.dist = Integer.MAX_VALUE;

				priorityQueue.add(n);
			}
			
			priorityQueue.add(nodestart);
			
		}
		
		
		while(!priorityQueue.isEmpty()){
			Node u = priorityQueue.remove();
			
			
			
			if(u.equals(nodefinish)){ //returns desired path once queue reaches end node using parent node calls
				System.out.println("PATH: ");
				while(u != null){
					System.out.println("ID: " + u.NodeID);
					
					
					
					Node temp1 = u;
					u = u.prev;
					Node temp2 = u;
					if(temp2 != null){
					edgePath.add(new Edge(temp1, temp2));
					miles += getMiles(temp1, temp2);
					
					}
					
					
				}
				System.out.println("TOTAL MILES: "   + miles + " miles");
				return edgePath;
			}
			
			for(Node v: u.connectedNodes){
				
				double alt = 0;
				alt = u.dist + getDistance(u, v);
				
				if(alt < v.dist){
					v.dist = alt;
					v.prev = u;
					priorityQueue.remove(v);
					priorityQueue.add(v);
					
					
				}
			}
			
		}
		return edgePath;
	}
	
}
