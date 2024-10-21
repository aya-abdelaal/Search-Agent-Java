package code;

public class Node {
    public State state;   // Current state (e.g., bottle configurations)
    public Node parent;    // Parent node
    public String action;  // Action taken to reach this state
    public int pathCost;   // Total cost to reach this node
    public int depth;      // Depth in the search tree

    public Node(State state, Node parent, String action, int pathCost, int depth) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
        this.depth = depth;
    }

    @Override
    public String toString() {
    	String s="";
    	for(Bottle b:state.bottles) {
    		s=s+b.toString()+";";
    	}
    	return s;
    	
    }

}

