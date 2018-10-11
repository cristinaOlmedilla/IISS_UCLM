package problems.maze;

import java.util.LinkedList;
import java.util.Objects;

import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 *  Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State implements Cloneable{
	
	/** An state is includes a position given by the coordinates (x,y) */
	public Position hamsterPosition;
	public int cheeseCounter;
	public int damage;
	
	public LinkedList<Position> eatenCheeses;
	
	//InitialState MazeState
	public MazeState(int x, int y){
		this.hamsterPosition = new Position(x,y);
		this.eatenCheeses = new LinkedList<>(); 
		this.cheeseCounter = eatenCheeses.size();		
	}
	public MazeState(Position initialP){
		this.hamsterPosition = initialP;
		this.eatenCheeses = new LinkedList<>(); 
		this.cheeseCounter = eatenCheeses.size();		
	}
	
	//Overwrite States
	public MazeState(int x, int y, LinkedList<Position>eCheeses, int dam){
		this.hamsterPosition = new Position(x,y);
		this.eatenCheeses = eCheeses; 
		this.cheeseCounter = eatenCheeses.size();
		this.damage = dam;
	}
	public MazeState(Position initialP, LinkedList<Position>eCheeses, int dam){
		this.hamsterPosition = initialP;
		this.eatenCheeses = eCheeses; 
		this.cheeseCounter = eatenCheeses.size();
		this.damage = dam;
	}

	@Override
	public boolean equals(Object anotherState) {
		// TODO Auto-generated method stub
		MazeState aState = (MazeState)anotherState;
		if(this.hamsterPosition.equals(aState.hamsterPosition)
				&&this.eatenCheeses.equals(aState.eatenCheeses)
				&&this.damage==aState.damage){
			return true;
		}else{
			return false;
				}
	
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		
		return Objects.hash(hamsterPosition, eatenCheeses, damage, cheeseCounter);
		
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("State: Position-> "+this.hamsterPosition.toString()
				+" Eaten Cheeses-> "+this.cheeseCounter
				+" Damage-> "+this.damage);
		
	}
}
