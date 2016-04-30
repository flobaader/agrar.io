package agrar.io;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class LocalPlayer extends Player {

	public LocalPlayer(Controller parent, Point loc, int size, Color col) {
		super(parent, loc, size, col);
		// TODO Auto-generated constructor stub
	}

	public void moveToNewPosition() {
		//TODO: Catch Mouse Position and set as new Position
		
		//Gets current Mouse Position
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		
		//Moves one step towards the mouse location
		System.out.println("X:" + b.getX() + " Y: " + b.getY());
	}

	public boolean isPlayer() {
		return true;
	}	
	
	
}
