package code;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

public abstract class GenericSearch {

	//search method
	public String search(State initialState, String strategy, boolean visualize){

		// Measure the start time
        long startTime = System.currentTimeMillis();

        // Initialize CPU monitoring
		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// double cpuLoad = osBean.getProcessCpuLoad(); // Get CPU load for the current process
        double totalCpuLoad = 0.0;
        long totalMemoryUsage = 0;

		if(strategy.equals("ID")){
			int depth = 0;
			int expanded = 0;

			while(true){
				Queue<Node> q = makeQueue(new Node(initialState, null, "", 0, 0), strategy);
				Map<String, Node> explored = new HashMap<>();
				boolean cutoff = false;

				while(!q.isEmpty()){
					Node curr = q.poll();
					explored.put(curr.toString(), curr);
					expanded++;

					// Memory usage during this iteration
					Runtime runtime = Runtime.getRuntime();
					long memoryUsage = runtime.totalMemory() - runtime.freeMemory();
					totalMemoryUsage += memoryUsage;

					// CPU utilization during this iteration
					if (expanded % 5 == 0) { // Sample every 10 iterations
						double cpuLoad = osBean.getProcessCpuLoad();
						if (cpuLoad >= 0) {
							totalCpuLoad += cpuLoad;
						}
					}

					if(isGoal(curr.state)){
						printMetrics(totalCpuLoad, expanded, startTime, expanded, totalMemoryUsage);
						return constructSolution(curr, visualize, expanded);
					}else if(curr.depth < depth){
						qingFun(q, strategy, expandNode(curr, explored));
					}else{
						cutoff = true;
					}
				}

				if(cutoff){
					depth++;
				}else {
					printMetrics(totalCpuLoad, expanded, startTime, expanded, totalMemoryUsage);
					return "NOSOLUTION";
				}
			}
		}else{
			//make node and make q
			Queue<Node> q = makeQueue(new Node(initialState, null, "", 0, 0), strategy);
			Map<String, Node> explored = new HashMap<>();
			
			int expanded = 0;


			while(!q.isEmpty()){
				Node curr = q.poll();
				explored.put(curr.toString(), curr);
				expanded++;


				// Memory usage during this iteration
				Runtime runtime = Runtime.getRuntime();
				long memoryUsage = runtime.totalMemory() - runtime.freeMemory();
				totalMemoryUsage += memoryUsage;

				// CPU utilization during this iteration
				if (expanded % 5 == 0) { // Sample every 10 iterations
					double cpuLoad = osBean.getProcessCpuLoad();
					if (cpuLoad >= 0) {
						totalCpuLoad += cpuLoad;
					}
				}

				if(isGoal(curr.state)){
					printMetrics(totalCpuLoad, expanded, startTime, expanded, totalMemoryUsage);
					return constructSolution(curr, visualize, expanded);
				}else{
					qingFun(q, strategy, expandNode(curr, explored));
				}
			}
			printMetrics(totalCpuLoad, expanded, startTime, expanded, totalMemoryUsage);
			return "NOSOLUTION";
		}
	}

	//print metrics method
	public void printMetrics(double totalCpuLoad,  int expanded, long startTime, int iterations, long totalMemoryUsage) {
		long endTime = System.currentTimeMillis();
		double avgCpuLoad = totalCpuLoad / Math.floor(iterations /5);
		double avgMemoryUsage = (double) totalMemoryUsage / iterations;
		// Print results
		System.out.println("Expanded Nodes: " + expanded);
		System.out.println("Runtime: " + (endTime - startTime) + " ms");
		//print memory usage as KB
		System.out.printf("Average Memory Usage: %.2f KB\n", avgMemoryUsage  / 1024);
		System.out.printf("Average CPU Utilization: %.2f load\n", avgCpuLoad);
		System.out.println("************");
	}


	private Queue<Node> makeQueue(Node node, String strategy) {
		switch (strategy) {
		case "BF", "DF", "ID" -> {
                    Queue<Node> queue = new LinkedList<>();
                    queue.add(node);
                    return queue;
                }
		case "UC" -> {
					Queue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
						@Override
						public int compare(Node o1, Node o2) {
							return o1.pathCost - o2.pathCost;
						}
					});
					queue.add(node);
					return queue;
				}
		case "GR1" -> {
					Queue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
						@Override
						public int compare(Node o1, Node o2) {
							return o1.state.getHeuristic1() - o2.state.getHeuristic1();
						}
					});
					queue.add(node);
					return queue;
				}
		case "GR2" -> {

					Queue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
						@Override
						public int compare(Node o1, Node o2) {
							return o1.state.getHeuristic2() - o2.state.getHeuristic2();
						}
					});
					queue.add(node);
					return queue;
				}
		case "AS1" -> {
					Queue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
						@Override
						public int compare(Node o1, Node o2) {
							return (o1.pathCost + o1.state.getHeuristic1()) - (o2.pathCost + o2.state.getHeuristic1());
						}
					});
					queue.add(node);
					return queue;
				}
		case "AS2" -> {
					Queue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
						@Override
						public int compare(Node o1, Node o2) {
							return (o1.pathCost + o1.state.getHeuristic2()) - (o2.pathCost + o2.state.getHeuristic2());
						}
					});
					queue.add(node);
					return queue;
				}
             
		default ->  {
			return new LinkedList<>();
		}
		}
        
    }

	private void qingFun(Queue<Node> q, String strategy, LinkedList<Node> newNodes) {
		switch (strategy) {
			case "BF" -> // Breadth-First Search
				{q.addAll(newNodes); break;} // Add new nodes to the back (standard BFS)
			case "DF", "ID" -> {
                            // Depth-First Search
                            for (int i = newNodes.size() - 1; i >= 0; i--) {
                                ((Deque<Node>) q).addFirst(newNodes.get(i));  // Add new nodes to the front in reverse order
                            }
							break;
                }
			case "UC", "GR1", "GR2", "AS1", "AS2" -> {q.addAll(newNodes); break;} // Uniform Cost Search, Greedy Search, A* Search

			default -> {
                }
		}
	}
	

	

	// Helper methods for search algorithms (e.g., goal test, action generation)
	abstract protected  boolean isGoal(State state);
	abstract protected LinkedList<Node> expandNode(Node node, Map<String,Node> explored);



	// This method reconstructs the solution path once the goal is reached
	protected String constructSolution(Node node, boolean visualize, int expanded) {
		List<String> actions = new ArrayList<>();
		int pathCost = node.pathCost;
		List<String> path = new ArrayList<>();
		path.add(node.toString());

		while (node.parent != null) {
			actions.add(node.action);
			node = node.parent;
			if(visualize){
			path.add(node.toString());}

		}
		if(visualize) {
		Collections.reverse(path);
		for(String s:path) {
			System.out.println(s);
		}}
		Collections.reverse(actions);
		return String.join(",", actions) + ";" + pathCost + ";" + expanded;
	}

	
}
