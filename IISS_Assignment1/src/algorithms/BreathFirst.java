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
		ArrayList<Node> succesors = new ArrayList<Node>();
		
		//we include the first Node
		openNodes.add(initialNode);
		generatedNodes++;
		
		//We keep searching till finding a solution or till exploring all
		//the possible paths.
		while((openNodes!=null)&& !solutionFound){
			//We extract the first Node in the queue
			
			Node actualNode = openNodes.poll();
			//It will be explored if the State has not been explored before
			if (!exploredStates.contains(actualNode.getState())){
				//We explore the Node
				
				//First we check if it is the Goal State
				if(problem.testGoal(actualNode.getState())){
					actionSequence.addAll(recoverPath(actualNode));
					solutionFound = true;
				}else{
					//If is not the goal we expand the Node
					succesors.addAll(getSuccessors(actualNode));
					openNodes.addAll(succesors);
				}
				//We finish exploring the node, so we include it into
				//the explored States
				exploredStates.add(actualNode.getState());
				
			}
		}
		
		if (solutionFound) {
			System.out.print("********************************");
			System.out.print("**       Solution found       **");
			System.out.print("********************************");
			
			System.out.print("Open Nodes: 		"+generatedNodes);
			System.out.print("Explored Nodes: 	"+exploredStates.size() );
			System.out.print("Expanded Nodes: 	"+expandedNodes);
			System.out.print("Total Cost:		"+totalCost);
			System.out.print("Path: ");
			for (Action action : actionSequence) {
				System.out.print("	Action: " + action.getId());
			}
			System.out.print("********************************");
			
		}else{
			System.out.print("********************************");
			System.out.print("**Failure -- No solution found**");
			System.out.print("********************************");
			System.out.print("Open Nodes: 		"+generatedNodes);
			System.out.print("Explored Nodes: 	"+exploredStates.size() );
			System.out.print("Expanded Nodes: 	"+expandedNodes);
			System.out.print("Total Cost:		"+totalCost);
			System.out.print("********************************");
			
			
		}
	}
	
	public ArrayList<Action> recoverPath(Node node){
		ArrayList<Action> path = new ArrayList<Action>();
		while (!node.getState().equals(problem.initialState())){
			path.add(node.getAction());
			totalCost = node.getCost() + totalCost;
			node = node.getParent();
		}
		
		return path;
	}
	

}
