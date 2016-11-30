import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph extends JPanel {

	private static ArrayList<Node> nodes = new ArrayList<Node>();
	private static ArrayList<Edge> edges = new ArrayList<Edge>();
	private static ArrayList<Edge> edgePath = new ArrayList<Edge>();
	private static HashMap<String, Node> nodesHash = new HashMap<String, Node>();
	private static HashMap<Node, String> nodesIDHash = new HashMap<Node, String>();
	private static HashMap<Edge, String> edgesHash = new HashMap<Edge, String>();
	private static HashMap<ArrayList<Double>, String> coordHash = new HashMap<ArrayList<Double>, String>();
	
	private static boolean UR = false;
	private static boolean Monroe = false;
	private static boolean NYS = false;
	private static boolean shortestpath = false;
	static double maxLongitude = 0;
	static double minLongitude = 0;
	static double minLatitude = 0;
	static double maxLatitude = 0;
	static double scaleNYSCon = 100;
	static double scaleMONCon = 1000;
	static double scaleURCon = 100000;
	static double scaleCon = 0;

	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		double tempLat1 = 0;
		double tempLat2 = 0;
		double tempLong1 = 0;
		double tempLong2 = 0;
		for (Edge e : edges) {

			tempLat1 = e.node1.latitude * scaleCon;
			tempLat2 = e.node2.latitude * scaleCon;
			tempLong1 = e.node1.longitude * scaleCon;
			tempLong2 = e.node2.longitude * scaleCon;

			g2.draw(new Line2D.Double((tempLong1 - (minLongitude * scaleCon)), (getHeight() - (tempLat1 - (minLatitude * scaleCon))), (tempLong2 - (minLongitude * scaleCon)), (getHeight() - (tempLat2 - (minLatitude * scaleCon)))));
		}
		for (Edge f : edgePath) {
			if(shortestpath){
			g.setColor(Color.RED);
			}else{
				g.setColor(Color.CYAN);
			}
			tempLat1 = f.node1.latitude * scaleCon;
			tempLat2 = f.node2.latitude * scaleCon;
			tempLong1 = f.node1.longitude * scaleCon;
			tempLong2 = f.node2.longitude * scaleCon;

			g2.draw(new Line2D.Double(tempLong1 - (minLongitude * scaleCon), getHeight() - (tempLat1 - (minLatitude * scaleCon)), tempLong2 - (minLongitude * scaleCon), getHeight() - (tempLat2 - (minLatitude * scaleCon))));
		}

	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public HashMap<String, Node> getHashNodes() {
		return nodesHash;
	}
	
	public HashMap<Edge, String> getHashEdges(){
		return edgesHash;
	}

	public HashMap<ArrayList<Double>, String> getCoordHash(){
		return coordHash;
	}
	public void insert(Edge e) {
		edges.add(e);

	}

	public static Graph createFromFile(String filename) { // TODO

		String tmpRoadID = null;
		String tmpInterID = null;
		double tmplatitude = 0;
		double tmplongitude = 0;
		String tmpinter1ID = null;
		String tmpinter2ID = null;

		int count = 1;
		boolean intersection = false;
		boolean road = false;
		int first = 1;
		try {
			Scanner scanner = new Scanner(new File(filename));

			while (scanner.hasNext()) {
				String line = scanner.next();

				if (count == 1) {
					if (line.equals("i")) {
						intersection = true;
						road = false;
					} else if (line.equals("r")) {
						intersection = false;
						road = true;

					}
				} else if (count == 2) {
					if (intersection) {
						tmpInterID = line;
					} else {
						tmpRoadID = line;
					}
				} else if (count == 3) {
					if (intersection) {

						tmplatitude = Double.parseDouble(line);
						if (first == 1) {
							minLatitude = tmplatitude;
							maxLatitude = tmplatitude;

						} else {
							if (tmplatitude > maxLatitude) {
								maxLatitude = tmplatitude;
							}
							if (tmplatitude < minLatitude) {
								minLatitude = tmplatitude;
							}
						}
					} else {
						tmpinter1ID = line;
					}

				} else {

					if (intersection) {
						tmplongitude = Double.parseDouble(line);

						if (first == 1) {
							minLongitude = tmplongitude;
							maxLongitude = tmplongitude;
							first++;
						} else {
							if (tmplongitude > maxLongitude) {
								maxLongitude = tmplongitude;
							}
							if (tmplongitude < minLongitude) {
								minLongitude = tmplongitude;
							}
						}
						

						Node tmpNode = new Node(tmplatitude, tmplongitude);
						tmpNode.NodeID = tmpInterID;
						nodes.add(tmpNode);
						nodesHash.put(tmpInterID, tmpNode);
						nodesIDHash.put(tmpNode, tmpInterID);

					} else {
						tmpinter2ID = line;
						double tempWeight = 0;
						double xDifference = 0;
						double yDifference = 0;
						double sum = 0;

						Node node1 = new Node(0, 0);
						Node node2 = new Node(0, 0);
						
						yDifference = Math.abs(node1.latitude - node2.latitude);
						xDifference = Math.abs(node1.longitude - node2.longitude);
						sum = xDifference * xDifference + yDifference * yDifference;
						tempWeight = Math.sqrt(sum);
						
						node1 = nodesHash.get(tmpinter1ID);
						node2 = nodesHash.get(tmpinter2ID);
						
						node1.NodeID = nodesIDHash.get(node1);
						node2.NodeID = nodesIDHash.get(node2);
						
						node1.connectedNodes.add(node2);
						node2.connectedNodes.add(node1);

						Edge tmpEdge = new Edge(node1, node2);
						Edge tmpEdge2 = new Edge(node2, node1);
						
						edges.add(tmpEdge);
					
						ArrayList<Double> tmpList = new ArrayList<Double>();
						tmpList.add(node1.latitude);
						tmpList.add(node1.longitude);
						tmpList.add(node2.latitude);
						tmpList.add(node2.longitude);
						
						
						coordHash.put(tmpList, tmpRoadID);
						
						edgesHash.put(tmpEdge, tmpRoadID);
						edgesHash.put(tmpEdge2, tmpRoadID);
						
					}
					count = 0;
				}

				count++;

			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Graph graph = new Graph(nodes, edges);

		return graph;

	}

	public static boolean containsChar(String s, char search) {
		if (s.length() == 0)
			return false;
		else
			return s.charAt(0) == search || containsChar(s.substring(1), search);
	}

	public static void main(String[] args) {
		String filename = null;
		int i = 0, j;
		String arg;
		double tempX = 0;
		double tempY = 0;
		
		JFrame frame = new JFrame("Project 4");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graph graph = new Graph(nodes, edges);
		
	
		while(i < args.length){
			arg = args[i++];
			
			switch(arg){
			case "ur.txt":
				filename = arg;
				UR = true;
				graph = createFromFile(filename);
				break;
			case "monroe.txt":
				filename = arg;
				Monroe = true;
				graph = createFromFile(filename);
				
				break;
			case "nys.txt":
				filename = arg;
				NYS = true;
				graph = createFromFile(filename);
				break;
			case "[-show]":
				graph.setBackground(Color.WHITE);
				frame.add(graph);
					if(NYS){
						scaleCon = scaleNYSCon;
					}else if(Monroe){
						scaleCon = scaleMONCon;
					}else{
						scaleCon = scaleURCon;
					}

					tempX = (maxLongitude - minLongitude) * scaleCon;
					tempY = (maxLatitude - minLatitude) * scaleCon;
					frame.setSize((int) tempX, (int) tempY);
					frame.repaint(); //paints the map
					frame.setVisible(true);
					break;
			case "[-directions":
				shortestpath = true;
				DijkstraAlgorithm dij = new DijkstraAlgorithm(graph);
				String startIntersection = null;
				String endIntersection = null;
				startIntersection = args[i++];
				endIntersection = args[i++];
				endIntersection = endIntersection.substring(0, endIntersection.length() - 1);
				edgePath = dij.start(startIntersection, endIntersection);;
				frame.repaint();
				break;
			case "[-meridianmap]":
				PrimsAlgorithm prim = new PrimsAlgorithm(graph);
				edgePath = prim.start();//stores an edgePath ArrayList for graphing 
				frame.repaint();
				break;
			default:
				System.err.println("Illegal Command Line " + arg);
				break;
				
			}
			
			
		}
		
	
		
		
	}
}
