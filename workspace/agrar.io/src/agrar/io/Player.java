package agrar.io;

import java.awt.Color;
import java.awt.Point;

public abstract class Player extends Circle {
	protected String name;
	
	public Player(Controller parent, Point loc, int size, Color col, String name) {
		super(parent, loc, size, col);
		this.name = name;
		// TODO Auto-generated constructor stub
	}
	
	public abstract void moveToNewPosition();
	
	public String getName(){
		return name;
	}
	
	public void tryToEat(Circle c1){
		if(c1 != null){
			if(((Utility.getDistance(c1, this) - size) < c1.getSize()) && (c1.getSize() < this.getSize())){
				//gets the Size of the other Circle
				this.size += c1.getSize();
				c1.delete();
			}	
		}
	} 
	
}
