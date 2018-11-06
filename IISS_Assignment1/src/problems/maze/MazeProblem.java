package problems.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
	// Infinity Value for the Heuristics
	private static final double INFINITY = Double.POSITIVE_INFINITY;
	

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

	/** Returns the state resulting of applying an action to another state */
	@Override
	public State applyAction(State state, Action action) {
		// TODO Auto-generated method stub

		int newX = ((MazeState) state).hamsterPosition.x;
		int newY = ((MazeState) state).hamsterPosition.y;
		double newDamage = ((MazeState) state).damage;
		//double newDamage = 0;
		Position newPosition = new Position(newX, newY);
		LinkedList<Position> newEatenCheeses = (LinkedList<Position>)((MazeState) state).eatenCheeses.clone();

				
		switch (((MazeAction) action).getId()) {

		case "RIGHT":
			newX += 1;
			MazeState newState1 = new MazeState(newX, newY,newEatenCheeses, newDamage );
			if (this.maze.containsCat(newX, newY))
				newState1.damage += PENALTY;
			return newState1;
			/*newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);*/

		case "UP":
			newY -= 1;
			MazeState newState2 = new MazeState(newX, newY,newEatenCheeses, newDamage );
			if (this.maze.containsCat(newX, newY))
				newState2.damage += PENALTY;
			return newState2;
				/*newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);*/

		case "LEFT":
			newX -= 1;
			MazeState newState3 = new MazeState(newX, newY,newEatenCheeses, newDamage );
			if (this.maze.containsCat(newX, newY))
				newState3.damage += PENALTY;
			return newState3;
				/*newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);*/

		case "DOWN":
			newY += 1;
			MazeState newState4 = new MazeState(newX, newY,newEatenCheeses, newDamage );
			if (this.maze.containsCat(newX, newY))
				newState4.damage += PENALTY;
			return newState4;
				/*newDamage = PENALTY;
			return new MazeState(newX, newY, newEatenCheeses, newDamage);*/

		case "EAT":	
			MazeState newState5 = new MazeState(newX, newY,newEatenCheeses, newDamage );
			if(!((MazeState) state).eatenCheeses.contains(newPosition))
				newState5.eatenCheeses.add(newPosition);
			return newState5;

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
		Set<Position> reachablePosition = maze.reachablePositions(thisPosition);

		if (this.maze.containsCheese(thisPosition)&& !((MazeState)state).eatenCheeses.contains(thisPosition)) {
			possibleActions.add(MazeAction.EAT);
		}
		if(this.maze.containsCat(thisPosition)&&(((MazeState)state).damage>PENALTY)) {
			System.out.print("\nYou die here, already damaged and "
							+ "there is a cat here\n");
		}else {
			for (Position position : reachablePosition) {

				if ((position.x == thisPosition.x)
						&& (position.y == thisPosition.y - 1)) {
					possibleActions.add(MazeAction.UP);
				}
				if ((position.x == thisPosition.x)
						&& (position.y == thisPosition.y + 1)) { 
					possibleActions.add(MazeAction.DOWN);
				}
				if ((position.y == thisPosition.y)
						&& (position.x == thisPosition.x - 1)) {
					possibleActions.add(MazeAction.LEFT);
				}
				if ((position.y == thisPosition.y)
						&& (position.x == thisPosition.x + 1)) { 
					possibleActions.add(MazeAction.RIGHT);
				}
			}
		}
		return possibleActions;
	}

	/** Returns the cost of applying an action over a state */
	@Override
	public double cost(State state, Action action) {
		// TODO Auto-generated method stub
		double one = 1;
		if (action.getId() == "EAT") {
			return one;
		} else if (((MazeState) state).damage > 0) {
			return PENALTY;
		} else {
			return one;
		}

	}

	/** Tests if an state is the goal */
	@Override
	public boolean testGoal(State chosen) {

		if (((MazeState) chosen).hamsterPosition.equals(maze.output())
				&& ((MazeState) chosen).cheeseCounter == 3) {
			return true;
		} else {
			return false;
		}

	}

	/** The returns the heuristic value of an state */
	@Override
	public double heuristic(State state) {
		// Minimum distance from my position to the exit passing by the closest cheese position

		double heuristicValue = 0;
		double shortestDistance = INFINITY;
		double distanceToNextCheese;
		double distanceToTheExit;
		int numberEatenCheeses = ((MazeState)state).cheeseCounter;
		
		Position closerCheesePos = ((MazeState)state).hamsterPosition; 
		Position actualCheesePos = null;
		
		Set<Position> cheesesPosition = new HashSet<Position>();
		cheesesPosition.addAll(this.maze.cheesePositions);
		
		LinkedList<Position> eatenCheesesPosition = (LinkedList<Position>)((MazeState)state).eatenCheeses.clone();
		Iterator<Position> iterator = cheesesPosition.iterator();
		while (numberEatenCheeses != 3) {
			//We target the first cheese on the list
			actualCheesePos = iterator.next();

			//We keeping searching if the cheese has been eaten
			if (!eatenCheesesPosition.contains(actualCheesePos)) {
				//Distance from HamsterPosition to the first cheese--> not eaten
				distanceToNextCheese = Math.abs(actualCheesePos.x - ((MazeState) state).hamsterPosition.x)
						+ Math.abs(actualCheesePos.y - ((MazeState) state).hamsterPosition.y);
				//We check whether this cheese is the closest from the hamster Position
				if (distanceToNextCheese < shortestDistance) {

					shortestDistance = distanceToNextCheese;
					closerCheesePos = actualCheesePos;
				} 
			}
			if (!iterator.hasNext()) {
				
				//Updating the variables
				numberEatenCheeses = 3;
				eatenCheesesPosition.add(closerCheesePos);
			}
		}
		
		//Distance from last cheese eaten to the exit
		if (numberEatenCheeses == 3) {
			
			distanceToTheExit = Math.abs(this.maze.output().x - closerCheesePos.x)
							  + Math.abs(this.maze.output().y - closerCheesePos.y);
			heuristicValue = shortestDistance + distanceToTheExit;
		}
		return heuristicValue;
		
		/*double heuristicValue = 0;
		double shortestDistance = INFINITY;
		double distanceToNextCheese;
		double distanceToTheExit;
		int numberEatenCheeses = ((MazeState)state).cheeseCounter;
		
		Position actualCheesePos; 
		Position myPosition;
		Position closerCheesePos = null;
		
		Set<Position> cheesesPosition = new HashSet<Position>();
		cheesesPosition.addAll(this.maze.cheesePositions);
		
		LinkedList<Position> eatenCheesesPosition = (LinkedList<Position>)((MazeState)state).eatenCheeses.clone();

		myPosition = ((MazeState)state).hamsterPosition;
		
		if(numberEatenCheeses != 3){
		
			while (numberEatenCheeses != 3) {
				//We target the first cheese on the list
				actualCheesePos = cheesesPosition.iterator().next();

				//We keeping searching if the cheese has been eaten
				while (eatenCheesesPosition.contains(actualCheesePos)){
					actualCheesePos = cheesesPosition.iterator().next();
				}
				
				//Distance from HamsterPosition/cheese to the next not eaten cheese
				distanceToNextCheese = Math.abs(actualCheesePos.x - myPosition.x)
									 + Math.abs(actualCheesePos.y - myPosition.y);

				//We check whether this cheese is the closest from the hamster Position
				if (distanceToNextCheese < shortestDistance) {

					shortestDistance = distanceToNextCheese;
					closerCheesePos = actualCheesePos;
				}

				if (!eatenCheesesPosition.iterator().hasNext()) {
					//We initialize the iterator so we cover all the elements of the Set for next steps
					((MazeState)state).eatenCheeses.iterator();
					//Updating the variables
					numberEatenCheeses += 1;
					eatenCheesesPosition.add(closerCheesePos);
					myPosition = closerCheesePos;
					heuristicValue = heuristicValue + shortestDistance;
				}
			}
			numberEatenCheeses = ((MazeState)state).cheeseCounter;
		}
		
		//Distance from last cheese eaten to the exit
		if (numberEatenCheeses == 3) {
					
			distanceToTheExit = Math.abs(this.maze.output().x - myPosition.x)
							  + Math.abs(this.maze.output().y - myPosition.y);
			heuristicValue = shortestDistance + distanceToTheExit;
		}
		return heuristicValue;*/
	}

	// VISUALIZATION
	/** Returns a panel with the view of the problem. */
	@Override
	public ProblemView getView(int sizePx) {
		return new MazeView(this, sizePx);
	}
}
