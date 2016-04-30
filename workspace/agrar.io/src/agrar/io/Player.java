package agrar.io;

import java.awt.Color;
import java.awt.Point;

public abstract class Player extends Circle {
	
	public Player(Controller parent, Point loc, int size, Color col) {
		super(parent, loc, size, col);
		// TODO Auto-generated constructor stub
	}

	private Controller parent;
	
	
	public abstract void moveToNewPosition();
	
	public void tryToEat(Circle c1){
		if(Utility.getDistance(c1, this) < this.size){
			
			
		}
		
	} 
	
}
