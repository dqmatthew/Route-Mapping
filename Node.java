import java.util.ArrayList;

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

