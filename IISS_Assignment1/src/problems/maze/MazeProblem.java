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
		double newDamage = ((MazeState) state).damage;
		int newX = ((MazeState) state).hamsterPosition.x;
		int newY = ((MazeState) state).hamsterPosition.y;

		// double newDamage = 0;
		Position newPosition = new Position(newX, newY);
		LinkedList<Position> newEatenCheeses = (LinkedList<Position>) ((MazeState) state).eatenCheeses.clone();

		// We will update the damage if there is a cat in the actual state, to propagate
		// it through
		// its successors

		switch (((MazeAction) action).getId()) {

		case "RIGHT":
			if (this.maze.containsCat(newX, newY))
				newDamage += PENALTY;
			newX += 1;
			MazeState newState1 = new MazeState(newX, newY, newEatenCheeses, newDamage);
			/*
			 * if (this.maze.containsCat(newX, newY)) newState1.damage += PENALTY;
			 */
			return newState1;

		case "UP":
			if (this.maze.containsCat(newX, newY))
				newDamage += PENALTY;
			newY -= 1;
			MazeState newState2 = new MazeState(newX, newY, newEatenCheeses, newDamage);
			/*
			 * if (this.maze.containsCat(newX, newY)) newState2.damage += PENALTY;
			 */
			return newState2;

		case "LEFT":
			if (this.maze.containsCat(newX, newY))
				newDamage += PENALTY;
			newX -= 1;
			MazeState newState3 = new MazeState(newX, newY, newEatenCheeses, newDamage);
			/*
			 * if (this.maze.containsCat(newX, newY)) newState3.damage += PENALTY;
			 */
			return newState3;

		case "DOWN":
			if (this.maze.containsCat(newX, newY))
				newDamage += PENALTY;
			newY += 1;
			MazeState newState4 = new MazeState(newX, newY, newEatenCheeses, newDamage);
			/*
			 * if (this.maze.containsCat(newX, newY)) newState4.damage += PENALTY;
			 */
			return newState4;

		case "EAT":
			MazeState newState5 = new MazeState(newX, newY, newEatenCheeses, newDamage);
			if (!((MazeState) state).eatenCheeses.contains(newPosition))
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
		Position thisPosition = ((MazeState) state).hamsterPosition;
		Set<Position> reachablePosition = maze.reachablePositions(thisPosition);

		if (this.maze.containsCheese(thisPosition) && !((MazeState) state).eatenCheeses.contains(thisPosition)) {
			possibleActions.add(MazeAction.EAT);
		}
		if (this.maze.containsCat(thisPosition) && (((MazeState) state).damage == PENALTY)) {
			System.out.print("\n-----------------------------------------------");
			System.out.print("\n------You would die here, you are already------");
			System.out.print("\n------  damaged and there is a cat here  ------");
			System.out.print("\n-----------------------------------------------\n");
		} else {
			for (Position position : reachablePosition) {

				if ((position.x == thisPosition.x) && (position.y == thisPosition.y - 1)) {
					possibleActions.add(MazeAction.UP);
				}
				if ((position.x == thisPosition.x) && (position.y == thisPosition.y + 1)) {
					possibleActions.add(MazeAction.DOWN);
				}
				if ((position.y == thisPosition.y) && (position.x == thisPosition.x - 1)) {
					possibleActions.add(MazeAction.LEFT);
				}
				if ((position.y == thisPosition.y) && (position.x == thisPosition.x + 1)) {
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

		if (((MazeState) chosen).hamsterPosition.equals(maze.output()) && ((MazeState) chosen).cheeseCounter == 3) {
			return true;
		} else {
			return false;
		}

	}

	/** The returns the heuristic value of an state */
	@Override
	public double heuristic2(State state) {
		// Minimum distance from my position to the exit passing by the closest cheese
		// position

		double heuristicValue = 0;
		double shortestDistance = INFINITY;
		double distanceToNextCheese;
		double distanceToTheExit;
		int numberEatenCheeses = ((MazeState) state).cheeseCounter;

		Position closerCheesePos = ((MazeState) state).hamsterPosition;
		Position actualCheesePos = null;

		Set<Position> cheesesPosition = new HashSet<Position>();
		cheesesPosition.addAll(this.maze.cheesePositions);

		LinkedList<Position> eatenCheesesPosition = (LinkedList<Position>) ((MazeState) state).eatenCheeses.clone();
		Iterator<Position> iterator = cheesesPosition.iterator();

		boolean enter = true;
		while (numberEatenCheeses != 3 && enter) {
			// We target the first cheese on the list
			actualCheesePos = iterator.next();

			// We keeping searching if the cheese has been eaten
			if (!eatenCheesesPosition.contains(actualCheesePos)) {
				// Distance from HamsterPosition to the first cheese--> not eaten
				distanceToNextCheese = Math.abs(actualCheesePos.x - ((MazeState) state).hamsterPosition.x)
						+ Math.abs(actualCheesePos.y - ((MazeState) state).hamsterPosition.y);
				// We check whether this cheese is the closest from the hamster Position
				if (distanceToNextCheese < shortestDistance) {

					shortestDistance = distanceToNextCheese;
					closerCheesePos = actualCheesePos;
				}
			}
			if (!iterator.hasNext()) {

				// Updating the variables
				enter = false;
				eatenCheesesPosition.add(closerCheesePos);
			}
		}

		// Distance from last cheese eaten to the exit
		if (numberEatenCheeses == 3) {
			// This is in case that the mouse has eaten already the 3 cheeses
			if (shortestDistance == INFINITY) {
				distanceToTheExit = Math.abs(this.maze.output().x - closerCheesePos.x)
						+ Math.abs(this.maze.output().y - closerCheesePos.y);
				heuristicValue = distanceToTheExit;
				// This is in case that the mouse haven't eaten all cheeses yet
			} else {
				distanceToTheExit = Math.abs(this.maze.output().x - closerCheesePos.x)
						+ Math.abs(this.maze.output().y - closerCheesePos.y);
				heuristicValue = shortestDistance + distanceToTheExit;
			}

		}
		return heuristicValue;

	}

	public double heuristic(State state) {
		// Minimum distance from my position to the exit passing by the closest cheese
		// position

		double heuristicValue = 0;
		double shortestDistance = INFINITY;
		double distanceToNextCheese;
		double distanceToTheExit;
		int numberEatenCheeses = ((MazeState) state).cheeseCounter;

		// We need the position that the hamster will theoretically arrive to,
		// and the position of the cheese that is closer to the hamster position
		// (the one we change for the heuristics)
		Position closerCheesePos = null;
		// Position actualCheesePos = null;
		Position hamsPosition = ((MazeState) state).hamsterPosition;

		// we will use this List to retrieve the actual cheeses that have been eaten
		// and the ones that we will introduce as the closest to the hamster position
		LinkedList<Position> eatenCheesesPosition = (LinkedList<Position>) ((MazeState) state).eatenCheeses.clone();

		// Actual position of the cheeses that have not been eaten and its iterator
		Set<Position> cheesesPosition = new HashSet<Position>();
		cheesesPosition.addAll(this.maze.cheesePositions);
		// Iterator<Position> iterator = cheesesPosition.iterator();

		 boolean enter = true;

		// while (numberEatenCheeses != 3 && enter) {
		while (numberEatenCheeses != 3) {
			// We target the first cheese on the list
			// actualCheesePos = iterator.next();
			//enter = false;
			for (Position actualCheesePos : cheesesPosition) {
				// We keeping searching if the cheese has been eaten
				if (!eatenCheesesPosition.contains(actualCheesePos)) {
					// Distance from HamsterPosition to the first cheese--> not eaten
					distanceToNextCheese = Math.abs(actualCheesePos.x - hamsPosition.x)
							+ Math.abs(actualCheesePos.y - hamsPosition.y);
					// We check whether this cheese is the closest from the hamster Position
					if (distanceToNextCheese < shortestDistance) {

						shortestDistance = distanceToNextCheese;
						closerCheesePos = actualCheesePos;
					}
				}
			}

			// Once it finishes the first iteration we have to cover the
			// Updating the variables

			eatenCheesesPosition.add(closerCheesePos);
			numberEatenCheeses += 1;
			hamsPosition = closerCheesePos;
			heuristicValue = heuristicValue + shortestDistance;

		}

		// Distance from last cheese eaten to the exit
		//If all cheeses have been eaten before call heuristics, it give us 
		//the distance from  its position to the exit
		//if ((numberEatenCheeses == 3)&&enter) {

			if (shortestDistance >= INFINITY) {
				hamsPosition = ((MazeState) state).hamsterPosition;
				distanceToTheExit = Math.abs(this.maze.output().x - hamsPosition.x)
						+ Math.abs(this.maze.output().y - hamsPosition.y);
				heuristicValue = distanceToTheExit;
			} else {
				distanceToTheExit = Math.abs(this.maze.output().x - hamsPosition.x)
						+ Math.abs(this.maze.output().y - hamsPosition.y);
				heuristicValue = shortestDistance + distanceToTheExit;
			}

		
		return heuristicValue;
	}

	// VISUALIZATION
	/** Returns a panel with the view of the problem. */
	@Override
	public ProblemView getView(int sizePx) {
		return new MazeView(this, sizePx);
	}
}
