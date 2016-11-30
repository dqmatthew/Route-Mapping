import java.util.ArrayList;

/* Author: Matthew Deng
 * Course: CSC 172
 * Lab Session: MW 1525-1640
 * Project 4
 * Date: April 13, 2016
 */
public class Node { // intersections
	public double latitude, longitude;
	public String NodeID;
	public Node prev;
	public double dist = 0;
	ArrayList<Node> connectedNodes = new ArrayList<Node>();

	
	public Node(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	
	}
}

