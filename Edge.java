/* Author: Matthew Deng
 * Course: CSC 172
 * Lab Session: MW 1525-1640
 * Project 4
 * Date: April 13, 2016
 */

public class Edge {// roads
		public Node node1, node2;
		//public double weight = 0;
		public Edge(Node node1, Node node2) {
			this.node1 = node1;
			this.node2 = node2;
		// 	this.weight = weight;

		}

		public Node getNode1() {
			return node1;
		}

		public Node getNode2() {
			return node2;
		}
		
	}
