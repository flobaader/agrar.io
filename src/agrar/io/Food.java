package agrar.io;

import java.awt.Color;
import java.awt.Point;

public class Food extends Circle {
	
	public Food(Controller parent, Point loc, int size, Color col) {
		super(parent, loc, size, col);
	}

	public boolean isPlayer() {
		return false;
		
	}
}
