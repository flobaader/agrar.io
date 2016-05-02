package agrar.io.controller;

import java.awt.Color;
import java.awt.Point;

public class KIPlayer extends Player {
	private Point target = null;

	public KIPlayer(Controller parent, Point loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		// TODO Auto-generated constructor stub
	}

	private void selectTarget() {
		// If no idea --> Generate random target

		// Trys to get nearest object
		if (parent.getNearestPlayer(this).getSize() < this.size) {
			this.target = parent.getNearestPlayer(this).getLocation();
		}else{
			this.target = Utility.mirrorPoint(this.getLocation(), parent.getNearestPlayer(this).getLocation());
		}

		// Selects random target
		if (target == null) {
			target = Utility.getRandomPoint(0, 1000);
		}

	}

	public void moveToNewPosition() {

		if (this.location == target) {
			target = null;
		}

		// gets new Target
		selectTarget();
		// Moves to the next position towards target
		this.location = Utility.nextStepTowards(getLocation(), target.getLocation());

		target = null;
		selectTarget();

		// Trys to eat nearest object
		tryToEat(parent.getNearestObject(this));
	}

	public boolean isPlayer() {
		return true;
	}

}
