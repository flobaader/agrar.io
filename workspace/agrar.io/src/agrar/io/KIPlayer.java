package agrar.io;

import java.awt.Color;
import java.awt.Point;

public class KIPlayer extends Player {

	public KIPlayer(Controller parent, Point loc, int size, Color col) {
		super(parent, loc, size, col);
		// TODO Auto-generated constructor stub
	}

	public void moveToNewPosition() {
		// TODO Add movement for KI Players
		
	}

	public boolean isPlayer() {
		return true;
	}

}
