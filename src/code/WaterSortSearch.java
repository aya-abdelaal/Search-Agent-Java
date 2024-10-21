package code;

import java.util.*;

public class WaterSortSearch extends GenericSearch {
	private static int numberOfBottles;
	private static int bottleCapacity;
	private static State initialState;

	public WaterSortSearch() {

	}

	@Override
	// Helper methods for search algorithms (e.g., goal test, action generation)
	 protected  boolean isGoal(State state) {

		for (Bottle b : state.bottles) {
			if (b.isSameColor() == false) {
				return false;
			}
		}
		return true;

	}

	public void parseStateString(String state){

		String[] parts = state.split(";");
		numberOfBottles = Integer.parseInt(parts[0]);
		bottleCapacity = Integer.parseInt(parts[1]);
		Bottle[] bottles = new Bottle[numberOfBottles];
		for (int i = 0; i < numberOfBottles; i++) {
			String[] letters = (parts[i + 2]).split(",");
			Stack<Character> st = new Stack<>();
			// we do not enter the letter 'e' we instead leave an empty spot
			for (int j = bottleCapacity - 1; j >= 0; j--) {
				if (letters[j].charAt(0) != 'e')
					st.push(letters[j].charAt(0));
			}

			Bottle b = new Bottle(bottleCapacity, st);
			bottles[i] = b;

		}

		initialState = new State(bottles);

	}

	public static String solve(String state, String strategy, Boolean visualize) {

        WaterSortSearch w = new WaterSortSearch();
		w.parseStateString(state);
        String solution = w.search(initialState, strategy, visualize);
		
        return solution;


	}


	@Override
	protected LinkedList<Node> expandNode(Node node, Map<String,Node> explored){
		LinkedList<Node> newNodes = new LinkedList<>();

		for (String action : getActions(node.state)) {
			
			//create a new state with new bottles with the same colors
			State newState = new State ( new Bottle[node.state.bottles.length] );
			for (int i = 0; i < node.state.bottles.length; i++)
				newState.bottles[i] = (Bottle) node.state.bottles[i].clone();

			int cost = newState.bottles[action.charAt(5) - '0'].pour(newState.bottles[action.charAt(7) - '0']);

			Node child = new Node(newState, node, action, node.pathCost + cost, node.depth + 1);
			

			String stateKey = child.toString();
			if (explored.containsKey(stateKey)) {
				Node existingNode = explored.get(stateKey);
				if (child.pathCost < existingNode.pathCost) {
					// Replace the existing node with the new one if it's cheaper
					explored.put(stateKey, child);
					newNodes.add(child);
				}

			} else {
				newNodes.add(child);
				explored.put(child.toString(), child);
			}

			
		}

		return newNodes;

	}

	// This method generates a list of valid actions from a given state
	protected List<String> getActions(State state) {
		ArrayList<String> r= new ArrayList<>();
		Bottle [] bottles = state.bottles;
		
		for (int i = 0; i < bottles.length; i++) {
			
			Bottle b = bottles[i];
			
			if (!b.stack.isEmpty()) { //if bottle isn't empty
				for(int j=0;j<bottles.length;j++) {
					if(i!=j) { // loop on other bottles
						
						if((bottles[j].isEmpty())||((!bottles[j].isFull() )&& ((bottles[j].stack.peek())).equals((bottles[i].stack.peek()) ))) {
							//if bottle is empty or it isn't full and has same color as our bottle
							String s="pour_"+i+"_"+j;
							r.add(s);
						}
					}
				}

			}
		}
		return r;
	}


	public static void main(String[] args) {
		String initial = "5;4;" + "b,y,r,b;" + "b,y,r,r;" + "y,r,b,y;" + "e,e,e,e;" + "e,e,e,e;";
		String x=solve(initial,"GR2",true);
		System.out.println(x);
		System.out.println("****");
	}

    

}