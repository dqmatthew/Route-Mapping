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
