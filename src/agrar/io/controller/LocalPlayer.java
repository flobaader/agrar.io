package agrar.io.controller;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import agrar.io.Vector;

public class LocalPlayer extends Player {

	public LocalPlayer(Controller parent, Vector loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		// TODO Auto-generated constructor stub
	}

	public void moveToNewPosition() {
		// TODO: Catch Mouse Position and set as new Position

		// Gets current Mouse Position
		PointerInfo a = MouseInfo.getPointerInfo();
		Point mouseLocation = a.getLocation();

		Vector offset = parent.getOffset();
		movement_direction = Vector.substractVector(Vector.pointToVector(mouseLocation), offset);
		Vector realVector = Vector.addVector(movement_direction, this.location);
		// Moves one step towards the mouse location
		
		this.location = Utility.nextStepTowards(this.location, realVector);

		// Checks if next Object is very close
		tryToEat(parent.getNearestObject(this));
	}

	public boolean isPlayer() {
		return true;
	}

}
