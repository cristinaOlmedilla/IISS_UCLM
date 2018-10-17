package problems.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import search.State;
import search.Action;
import search.SearchProblem;
import utils.Position;
import visualization.ProblemView;
import visualization.ProblemVisualizable;

/**
 * Extends SearchProblem and implements the functions which define the maze
 * problem. Always uses two cheeses.
 */
public class MazeProblem implements SearchProblem, ProblemVisualizable {

	// Uses always three cheeses (to make it easier implementation).
	private static final int NUM_CHEESES = 3;
	// Penalty factor for fight with the cat.
	private static final double PENALTY = 2;

	/* Maze */
	Maze maze;

	/** Builds a maze */
	public MazeProblem() {
		this.maze = new Maze(10);
	}

	public MazeProblem(int size, int seed, int cats) {
		this.maze = new Maze(size, seed, cats, NUM_CHEESES);
	}

	@Override
	public void setParams(String[] args) {
		// The maze already exists.
		// It is generated with the new parameters.
		int size = this.maze.size;
		int seed = this.maze.seed;
		int cats = this.maze.numCats;

		if (args.length == 3)
			try {
				size = Integer.parseInt(args[0]);
				cats = Integer.parseInt(args[1]);
				seed = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.out.println("Parameters for MazeProblem are incorrect.");
				return;
			}

		// Generates the new maze.
		this.maze = new Maze(size, seed, cats, NUM_CHEESES);
	}

	// PROBLEM REPRESENTATION (CORRESPONDS TO THE FUNCTIONS IN THE INTERFACE
	// SearchProblem)

	@Override
	public State initialState() {
		// TODO Auto-generated method stub
		return new MazeState(this.maze.input());

	}
	/**  Returns the state resulting of applying an action to another state */
	@Override
	public State applyAction(State state, Action action) {
		// TODO Auto-generated method stub

		
		
		int newX = ((MazeState)state).hamsterPosition.x;
		int newY = ((MazeState)state).hamsterPosition.y;		
		double newDamage = ((MazeState)state).damage;		
		Position newPosition = new Position(newX, newY);
		LinkedList<Position> newEatenCheeses =  (LinkedList<Position>)((MazeState)state).eatenCheeses.clone();
		
		
		
		switch (((MazeAction)action).getId()) {

		case "RIGHT":			
			newX +=1;	
			if(this.maze.containsCat(newX, newY)) newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);
			
		case "UP":
			newY -=1;
			if(this.maze.containsCat(newX, newY)) newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);
			
		case "LEFT":
			newX -=1;
			if(this.maze.containsCat(newX, newY)) newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);
			
		case "DOWN":
			newY +=1;
			if(this.maze.containsCat(newX, newY)) newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);
			
		case "EAT":
			if(this.maze.containsCheese(newX, newY)) newEatenCheeses.add(newPosition);
			return new MazeState(newX, newY, newEatenCheeses, newDamage);
			
		default:
			return state;
		}
		
	}

	/** Returns the set of actions that can be applied to a certain state */
	@Override
	public ArrayList<Action> getPossibleActions(State state) {
		// TODO Auto-generated method stub
		ArrayList<Action> possibleActions = new ArrayList<>();
		Position thisPosition = ((MazeState)state).hamsterPosition;
		//necesito saber : si esta en un muro o en las paredes????
		if(this.maze.containsCheese(thisPosition)){
			possibleActions.add(MazeAction.EAT);
		}
		if(thisPosition.x != 0){
			possibleActions.add(MazeAction.LEFT);
		}
		if(thisPosition.x != this.maze.size-1){ ///sin el menos 1???
			possibleActions.add(MazeAction.RIGHT);
		}
		if(thisPosition.y != 0){
			possibleActions.add(MazeAction.UP);
		}
		if(thisPosition.y != this.maze.size-1){ ///sin el menos 1???
			possibleActions.add(MazeAction.DOWN);
		}
		return possibleActions;
	}
	 /** Returns the cost of applying an action over a state */
	@Override
	public double cost(State state, Action action) {
		// TODO Auto-generated method stub
		//PARA QUE EL ACTION???
		if (((MazeState)state).damage == PENALTY){
			return  PENALTY;
		}else{
			return 1;
		}
		
	}

	/** Tests if an state is the goal */
	@Override
	public boolean testGoal(State chosen) {
		
		if (((MazeState)chosen).hamsterPosition.equals(maze.output()) && ((MazeState)chosen).cheeseCounter==3){
			return true;			
		}else{
			return false;
		}
		
	}

	/** The returns the heuristic value of an state */
	@Override
	public double heuristic(State state) {
		// TODO Auto-generated method stub
		//Min number of movements from hamster position to output position
		
		double heuristicValue = 0;
		double xDistance = Math.abs(((MazeState)state).hamsterPosition.x - this.maze.output().x);
		double yDistance = Math.abs(((MazeState)state).hamsterPosition.y - this.maze.output().y);
		heuristicValue = xDistance +yDistance;
		
		return heuristicValue;
	}

	// VISUALIZATION
	/** Returns a panel with the view of the problem. */
	@Override
	public ProblemView getView(int sizePx) {
		return new MazeView(this, sizePx);
	}
}
