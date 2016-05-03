package agrar.io.controller;

import java.awt.Color;
import java.awt.Point;

import agrar.io.vector;

public class KIPlayer extends Player {
	private vector targetLocation;

	public KIPlayer(Controller parent, vector loc, int size, Color col, String name) {
		super(parent, loc, size, col, name);
		// TODO Auto-generated constructor stub
	}

	private void selectTarget() {

		// Trys to get nearest Player
		Player nearPlayer = parent.getNearestPlayer(this);
		double distance = vector.vectorFromTo(this.getLocation(), nearPlayer.getLocation()).getLength();

		boolean isNear = ((distance - nearPlayer.getSize() - this.getSize()) < 100);
		boolean isBigger = ((nearPlayer.getSize() - this.size) > 10);

		if (isNear && isBigger) {
			targetLocation = vector.reverseVector(vector.vectorFromTo(this.getLocation(), nearPlayer.getLocation()));
		} else if (isNear && !isBigger) {
			targetLocation = vector.vectorFromTo(this.getLocation(), nearPlayer.getLocation());
		} else {
			// Searches for food
			if (parent.getNearestFood(this) != null) {
				targetLocation = parent.getNearestFood(this).getLocation();
			}

		}

		// Searches for food
		if (parent.getNearestFood(this) != null) {
			targetLocation = parent.getNearestFood(this).getLocation();
		}

	}

	public void moveToNewPosition() {

		// gets new Target
		selectTarget();
		// Moves to the next position towards target
		this.location = Utility.nextStepTowards(getLocation(), targetLocation);

		// Trys to eat nearest object
		tryToEat(parent.getNearestObject(this));
	}

	public boolean isPlayer() {
		return true;
	}

}
