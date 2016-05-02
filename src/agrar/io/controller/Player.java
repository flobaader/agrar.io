package agrar.io.controller;

import java.awt.Color;
import java.awt.Point;

import agrar.io.model.Circle;

public abstract class Player extends Circle {
	protected String name;
	
	public Player(Controller parent, Point loc, int size, Color col, String name) {
		super(parent, loc, size, col);
		this.name = name;
	}
	
	public abstract void moveToNewPosition();
	
	public String getName(){
		return name;
	}
	
	public void tryToEat(Circle c1){
		if(c1 != null){
			if(((Utility.getDistance(c1, this) - radius) < c1.getRadius()) && (c1.getSize() < this.getSize())){
				//gets the Size of the other Circle
				this.setSize(c1.getSize()+this.getSize());
				c1.delete();
			}	
		}
	} 
	
}