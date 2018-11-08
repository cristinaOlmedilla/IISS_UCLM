package algorithms;
import java.util.*;

import search.*;

public class BreathFirst extends SearchAlgorithm{

	
	
	
	@Override
	public void setParams(String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		State initialState = problem.initialState();
		Node initialNode = new Node(initialState);
		
		Queue<Node> openNodes = new LinkedList<Node>();
		
		LinkedList<State> exploredStates = new LinkedList<State>();

		// We include the first Node
		openNodes.add(initialNode);
		generatedNodes++;

		/*
		 * We keep searching till finding a solution or till exploring all the possible
		 * paths.
		 */
		while ((openNodes.peek() != null) && !solutionFound) {

			// We extract the first Node in the queue
			Node actualNode = openNodes.poll();
			System.out.print("\n");
			System.out.print("\n****************************************************");
			System.out.print("\nNEW NODE: " + actualNode );
			System.out.print("\n	-------------------------------------------- ");

			State actualNodeState = (State) actualNode.getState();

			// It will be explored if the State has not been explored before
			if (!exploredStates.contains(actualNodeState)) {
				// We explore the Node

				// First we check if it is the Goal State
				if (problem.testGoal(actualNode.getState())) {
					
					actionSequence.addAll(recoverPath(actualNode));
					solutionFound = true;
					
				} else {
					
					// If is not the goal we expand the Node
					Queue<Node> successorNodes = new LinkedList<Node>();
					successorNodes.addAll(getSuccessors(actualNode));

					System.out.print("\nSUCCESORS OF THE NODE:");
					for (Node n : successorNodes) {
						openNodes.add(n);
						System.out.print("\n	Node: " + n.toString());
						System.out.print("\n	Heuristica del nodo: " + n.getHeuristic());
						System.out.print("\n	Evaluacion del nodo: " + n.getEvaluation());
						System.out.print("\n	-------------------------------------------- ");
					}

					System.out.print("\n****************************************************");
					//We check for the max size of the set of openNodes
					long temporalmax = openNodes.size();
					if (temporalmax > openMaxSize)
						openMaxSize = temporalmax;
				}
				// We finish exploring the node, so we include it into
				// the explored States
				// and we check for the max size of the set of exploredNodes
				exploredStates.add(actualNode.getState());
				long temporalmax = exploredStates.size();
				if (temporalmax > exploredMaxSize)
					exploredMaxSize = temporalmax;

			} else {
				System.out.print("\n----------------------------------------------------");
				System.out.print("\n--------This node has been already explored---------");
				System.out.print("\n----------------------------------------------------\n");
			}
		}

		if (solutionFound) {

			System.out.print("\n****************************************************");
			System.out.print("\n************       Solution found       ************");
			System.out.print("\n****************************************************");

			System.out.print("\nOpen Nodes: 					" + generatedNodes );
			System.out.print("\nExplored Nodes: 				" + exploredStates.size() );
			System.out.print("\nExpanded Nodes: 				" + expandedNodes );
			System.out.print("\nTotal Cost:						" + totalCost);
			System.out.print("\nMaximum size of the set of open nodes:		" + openMaxSize);
			System.out.print("\nMaximum size of the set of explored nodes:	" + exploredMaxSize);
			System.out.print("\nPath: " );
			System.out.print("\n	Actions: ");
			for (Action action : actionSequence) {
				System.out.print(action.getId()+"--");
			}
			System.out.print("\n****************************************************\n");
			System.out.print("\n");

		} else {
			System.out.print("\n****************************************************");
			System.out.print("\n************Failure -- No solution found************");
			System.out.print("\n****************************************************");
			System.out.print("\nOpen Nodes: 					" + generatedNodes);
			System.out.print("\nExplored Nodes: 				" + exploredStates.size());
			System.out.print("\nExpanded Nodes: 				" + expandedNodes);
			System.out.print("\nMaximum size of the set of open nodes:		" + openMaxSize);
			System.out.print("\nMaximum size of the set of explored nodes:	" + exploredMaxSize);
			System.out.print("\n****************************************************\n");
			System.out.print("\n");

		}
	}
	/**Method that it return the path follow from the root, or the beginning of the path follow by a Node.
	 * It saves in a List the sequence of parents giving a Node*/
	public ArrayList<Action> recoverPath(Node node){
		ArrayList<Action> path = new ArrayList<Action>();
		while (!node.getState().equals(problem.initialState())){
			path.add(node.getAction());
			totalCost = node.getCost() + totalCost;
			node = node.getParent();
		}
		Collections.reverse(path);
		
		return path;
	}
	

}
