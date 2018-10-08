package problems.maze;

import java.util.LinkedList;

import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 *  Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State implements Cloneable{
	
	/** An state is includes a position given by the coordinates (x,y) */
	public Position position;
	public int cheeseCounter;
	public int damage;
	
	public LinkedList<Position> eatenCheeses;
	
	//InitialState MazeState
	public MazeState(int x, int y){
		this.position = new Position(x,y);
		this.eatenCheeses = new LinkedList<>(); 
		this.cheeseCounter = eatenCheeses.size();		
	}
	public MazeState(int[] initialP){
		this.position = new Position(initialP);
		this.eatenCheeses = new LinkedList<>(); 
		this.cheeseCounter = eatenCheeses.size();		
	}
	
	//Overwrite States
	public MazeState(int x, int y, LinkedList<Position>eCheeses, int dam){
		this.position = new Position(x,y);
		this.eatenCheeses = eCheeses; 
		this.cheeseCounter = eatenCheeses.size();
		this.damage = dam;
	}
	public MazeState(int[] regularP, LinkedList<Position>eCheeses, int dam){
		this.position = new Position(regularP);
		this.eatenCheeses = eCheeses; 
		this.cheeseCounter = eatenCheeses.size();
		this.damage = dam;
	}

	@Override
	public boolean equals(Object anotherState) {
		// TODO Auto-generated method stub
		MazeState aState = (MazeState)anotherState;
		if(this.position.equals(aState.position)
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
		//NO CHABEMOS!!!
		//return Objects.hash(field1, filed2.... );
		return 0;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("State: Position-> "+this.position.toString()
				+" Eaten Cheeses-> "+this.cheeseCounter
				+" Damage-> "+this.damage);
		
	}
}
